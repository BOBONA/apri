package com.bobona.apri.entity;

import com.bobona.apri.entity.tools.Damage;
import com.bobona.apri.physics.Collision;
import com.bobona.apri.entity.tools.Mixin;
import com.bobona.apri.visual.animation.EntityAnimation;
import com.bobona.apri.visual.ui.Ui;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntityData {

    public String typeId;
    public String texture;
    public float height;
    public float width;
    public float density;
    public float[] collisionHitbox;
    public Map<String, float[]> sensors;
    public Consumer<Entity> intializeFunction;
    public List<Consumer<Entity>> updateFunctions;
    public List<BiConsumer<Entity, Collision>> contactBeginFunctions;
    public List<BiConsumer<Entity, Collision>> contactEndFunctions;
    public Map<Mixin<Entity, EntityData.Builder>, List<Object>> mixins;
    public int maxHitpoints;
    public Map<Damage, Double> damageMultipliers;
    public Map<String, EntityAnimation> animations;
    public Map<String, Ui> uiMap;

    public EntityData(Builder<?> builder) {
        typeId = builder.typeId;
        texture = builder.texture;
        height = builder.height;
        width = builder.width;
        density = builder.density;
        collisionHitbox = builder.collisionHitbox;
        sensors = builder.sensors;
        intializeFunction = builder.intializeFunction;
        updateFunctions = builder.updateFunctions;
        contactBeginFunctions = builder.contactBeginFunctions;
        contactEndFunctions = builder.contactEndFunctions;
        mixins = builder.mixins;
        maxHitpoints = builder.maxHitpoints;
        damageMultipliers = builder.damageMultipliers;
        animations = builder.animations;
        uiMap = new HashMap<>();
        builder.uiList.forEach(ui -> uiMap.put(ui.getName(), ui));
    }

    // the weird stuff with the T return types is a solution to fix the builder pattern with inheritance. this way, the order of the method calls shouldn't need to be chained in a certain order
    public static class Builder<T extends Builder<?>> {

        String typeId;
        String texture;
        float density;
        float height;
        float width;
        float[] collisionHitbox;
        Map<String, float[]> sensors = new HashMap<>();
        Consumer<Entity> intializeFunction = entity -> {};
        List<Consumer<Entity>> updateFunctions = new ArrayList<>();
        List<BiConsumer<Entity, Collision>> contactBeginFunctions = new ArrayList<>();
        List<BiConsumer<Entity, Collision>> contactEndFunctions = new ArrayList<>();
        Map<Mixin<Entity, EntityData.Builder>, List<Object>> mixins = new HashMap<>();
        int maxHitpoints = 0;
        Map<Damage, Double> damageMultipliers = new HashMap<>();
        Map<String, EntityAnimation> animations = new HashMap<>();
        Map<String, String> animationPaths = new HashMap<>();
        Map<String, Map<String, String>> animationTextures = new HashMap<>();
        String lastAnimationAdded;
        List<Ui> uiList = new ArrayList<>();

        public Builder(String typeId, String texture, float height, float width, float density, float[] collisionHitbox) {
            this.height = height;
            this.width = width;
            this.density = density;
            this.typeId = typeId;
            this.texture = texture;
            this.collisionHitbox = collisionHitbox;
            for (Damage damageType : Damage.values()) {
                damageMultipliers.put(damageType, 1d);
            }
            addAnimation("default", "default.json");
            addTextureToAnimation("main", texture);
        }

        public T setInitializeFunction(Consumer<Entity> function) {
            intializeFunction = function;
            return (T) this;
        }

        public T onUpdateHandler(Consumer<Entity> function) {
            updateFunctions.add(function);
            return (T) this;
        }

        public T onContactBeginHandler(BiConsumer<Entity, Collision> function) {
            contactBeginFunctions.add(function);
            return (T) this;
        }

        public T onContactEndHandler(BiConsumer<Entity, Collision> function) {
            contactEndFunctions.add(function);
            return (T) this;
        }
        
        public T mixin(Mixin mixin, Object... mixinArgs) {
            // verify that the mixinArgs match up the mixin's actual required args
            for (int i = 0; i < mixinArgs.length; i++) {
                Object initArg = mixinArgs[i];
                if (
                        initArg.getClass().getClasses().length == 0 && initArg.getClass() != mixin.requiredArgTypes[i] ||
                        initArg.getClass().getClasses().length > 0 && !Arrays.asList(initArg.getClass().getClasses()).contains(mixin.requiredArgTypes[i])) {

                        throw new IllegalArgumentException(mixin.getName() + " received the wrong arguments!");
                }
            }
            mixins.put(mixin, Arrays.asList(mixinArgs));
            mixin.buildingPhase(this);
            return (T) this;
        }

        public T maxHitpoints(int maxHitpoints) {
            this.maxHitpoints = maxHitpoints;
            return (T) this;
        }

        public T damageMultiplier(Damage damage, Double multiplier) {
            damageMultipliers.put(damage, multiplier);
            return (T) this;
        }

        public T invincible() {
            for (Damage damageType : Damage.values()) {
                damageMultipliers.put(damageType, 0d);
            }
            return (T) this;
        }

        public T sensor(String id, float[] vertices) {
            if (id.equals("this")) {
                throw new RuntimeException("'this' is a reserved sensor id, pick something else");
            }
            sensors.put(id, vertices);
            return (T) this;
        }

        public T addAnimation(String name, String path) {
            animationPaths.put(name, path);
            animationTextures.put(name, new HashMap<>());
            lastAnimationAdded = name;
            return (T) this;
        }

        public T addTextureToAnimation(String animation, String shape, String path) {
            animationTextures.get(animation).put(shape, path);
            return (T) this;
        }

        public T addTextureToAnimation(String shape, String path) {
            return addTextureToAnimation(lastAnimationAdded, shape, path);
        }

        public T addUi(String name, String path) {
            try {
                File file = new File(path);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(file);
                uiList.add(new Ui(name, document));
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
            return (T) this;
        }

        public EntityData build() {
            for (String animationName : animationPaths.keySet()) {
                animations.put(animationName, new EntityAnimation(
                        EntityAnimation.gsonAnimation("animations/" + animationPaths.get(animationName)),
                        animationTextures.get(animationName), true));
            }
            return new EntityData(this);
        }
    }
}
