package com.bobona.apri;

import com.badlogic.gdx.utils.Array;
import com.bobona.apri.entity.*;
import com.bobona.apri.entity.tools.*;
import com.bobona.apri.utils.Vector2;
import com.bobona.apri.visual.Renderable;
import com.bobona.apri.visual.Transform;

import java.util.Arrays;

public class Entities {

    public static Array<EntityData> apriEntities = new Array<>();

    public static void init() {
        EntityData[] entities = new EntityData[]{
                // new TileData.Builder<TileData.Builder<?>>
                new TileData.Builder<>("lava_stone", "dirt.png", 1f, 1f, Hitboxes.RECTANGLE)
//                        .onUpdateHandler((entity) ->
//                                entity.body.getCollidedEntities()
//                                    .forEach(other -> other.addStatus(StatusEffect.Type.BURN, 2, 1, false)))
                        .damageMultiplier(Damage.PICKAXE, 6.0)
                        .damageMultiplier(Damage.HAMMER, 4.0)
                        .maxHitpoints(120)
                        .build(),
                new MobData.Builder<>("player_temporary", "lava_stone.png", 1.5f, 1f, 10f, Hitboxes.RECTANGLE_CLIPPED)
                        .setInitializeFunction((entity) -> {
                            entity.getUiRenderable("overlay").setTransform(new Transform(-0.5f, -0.5f, 1, 1, true, Renderable.Align.TOP_LEFT));
                            entity.getUiRenderable("overlay").setUseGameCoords(false);
                        })
                        .onUpdateHandler((entity) -> {
                            if (entity.body.getPosition().y < -20f) {
                                entity.world.additionQueue.add(entity.exportSaveData().createEntity(new Vector2( -2f, 10f)));
                            }
                            entity.getProperty("overlay.health_bar_percent", Float.class).set((float) entity.hitpoints / entity.entityData.maxHitpoints);
                            entity.getProperty("overlay.health", String.class).set(String.valueOf(entity.hitpoints));
                        })
                        .sensor("left", Hitboxes.RECTANGE_CLIPPED_SIDE_LEFT)
                        .sensor("right", Hitboxes.RECTANGE_CLIPPED_SIDE_RIGHT)
                        .mixin(Mixins.canJump, (Object) Hitboxes.RECTANGE_CLIPPED_BOTTOM)
                        .maxHitpoints(1000)
                        .movementController(PlayerMovementController.class)
                        .addAnimation("idle", "test_animation.json")
                        .addTextureToAnimation("head", "dirt.png")
                        .addTextureToAnimation("body", "lava_stone.png")
                        .addUi("overlay", "ui/overlay.xml")
                        .build()
        };
        Arrays.stream(entities)
                .forEach(apriEntities::add);
    }

    public static EntityData getEntityDataByTypeId(String id) {
        return Arrays.stream(apriEntities.toArray())
                .filter(entityData -> entityData.typeId.equals(id))
                .findFirst()
                .get();
    }
}
