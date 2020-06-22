package com.bobona.apri.utils;

public class Vector2 {

    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(com.badlogic.gdx.math.Vector2 vector2) {
        this.x = vector2.x;
        this.y = vector2.y;
    }

    public Vector2(Vector2 v) {
        this(v.x, v.y);
    }

    public com.badlogic.gdx.math.Vector2 libgdx() {
        return new com.badlogic.gdx.math.Vector2(this.x, this.y);
    }

    public Vector2 multiply(float f) {
        x *= f;
        y *= f;
        return this;
    }

    public Vector2 add(Vector2 other) {
        x += other.x;
        y += other.y;
        return this;
    }
}
