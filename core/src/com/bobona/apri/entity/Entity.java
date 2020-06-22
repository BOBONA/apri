package com.bobona.apri.entity;

import com.badlogic.gdx.utils.Array;
import com.bobona.apri.*;
import com.bobona.apri.entity.tools.Damage;
import com.bobona.apri.entity.tools.EntityProperty;
import com.bobona.apri.entity.tools.Mixin;
import com.bobona.apri.entity.tools.StatusEffect;
import com.bobona.apri.physics.Collision;
import com.bobona.apri.physics.PhysicsBody;
import com.bobona.apri.visual.animation.AnimationInstance;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.Transform;
import com.bobona.apri.visual.ui.Ui;
import com.bobona.apri.utils.Config;
import com.bobona.apri.utils.Vector2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Entity {

    public ApriWorld world;
    public EntityData entityData;
    public PhysicsBody body;
    EntityProperty<Object> properties;
    public int hitpoints;
    AnimationInstance animation;
    List<Collision> contactBeginQueue;
    List<Collision> contactEndQueue;
    Array<StatusEffect> statusEffects;
    public Map<Ui, Renderable> uiList;

    public Entity(ApriWorld world, EntityData entityData, float xPos, float yPos) {
        this.world = world;
        this.entityData = entityData;
        properties = new EntityProperty<>(Object.class);
        body = Config.instantiate(
                Config.PHYSICS_BODY_CLASS,
                Config.PHYSICS_WORLD_CLASS.cast(world.physicsWorld),
                this,
                entityData.density,
                xPos, yPos,
                entityData.width, entityData.height,
                entityData.collisionHitbox,
                entityData.sensors);
        hitpoints = entityData.maxHitpoints;
        contactBeginQueue = new ArrayList<>();
        contactEndQueue = new ArrayList<>();
        statusEffects = new Array<>();
        uiList = new HashMap<>();
        for (Ui ui : entityData.uiMap.values()) {
            uiList.put(ui, new Renderable(new Transform(0, 0, 1, 0), null));
            uiList.get(ui).setVisible(false);
        }
        setDefaultAnimation();
        entityData.mixins.forEach((mixin, initArgs) -> mixin.init(this, initArgs));
        entityData.intializeFunction.accept(this);
    }

    public void update() {
        animation.step();
        for (BiConsumer<Entity, Collision> contactBeginFunction : entityData.contactBeginFunctions) {
            contactBeginQueue.forEach(collision -> contactBeginFunction.accept(this, collision));
        }
        for (Mixin<Entity, EntityData.Builder> mixin : entityData.mixins.keySet()) {
            contactBeginQueue.forEach(collision -> mixin.contactBegin(this, collision));
        }
        contactBeginQueue.clear();
        for (BiConsumer<Entity, Collision> contactEndFunction : entityData.contactEndFunctions) {
            contactEndQueue.forEach(collision -> contactEndFunction.accept(this, collision));
        }
        for (Mixin mixin : entityData.mixins.keySet()) {
            contactEndQueue.forEach(collision -> mixin.contactEnd(this, collision));
        }
        contactEndQueue.clear();
        entityData.updateFunctions.forEach(updateFunction -> updateFunction.accept(this));
        entityData.mixins.keySet().forEach(mixin -> mixin.update(this));
        statusEffects.forEach(this::handleStatus);
        if (hitpoints < 0) {
            onDeath();
        }
    }

    // the entity that is collided into is collision.other. collision.entity is just a reference to this entity
    public void contactBegin(Collision collision) {
        contactBeginQueue.add(collision);
    }

    public void contactEnd(Collision collisionEnd) {
        contactEndQueue.add(collisionEnd);
    }

    public void onDeath() {
        world.physicsWorld.destroyBody(body);
        world.deletionQueue.add(this);
    }

    public void handleStatus(StatusEffect statusEffect) {
        // if the frameDuration of the status effect is done then remove it
        if (statusEffect.frameStarted + statusEffect.frameDuration <= world.ellapsedFrames && statusEffect.frameDuration != -1) {
            statusEffects.removeValue(statusEffect, true);
            //overlaidAnimations.removeValue(entityData.statusAnimations.getPath(statusEffect.type), false);
        }
    }

    public void damage(float amount, Damage type) {
        hitpoints -= amount * entityData.damageMultipliers.get(type);
    }

    public void addStatus(StatusEffect.Type statusType, int level, int duration) {
        addStatus(statusType, level, duration, false);
    }

    public void addStatus(StatusEffect.Type statusType, int level, int duration, boolean stack) {
        // if something else is mutually exclusive to this effect but not the other way around, the effect will fail
        for (StatusEffect statusEffect : statusEffects) {
            if (Arrays.asList(statusEffect.type.mutuallyExclusiveStatuses).contains(statusType) &&
                    !Arrays.asList(statusType.mutuallyExclusiveStatuses).contains(statusEffect.type)) {
                return;
            }
        }
        // if stack is false, then only add if the effect isn't already present
        if (!stack) {
            boolean effectIsPresent = Arrays.stream(statusEffects.toArray())
                                            .anyMatch(statusEffect -> statusEffect.type.equals(statusType));
            if (effectIsPresent) {
                return;
            }
        }
        statusEffects.add(new StatusEffect(statusType, level, duration, world.ellapsedFrames));
        // handle mutually exclusive statuses
        for (StatusEffect statusEffect : statusEffects) {
            if (Arrays.asList(statusType.mutuallyExclusiveStatuses).contains(statusEffect.type)) {
                statusEffects.removeValue(statusEffect, false);
            }
        }
    }

    public List<Collision> getSensorCollisions(String sensorName, Class<? extends Entity> type) {
        return body.getCollisions()
                .stream()
                .filter(collision -> collision.sensor.equals(sensorName) && collision.other.getEntityClass().equals(type))
                .collect(Collectors.toList());
    }

    public void setAnimation(String animation) {
        this.animation = new AnimationInstance(entityData.animations.get(animation));
    }

    public void setDefaultAnimation() {
        setAnimation("default");
    }

    public void toggleUi(String uiKey) {
        Ui ui = entityData.uiMap.get(uiKey);
        uiList.get(ui).setVisible(!uiList.get(ui).isVisible());
    }

    public List<Renderable> getUiRenderables() {
        List<Renderable> renderables = new ArrayList<>();
        for (Ui ui : uiList.keySet()) {
            Renderable uiData = uiList.get(ui);
            if (uiList.get(ui).isVisible()) {
                // transform renderables
                List<Renderable> uiRenderables = ui.getRenderables(this, uiData.getTransform().scaleX, uiData.getTransform().scaleY);
                Renderable.transformRenderables(uiRenderables, uiData.getTransform().xOffset, uiData.getTransform().yOffset);
                if (!uiData.useGameCoords()) {
                    for (Renderable renderable : uiRenderables) {
                        renderable.setUseGameCoords(false);
                    }
                }
                renderables.addAll(uiRenderables);
            }
        }
        return renderables;
    }

    // Returns different references each time
    public List<Renderable> getBodyRenderables() {
        List<Renderable> renderables = animation.getRenderables();
        for (Renderable renderable : renderables) {
            Transform transform = renderable.getTransform();
            transform.xOffset *= body.getWidth();
            transform.yOffset *= body.getHeight();
            transform.xOffset += body.getPosition().x;
            transform.yOffset += body.getPosition().y;
            transform.scaleX *= body.getWidth();
            transform.scaleY *= body.getHeight();
            enlargeTransform(transform);
        }
        return renderables;
    }

    private void enlargeTransform(Transform transform) {
        float widenFactor = 1.02f;
        transform.scaleX *= widenFactor;
        transform.scaleY *= widenFactor;
        transform.xOffset -= (widenFactor - 1) / 2;
        transform.yOffset -= (widenFactor - 1) / 2;
    }

    public Renderable getUiRenderable(String key) {
        return uiList.get(entityData.uiMap.get(key));
    }

    public EntityProperty<?> getProperty(String path) {
        return getProperty(path, Object.class);
    }

    public <T> EntityProperty<T> getProperty(String path, Class<T> type) {
        return properties.getPath(path, type);
    }

    public Class<? extends Entity> getEntityClass() {
        return Entity.class;
    }

    public SaveData exportSaveData() {
        Map<String, Object> fields = new HashMap<>();
        Class superClass = getEntityClass();
        while (superClass != Entity.class.getSuperclass()) {
            for (Field field : superClass.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    fields.put(field.getName(), field.get(this));
                } catch (IllegalAccessException ignored) {}
            }
            superClass = superClass.getSuperclass();
        }
        onDeath();
        return new SaveData(getEntityClass(), properties, entityData, body, fields);
    }

    public static class Properties {

        public static final String CAN_JUMP = "mixin.canJump.canJump";
    }

    public class SaveData {

        Class<? extends Entity> entityClass;
        EntityProperty properties;
        EntityData entityData;
        PhysicsBody body;
        Map<String, Object> fields;

        public SaveData(Class<? extends Entity> entityClass, EntityProperty properties, EntityData entityData, PhysicsBody body, Map<String, Object> fields) {
            this.entityClass = entityClass;
            this.properties = properties;
            this.entityData = entityData;
            this.body = body;
            this.fields = fields;
        }

        public Entity createEntity(Vector2 position) {
            Class[] constructorArguments = new Class[]{ApriWorld.class, entityData.getClass(), float.class, float.class};
            try {
                Entity entity = entityClass.getConstructor(constructorArguments).newInstance(world, entityData, position.x, position.y);
                for (String key : fields.keySet()) {
                    if (key.equals("body")) {
                        continue;
                    }
                    Map<String, Field> fieldsList = new HashMap<>();
                    Class superClass = getEntityClass();
                    while (superClass != Entity.class.getSuperclass()) {
                        for (Field field : superClass.getDeclaredFields()) {
                            field.setAccessible(true);
                            fieldsList.put(field.getName(), field);
                        }
                        superClass = superClass.getSuperclass();
                    }
                    Field field = fieldsList.get(key);
                    field.setAccessible(true);
                    field.set(entity, fields.get(key));
                }
                return entity;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
                return null;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Expected entityClass constructor: " + Arrays.toString(constructorArguments));
            }
        }
    }
}
