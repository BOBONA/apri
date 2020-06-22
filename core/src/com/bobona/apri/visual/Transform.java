package com.bobona.apri.visual;

import com.bobona.apri.visual.animation.GsonEntityAnimation;

public class Transform {

    // Transform is less of a class for transformations and more of a good place to store a bunch of data values without creating new classes
    public boolean isVisible;  // not really related to transformation, this is just a good place to store it
    public Renderable.Align alignment; // doesn't really belong here, but I prefer to just reuse the Transform class whenever I need to store position data for anything
    public float zIndex;
    public float xOffset;
    public float yOffset;
    public float scaleX;
    public float scaleY;
    public float rotation;
    public boolean rotOrigin;
    public float rotX;
    public float rotY;

    public Transform() {
        this(true, 0, 0f, 0f, 1f, 1f, 0f, true, 0f, 0f);
    }

    public Transform(float xOffset, float yOffset, float scaleX, float scaleY) {
        this();
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public Transform(float xOffset, float yOffset, float scaleX, float scaleY, boolean isVisible, Renderable.Align alignment) {
        this(xOffset, yOffset, scaleX, scaleY);
        this.isVisible = isVisible;
        this.alignment = alignment;
    }

    public Transform(boolean isVisible, float zIndex, float xOffset, float yOffset, float scaleX, float scaleY, float rotation, boolean rotOrigin, float rotX, float rotY) {
        this.isVisible = isVisible;
        this.zIndex = zIndex;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.rotOrigin = rotOrigin;
        this.rotX = rotX;
        this.rotY = rotY;
    }

    public Transform(GsonEntityAnimation.Transform t) {
        this.zIndex = t.zIndex;
        this.xOffset = t.x;
        this.yOffset = t.y;
        this.scaleX = t.scaleX;
        this.scaleY = t.scaleY;
        this.rotation = t.rot;
        this.rotOrigin = t.origin;
        this.rotX = t.rotX;
        this.rotY = t.rotY;
        this.isVisible = !t.hide;
    }

    public Transform(Transform old) {
        this.isVisible = old.isVisible;
        this.zIndex = old.zIndex;
        this.xOffset = old.xOffset;
        this.yOffset = old.yOffset;
        this.scaleX = old.scaleX;
        this.scaleY = old.scaleY;
        this.rotation = old.rotation;
        this.rotOrigin = old.rotOrigin;
        this.rotX = old.rotX;
        this.rotY = old.rotY;
    }

    public void set(Transform old) {
        this.isVisible = old.isVisible;
        this.alignment = old.alignment;
        this.zIndex = old.zIndex;
        this.xOffset = old.xOffset;
        this.yOffset = old.yOffset;
        this.scaleX = old.scaleX;
        this.scaleY = old.scaleY;
        this.rotation = old.rotation;
        this.rotOrigin = old.rotOrigin;
        this.rotX = old.rotX;
        this.rotY = old.rotY;
    }
}
