package com.bobona.apri;

import com.badlogic.gdx.Gdx;
import com.bobona.apri.entity.*;
import com.bobona.apri.input.InputController;
import com.bobona.apri.input.Keyboard;
import com.bobona.apri.physics.PhysicsWorld;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.Transform;
import com.bobona.apri.utils.Config;
import com.bobona.apri.utils.Vector2;
import com.bobona.apri.utils.WorldSchematic;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApriWorld {

    public static Gson gson = new Gson();
    private static List<String> unloadedTextures = new ArrayList<>();
    private static Set<String> allTextures = new HashSet<>();

    public PhysicsWorld physicsWorld;
    public List<Entity> entities;
    public Entity selectedEntity;
    public Mob player;
    public Transform camera;
    public InputController inputController;
    public List<Entity> deletionQueue;
    public List<Entity> additionQueue;
    public long ellapsedFrames;

    public static final Float OUTLINE_SIZE = 0.1f;
    public static final String TEXTURE_DIRECTORY = "textures/";
    public static final String OUTLINE_TEXTURE = "black.png";
    public static final float GRAVITY = 23f;

    public ApriWorld() {
        physicsWorld = Config.instantiate(Config.PHYSICS_WORLD_CLASS, this);
        inputController = Config.instantiate(Config.INPUT_CONTROLLER_CLASS);
        physicsWorld.setContactListener(Config.instantiate(Config.CONTACT_LISTENER_CLASS));
        entities = new ArrayList<>();
        camera = new Transform();
        deletionQueue = new ArrayList<>();
        additionQueue = new ArrayList<>();
        ellapsedFrames = 0;
        addTexture(OUTLINE_TEXTURE);
        Entities.init();
    }

    public void loadFromSchematic(WorldSchematic schematic) {
        schematic.loadIndexWorld();
        entities.clear();
        for (WorldSchematic.IndexEntity indexEntity : schematic.indexWorld) {
            entities.add(new Tile(
                    this,
                    (TileData) Entities.apriEntities.get(indexEntity.index),
                    indexEntity.x,
                    indexEntity.y));
        }
        player = new Mob(
                this,
                (MobData) Entities.getEntityDataByTypeId("player_temporary"),
                -2f,
                10f
        );
        entities.add(player);
    }

    public void update() {
        ellapsedFrames++;
        // System.out.println(1 / Gdx.graphics.getDeltaTime());
        // this might not be the ideal way of doing it
        physicsWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);
        inputController.update();
        for (Entity entity : entities) {
            entity.update();
            Vector2 mouse = new Vector2(Keyboard.o.getMouseX() - camera.xOffset*Demo.SCALE, Keyboard.o.getMouseY() - camera.yOffset*Demo.SCALE).multiply(1 / Demo.SCALE);
            if (entity.body.pointIntersects(mouse)) {
//                Collision closestCollision = physicsWorld.raycast(
//                        player.body.getPosition().x,
//                        player.body.getPosition().y,
//                        mouse.x,
//                        mouse.y)
//                .get(0);
//                if (closestCollision.collisionPoint.libgdx().dst(player.body.getPosition().libgdx()) <= 1) {
//                    selectedEntity = closestCollision.entity;
//                } else {
//                    selectedEntity = null;
//                }
                selectedEntity = null;
                for (int i = 0; i < player.body.getVertices().length; i += 2) {
                    Vector2 vertex = new Vector2(player.body.getCalculatedVertices()[i], player.body.getCalculatedVertices()[i + 1]);
                    if (mouse.libgdx().dst(player.body.getPosition().libgdx().add(vertex.libgdx())) <= 4) {
                        selectedEntity = entity;
                    }
                }
            }
        }
        camera.xOffset = -player.body.getPosition().x;
//        camera.yOffset = -player.body.getPosition().y;
        clearDeletionQueue();
        clearAdditionQueue();
    }

    public List<Renderable> getBodyRenderables() {
        List<Renderable> renderables = new ArrayList<>();
        for (Entity entity : entities) {
            if (!entity.equals(selectedEntity)) {
                renderables.addAll(entity.getBodyRenderables());
            }
        }
        if (selectedEntity != null) {
            renderables.addAll(getOutlineRenderables(selectedEntity));
            renderables.addAll(selectedEntity.getBodyRenderables());
        }
        Renderable.transformRenderables(renderables, camera.xOffset, camera.yOffset);
        return renderables;
    }

    public List<Renderable> getUiRenderables() {
        List<Renderable> renderables = new ArrayList<>();
        for (Entity entity : entities) {
            renderables.addAll(entity.getUiRenderables());
        }
        return renderables;
    }

    private List<Renderable> getOutlineRenderables(Entity entity) {
        List<Renderable> renderables = new ArrayList<>();
        for (Renderable renderable : entity.getBodyRenderables()) {
            renderable.setTexturePath(OUTLINE_TEXTURE);
            Transform transform = renderable.getTransform();
            transform.scaleX += OUTLINE_SIZE;
            transform.scaleY += OUTLINE_SIZE;
            transform.xOffset -= OUTLINE_SIZE / 2;
            transform.yOffset -= OUTLINE_SIZE / 2;
            renderables.add(renderable);
        }
        return renderables;
    }

    public void clearDeletionQueue() {
        deletionQueue.forEach(entities::remove);
        deletionQueue.clear();
    }

    public void clearAdditionQueue() {
        entities.addAll(additionQueue);
        additionQueue.clear();
    }

    public String getNextTexture() {
        if (unloadedTextures.size() == 0) {
            return null;
        } else {
            return unloadedTextures.remove(unloadedTextures.size() - 1);
        }
    }

    public static void addTexture(String path) {
        if (!allTextures.contains(path)) {
            unloadedTextures.add(path);
            allTextures.add(path);
        }
    }

    public void dispose() {
        physicsWorld.dispose();
    }
}
