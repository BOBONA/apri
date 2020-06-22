package com.bobona.apri.utils;

public class Vector3 {

    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(com.badlogic.gdx.math.Vector3 vector3) {
        this.x = vector3.x;
        this.y = vector3.y;
        this.z = vector3.z;
    }

    public com.badlogic.gdx.math.Vector3 libgdx() {
        return new com.badlogic.gdx.math.Vector3(this.x, this.y, this.z);
    }
}
