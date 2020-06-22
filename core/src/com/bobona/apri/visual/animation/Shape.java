package com.bobona.apri.visual.animation;


import com.bobona.apri.visual.Transform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Shape implements Comparable<Shape> {

    ShapeType type;
    float width, height;
    float x;
    float y;
    float rotation;
    float zIndex;
    Map<String, Transform> referencePoints;
    String texture;

    private Shape() {
        referencePoints = new HashMap<>();
    }

    public Shape(ShapeType type, float width, float height, float x, float y, float rotation, float zIndex) {
        this();
        this.type = type;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.zIndex = zIndex;
    }

    public Shape(GsonEntityAnimation.Shape shape) {
        this(shape.type == null ? ShapeType.RECTANGLE : ShapeType.valueOf(shape.type.toUpperCase()), shape.width, shape.height, shape.x, shape.y, shape.rotation, shape.zIndex);
        if (shape.referencePoints != null) {
            Arrays.stream(shape.referencePoints)
                    .forEach(transform -> referencePoints.put(transform.name, new Transform(transform)));
        }
    }

    public Shape(float width, float height) {
        this(ShapeType.RECTANGLE, width, height, 0, 0, 0, 0);
    }

    public ShapeType getType() {
        return type;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZIndex() {
        return zIndex;
    }

    public Map<String, Transform> getReferencePoints() {
        return referencePoints;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String path) {
        this.texture = path;
    }

    public float getRotation() {
        return rotation;
    }

    public void setType(ShapeType type) {
        this.type = type;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setzIndex(float zIndex) {
        this.zIndex = zIndex;
    }

    @Override
    public int compareTo(Shape o) {
        return Double.compare(zIndex, o.zIndex);
    }

    public enum ShapeType {

        RECTANGLE,
        CIRCLE
    }
}
