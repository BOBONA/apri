package com.bobona.apri.visual.animation;


import com.bobona.apri.visual.Transform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Frame {

    int duration;
    boolean interpolate;
    private Map<String, Transform> shapeTransforms;

    public Frame() {
        shapeTransforms = new HashMap<>();
    }

    public Frame(GsonEntityAnimation.Frame frame) {
        this();
        duration = frame.duration;
        if (frame.transforms != null) {
            Arrays.stream(frame.transforms)
                    .forEach(transform -> shapeTransforms.put(transform.name, new Transform(transform)));
        }
    }

    public void changeTransformName(String key, String old) {
        Transform temp = shapeTransforms.get(old);
        shapeTransforms.remove(old);
        shapeTransforms.put(key, temp);
    }

    public Transform getTransform(String key) {
        return shapeTransforms.get(key);
    }

    public void setTransform(String key, Transform transform) {
        shapeTransforms.put(key, transform);
    }
}
