package com.bobona.apri;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.bobona.apri.input.Keyboard;
import com.bobona.apri.physics.Box2DWorld;
import com.bobona.apri.visual.FontManager;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.TextData;
import com.bobona.apri.visual.Transform;
import com.bobona.apri.utils.OutlineRenderer;
import com.bobona.apri.utils.WorldSchematic;
import com.google.gson.Gson;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Demo extends ApplicationAdapter {

    Box2DDebugRenderer debugRenderer;
    OutlineRenderer outlineRenderer;
    boolean useDebugRenderer;
    public static OrthographicCamera camera;
    Viewport viewport;

    AssetManager assets;
    TextureAtlas atlas;
    SpriteBatch spriteBatch;
    FontManager fontManager;

    Gson gson;
    ApriWorld apriWorld;

    public static final float SCALE = 30f;
    public static final String TEXTURE_DIRECTORY = "textures/";
    public static final String DEFAULT_TEXTURE = "notfound.png";

	@Override
	public void create() {
	    Box2D.init();

	    debugRenderer = new Box2DDebugRenderer();
	    outlineRenderer = new OutlineRenderer(true, false, false, true, false, false);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 0, 0);
        viewport = new ExtendViewport(800, 400, camera);

        assets = new AssetManager();
        atlas = new TextureAtlas(Gdx.files.internal("packed_textures/atlas.atlas"));

        spriteBatch = new SpriteBatch();
        fontManager = new FontManager(spriteBatch);
        fontManager.loadFont("comfortaa");
        System.out.println("Fonts loaded.");

        gson = new Gson();
        try {
            String json = readFile("test_schematic.json");
            WorldSchematic schematic = gson.fromJson(json, WorldSchematic.class);
            apriWorld = new ApriWorld();
            apriWorld.loadFromSchematic(schematic);
            String nextTexture = apriWorld.getNextTexture();
            while (nextTexture != null) {
                assets.load(TEXTURE_DIRECTORY + nextTexture, Texture.class);
                nextTexture = apriWorld.getNextTexture();
            }
            assets.load(TEXTURE_DIRECTORY + DEFAULT_TEXTURE, Texture.class);
        } catch (IOException io) {
            System.out.println("What a \nTerrible \nFailure!");
            io.printStackTrace();
        }
        System.out.println("Textures loaded");
	}

	@Override
	public void render() {
        if (!assets.update()) return;
        Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        apriWorld.update();
        camera.update();

		if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
		    useDebugRenderer = !useDebugRenderer;
        }

		if (!useDebugRenderer) {
            spriteBatch.setProjectionMatrix(camera.combined);
            spriteBatch.begin();
            for (Renderable renderable : apriWorld.getBodyRenderables()) {
                Transform transform = renderable.getTransform();
                if (renderable.isVisible()) {
                    drawTexture(
                            renderable.getTexturePath(),
                            transform.xOffset,
                            transform.yOffset,
                            transform.scaleX,
                            transform.scaleY,
                            renderable.useGameCoords()
                    );
                }
                if (renderable.getTextData() != null) {
                    fontManager.draw(renderable);
                }
            }
            for (Renderable renderable : apriWorld.getUiRenderables()) {
                Transform transform = renderable.getTransform();
                if (renderable.isVisible()) {
                    drawTexture(
                            renderable.getTexturePath(),
                            transform.xOffset,
                            transform.yOffset,
                            transform.scaleX,
                            transform.scaleY,
                            renderable.useGameCoords()
                    );
                }
                if (renderable.getTextData() != null) {
                    fontManager.draw(renderable);
                }
            }
            fontManager.draw(new Renderable((int) Keyboard.o.getMouseX() + ", " + (int) Keyboard.o.getMouseY(), "comfortaa", Color.BLACK, Renderable.Align.BOTTOM_LEFT, -0.5f, -0.5f, 0.04f, 1));
            spriteBatch.end();
        } else {
		    debugRenderer.render(((Box2DWorld) apriWorld.physicsWorld).getWorld(), camera.combined);
        }
	}

	public void drawTexture(String texturePath, float xOffset, float yOffset, float width, float height, boolean useGameCoords) {
	    float scaleX = useGameCoords ? SCALE : camera.viewportWidth;
	    float scaleY = useGameCoords ? SCALE : camera.viewportHeight;
        // TODO use atlas find region
        Texture texture;
        try {
            texture = assets.get(TEXTURE_DIRECTORY + texturePath, Texture.class);
        } catch (GdxRuntimeException e) {
            texture = assets.get(TEXTURE_DIRECTORY + DEFAULT_TEXTURE, Texture.class);
        }
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteBatch.draw(
	            texture,
                (float) xOffset * scaleX,
                (float) yOffset * scaleY,
                (float) width * scaleX,
                (float) height * scaleY
        );
    }

	@Override
	public void dispose() {
		spriteBatch.dispose();
		assets.dispose();
		apriWorld.dispose();
        fontManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }
}
