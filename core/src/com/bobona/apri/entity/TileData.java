package com.bobona.apri.entity;

import com.bobona.apri.entity.tools.Damage;

public class TileData extends EntityData {

    int minBurnLvToCatchOnFire;

    public TileData(Builder builder) {
        super(builder);
        minBurnLvToCatchOnFire = builder.minBurnLvToCatchOnFire;
    }

    public static class Builder<T extends Builder<?>> extends EntityData.Builder<T> {

        int minBurnLvToCatchOnFire = -1;

        public Builder(String typeId, String texture, float height, float width, float[] collisionHitbox) {
            super(typeId, texture, height, width, 0.0f, collisionHitbox);
        }

        public T minBurnLvToCatchOnFire(int burnLevel) {
            minBurnLvToCatchOnFire = burnLevel;
            return (T) this;
        }

        public EntityData build() {
            super.build();
            return new TileData(this);
        }
    }
}
