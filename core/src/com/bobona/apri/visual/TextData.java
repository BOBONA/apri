package com.bobona.apri.visual;

import com.badlogic.gdx.graphics.Color;

public class TextData {

    private String text;
    private String fontName;
    private Color color;

    public TextData(String text, String fontName, Color color) {
        this.text = text;
        this.fontName = fontName;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public String getFontName() {
        return fontName;
    }

    public Color getColor() {
        return color;
    }
}
