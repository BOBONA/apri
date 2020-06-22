package com.bobona.apri.visual;

import com.badlogic.gdx.graphics.Color;

import java.util.List;

public class Renderable implements Comparable<Renderable> {

    Transform transform;
    String texturePath;
    TextData textData;
    boolean useGameCoords;
    boolean hide;

    public Renderable(Transform transform, String texturePath) {
        this.transform = transform;
        this.texturePath = texturePath;
        useGameCoords = true;
        hide = !transform.isVisible;
    }

    public Renderable(String text, String fontName, Color color, Align alignment, float posX, float posY, float height, float maxWidth) {
        transform = new Transform();
        setTextData(text, fontName, color);
        setTransform(new Transform(posX, posY, maxWidth, height, false, alignment));
        hide = true;
    }

    public Renderable(Renderable old) {
        this.transform = new Transform(old.transform);
        this.texturePath = old.texturePath;
        this.useGameCoords = old.useGameCoords;
        this.hide = old.hide;
    }

    public Transform getTransform() {
        return transform;
    }

    // negative zIndexes are higher than positive no matter what
    public static List<Renderable> orderRenderables(List<Renderable> renderables) {
        renderables.sort(null);
        return renderables;
    }

    public boolean useGameCoords() {
        return useGameCoords;
    }

    public void setUseGameCoords(boolean useGameCoords) {
        this.useGameCoords = useGameCoords;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public boolean isVisible() {
        return !hide;
    }

    public void setTransform(Transform transform) {
        this.transform.set(transform);
        hide = !transform.isVisible;
    }

    public void setVisible(boolean visible) {
        hide = !visible;
    }

    public static void transformRenderables(List<Renderable> renderables, float x, float y) {
        for (Renderable renderable : renderables) {
            renderable.getTransform().xOffset += x;
            renderable.getTransform().yOffset += y;
        }
    }

    public void setTextData(String text, String fontName, Color color) {
        setTextData(new TextData(text, fontName, color));
    }

    public void setTextData(TextData data) {
        this.textData = data;
    }

    public TextData getTextData() {
        return textData;
    }

    @Override
    public int compareTo(Renderable o) {
        if (this.transform.zIndex < 0 && o.transform.zIndex >= 0) {
            return 1;
        } else if (!(this.transform.zIndex >= 0 && o.transform.zIndex <= 0)) {
            float zIndexCompare = Double.compare(this.transform.zIndex, o.transform.zIndex);
            if (zIndexCompare == 0) {
                return Integer.compare(this.hashCode(), o.hashCode());
            } else {
                return (int) zIndexCompare;
            }
        } else {
            return -1;
        }
    }

    public void setTexturePath(String path) {
        texturePath = path;
    }

    public static Align getAlign(String alignment) {
        try {
            return Align.valueOf(alignment.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Align.BOTTOM_LEFT;
        }
    }

    public enum Align {

        CENTER(-0.5f, -0.5f),
        TOP(-0.5f, -1),
        BOTTOM(-0.5f, 1),
        LEFT(0, -0.5f),
        RIGHT(-1, -0.5f),
        TOP_LEFT(0, -1),
        TOP_RIGHT(-1, -1),
        BOTTOM_LEFT(0, 0),
        BOTTOM_RIGHT(-1, 0);

        private float transformX;
        private float transformY;

        Align(float transformX, float transformY) {
            this.transformX = transformX;
            this.transformY = transformY;
        }

        public float getTransformX() {
            return transformX;
        }

        public float getTransformY() {
            return transformY;
        }
    }
}
