package com.bobona.apri.visual.animation;

import com.bobona.animationMaker.exception.InvalidInputException;
import com.bobona.apri.visual.Transform;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Layer {

    private EntityAnimation entityAnimation;
    private Map<String, Shape> shapes;
    private List<Frame> frames;

    private Layer(EntityAnimation animation) {
        this.entityAnimation = animation;
        shapes = new HashMap<>();
        frames = new ArrayList<>();
    }

    public Layer(EntityAnimation entityAnimation, GsonEntityAnimation.Layer layer) {
        this(entityAnimation);
        if (layer.shapes != null) {
            Arrays.stream(layer.shapes)
                    .forEach(shape -> shapes.put(shape.name, new Shape(shape)));
        }
        if (layer.frames != null) {
            Arrays.stream(layer.frames)
                    .forEach(frame -> {
                        frames.add(new Frame(frame));
                    });
        }
    }

    public Layer(EntityAnimation entityAnimation, int length) {
        this(entityAnimation);
        IntStream.range(1, length).forEach(i -> frames.add(new Frame()));
    }

    public void updateShapeOrder() {
        updateShapeOrder(getShapeOrder());
    }

    public void updateShapeOrder(List<String> shapeOrder) {
        for (int i = 0; i < shapeOrder.size(); i++) {
            shapes.get(shapeOrder.get(i)).zIndex = i;
        }
    }

    public List<String> getShapeOrder() {
        List<Shape> shapes = new LinkedList<>(Arrays.asList(this.shapes.values().toArray(new Shape[0])));
        return shapes.stream()
                .sorted()
                .map(this::getKeyByShape)
                .collect(Collectors.toList());
    }

    public String getKeyByShape(Shape shape) {
        for (Map.Entry<String, Shape> entry : shapes.entrySet()) {
            if (shape.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    // nameSafe determines whether a suffix should be added to the end of the key to avoid duplicate key names
    public void newShape(String name, float width, float height, boolean nameSafe) {
        if (shapes.containsKey(name)) {
            int suffix = 1;
            while (shapes.containsKey(name + suffix)) {
                suffix++;
            }
            if (nameSafe) {
                newShape(name + suffix, width, height, true);
            } else {
                throw new InvalidInputException();
            }
        } else {
            updateShapeOrder();
            shapes.put(name, new Shape(Shape.ShapeType.RECTANGLE, width, height, 0, 0, 0, shapes.size()));
            for (Frame frame : frames) {
                frame.setTransform(name, new Transform());
            }
        }
    }

    public int getFrameCount() {
        int count = 0;
        for (Frame frame : frames) {
            count += frame.duration;
        }
        return count;
    }

    public Frame getAnimationFrame(int animationFrame) {
        int count = 0;
        for (Frame frame : frames) {
            if (count <= animationFrame && animationFrame <= (count + frame.duration - 1)) {
                return frame;
            }
            count += frame.duration;
        }
        return frames.get(animationFrame);
    }

    public Frame getFrame(int n) {
        return frames.get(n);
    }

    public Shape getShape(String key) {
        return shapes.get(key);
    }

    public void removeShape(String key) {
        shapes.remove(key);
        updateShapeOrder();
    }

    public void changeShapeName(String key, String old) {
        Shape temp = getShape(old);
        shapes.remove(old);
        shapes.put(key, temp);
        for (Frame frame : frames) {
            frame.changeTransformName(key, old);
        }
        updateShapeOrder();
    }

    public Set<String> getShapeKeySet() {
        return shapes.keySet();
    }
}
