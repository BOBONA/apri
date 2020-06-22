package com.bobona.apri.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.bobona.apri.Demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FontManager implements Disposable {

    Map<String, Map<Integer, BitmapFont>> fonts;
    SpriteBatch batch;

    public static final String FONT_PATH = "fonts/";

    public FontManager(SpriteBatch batch) {
        fonts = new HashMap<>();
        this.batch = batch;
    }

    public void draw(Renderable renderable) {
        TextData textData = renderable.getTextData();
        Renderable.Align align = renderable.getTransform().alignment;
        double x = renderable.getTransform().xOffset;
        double y = renderable.getTransform().yOffset;
        double size = renderable.getTransform().scaleY;
        double maxWidth = renderable.getTransform().scaleX;
        int fontSize = 72;
        BitmapFont font = fonts.get(textData.getFontName()).get(fontSize);
        if (renderable.useGameCoords) {
            x *= Demo.SCALE;
            y *= Demo.SCALE;
            size *= Demo.SCALE;
            maxWidth *= Demo.SCALE;
        } else {
            x *= Demo.camera.viewportWidth;
            y *= Demo.camera.viewportHeight;
            size *= Demo.camera.viewportHeight;
            maxWidth *= Demo.camera.viewportWidth;
        }
        font.getData().setScale((float) size/fontSize);
        font.setColor(textData.getColor());
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, textData.getText());
        if (maxWidth != 0 && glyphLayout.width > maxWidth) {
            font.getData().setScale((float) (maxWidth * (size / glyphLayout.width) / fontSize));
        }
        glyphLayout.setText(font, textData.getText());
        x += glyphLayout.width * align.getTransformX();
        y += glyphLayout.height * (align.getTransformY() + 1);
        font.draw(batch, textData.getText(), (float) x, (float) y, 0, Align.left, false);
    }
//
//    public BitmapFont getFont(String fontName, int size, boolean worldScale) {
//        if (worldScale) {
//            size *= Demo.SCALE;
//        }
//        Map<Integer, BitmapFont> fontType = fonts.get(fontName);
//        int nearestSize = 0;
//        int nearestHigherSize = 0;
//        for (int fontSize : fontType.keySet()) {
//            if (Math.abs(fontSize - size) <= Math.abs(nearestSize - size)) {
//                nearestSize = fontSize;
//                if (fontSize >= size) {
//                    nearestHigherSize = fontSize;
//                }
//            }
//        }
//        if (nearestHigherSize == 0) {
//            nearestHigherSize = nearestSize;
//        }
//        nearestHigherSize = 72;
//        BitmapFont font = fontType.get(nearestHigherSize);
//        font.getData().setScale((float) size/nearestHigherSize);
//        return font;
//    }

    public void loadFont(String fontName) {
        loadFont(fontName, new int[]{72});
    }

    public void loadFont(String fontName, int[] sizes) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH + fontName + ".ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter config = new FreeTypeFontGenerator.FreeTypeFontParameter();
        config.minFilter = Texture.TextureFilter.Linear;
        config.magFilter = Texture.TextureFilter.Linear;
        fonts.computeIfAbsent(fontName, k -> new HashMap<>());
        for (int size : sizes) {
            config.size = size;
            fonts.get(fontName).put(size, generator.generateFont(config));
        }
        generator.dispose();
    }

    @Override
    public void dispose() {
        for (Map<Integer, BitmapFont> fontType : fonts.values()) {
            for (BitmapFont font : fontType.values()) {
                font.dispose();
            }
        }
    }
}
