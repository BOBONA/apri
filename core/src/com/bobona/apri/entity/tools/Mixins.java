package com.bobona.apri.entity.tools;

import com.bobona.apri.entity.Entity;
import com.bobona.apri.entity.Mob;
import com.bobona.apri.entity.MobData;
import com.bobona.apri.entity.Tile;
import com.bobona.apri.physics.Collision;

import static com.bobona.apri.entity.Entity.Properties.CAN_JUMP;

public class Mixins {

    public static final Mixin canJump = new Mixin<Mob, MobData.Builder>(new Class<?>[]{float[].class}) {

        @Override
        public void buildingPhase(MobData.Builder builder) {
            ((MobData.Builder<?>) builder)
                    .sensor("bottom", Hitboxes.RECTANGE_CLIPPED_BOTTOM);
        }

        @Override
        public void init(Mob entity, Object... args) {
            entity.getProperty(CAN_JUMP, Boolean.class).set(false);
        }

        @Override
        public void update(Mob entity) {
            boolean canJump = !entity.getSensorCollisions("bottom", Tile.class).isEmpty();
            entity.getProperty(CAN_JUMP, Boolean.class).set(canJump);
        }

        @Override
        public String getName() {
            return "canJump";
        }
    };
}
