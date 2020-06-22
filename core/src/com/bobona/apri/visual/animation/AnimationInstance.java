package com.bobona.apri.visual.animation;

import com.bobona.apri.visual.Renderable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationInstance {

    private EntityAnimation animation;
    private Map<String, Integer> frames;

    public AnimationInstance(EntityAnimation animation) {
        this.animation = animation;
        frames = new HashMap<>();
        for (String layer : animation.getLayerKeySet()) {
            frames.put(layer, 0);
        }
    }

    public void step() {
        for (String key : frames.keySet()) {
            int frame = frames.get(key);
            frames.put(key, (frame + 1) % animation.getLayer(key).getFrameCount());
        }
    }

    public List<Renderable> getRenderables() {
        return animation.getRenderableList(true).get(frames.get("visual"));
    }
}