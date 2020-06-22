package com.bobona.apri.visual.animation;

import com.badlogic.gdx.Gdx;
import com.bobona.apri.ApriWorld;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.Transform;

import java.util.*;

public class EntityAnimation {

    private String name;
    public float width, height;
    private Map<String, Layer> layers;
    private List<List<Renderable>> renderables;
    private boolean registerTextures;
    private String primaryLayer;

    public EntityAnimation(GsonEntityAnimation g, Map<String, String> textures, boolean registerTextures) {
        layers = new HashMap<>();
        name = g.name;
        if (g.width != 0) {
            width = g.width;
        } else {
            width = 1;
        }
        if (g.height != 0) {
            height = g.height;
        } else {
            height = 1;
        }
        if (g.layers != null) {
            Arrays.stream(g.layers)
                    .forEach(layer -> layers.put(layer.name, new Layer(this, layer)));
        }
        renderables = new ArrayList<>();
        this.registerTextures = registerTextures;
        primaryLayer = "visual";
        setTextures(textures);
        initializeRenderables();
    }

    public void setTextures(Map<String, String> textures) {
        for (String shapeName : textures.keySet()) {
            String path = textures.get(shapeName);
            if (registerTextures) {
                ApriWorld.addTexture(path);
            }
            getVisualLayer().getShape(shapeName).setTexture(path);
        }
    }

    public void initializeRenderables() {
        renderables.clear();
        Layer visuals = getVisualLayer();
        for (int i = 0; i < visuals.getFrameCount(); i++) {
            for (String key : visuals.getShapeKeySet()) {
                renderables.add(new ArrayList<>());
                Shape shape = visuals.getShape(key);
                Transform transform = visuals.getAnimationFrame(i).getTransform(key);
                if (transform.isVisible) {
                    renderables.get(i).add(new Renderable(
                            new Transform(
                                    true,
                                    transform.zIndex == 0 ? shape.getZIndex() : transform.zIndex,
                                    shape.getX() + transform.xOffset,
                                    shape.getY() + transform.yOffset,
                                    shape.getWidth() * transform.scaleX,
                                    shape.getHeight() * transform.scaleY,
                                    shape.rotation + transform.rotation,
                                    transform.rotOrigin,
                                    transform.rotX,
                                    transform.rotY
                            ), shape.getTexture())
                    );
                }
            }
            renderables.set(i, Renderable.orderRenderables(renderables.get(i)));
        }
    }

    public List<Renderable> getFrameRenderables(int frame) {
        Layer visuals = getVisualLayer();
        List<Renderable> renderables = new ArrayList<>();
        for (String key : visuals.getShapeKeySet()) {
            Shape shape = visuals.getShape(key);
            Transform transform = visuals.getFrame(frame).getTransform(key);
            if (transform.isVisible) {
                renderables.add(new Renderable(
                        new Transform(
                                true,
                                transform.zIndex == 0 ? shape.getZIndex() : transform.zIndex,
                                shape.getX() + transform.xOffset,
                                shape.getY() + transform.yOffset,
                                shape.getWidth() * transform.scaleX,
                                shape.getHeight() * transform.scaleY,
                                shape.rotation + transform.rotation,
                                transform.rotOrigin,
                                transform.rotX,
                                transform.rotY
                        ), shape.getTexture())
                );
            }
        }
        return renderables;
    }

    public String getName() {
        return name;
    }

    public Layer getLayer(String key) {
        return layers.get(key);
    }

    public Set<String> getLayerKeySet() {
        return layers.keySet();
    }

    public Layer getVisualLayer() {
        return getLayer("visual");
    }

    public void addLayer(String name, int frameCount) {
        layers.put(name, new Layer(this, frameCount));
        if (layers.keySet().size() == 1) {
            primaryLayer = name;
        }
    }

    public Layer getPrimaryLayer() {
        return layers.get(primaryLayer);
    }

    public static GsonEntityAnimation gsonAnimation(String path) {
        return ApriWorld.gson.fromJson(
                Gdx.files.internal(path).readString(),
                GsonEntityAnimation.class
        );
    }

    public List<List<Renderable>> getRenderableList(boolean copy) {
        if (copy) {
            List<List<Renderable>> renderables = new ArrayList<>();
            for (List<Renderable> layer : this.renderables) {
                renderables.add(new ArrayList<>());
                for (Renderable renderable : layer) {
                    renderables.get(renderables.size() - 1).add(new Renderable(renderable));
                }
            }
            return renderables;
        } else {
            return renderables;
        }
    }
}