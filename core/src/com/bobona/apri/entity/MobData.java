package com.bobona.apri.entity;

import com.bobona.apri.entity.tools.MovementController;

public class MobData extends EntityData {

    Class<MovementController> movementController;

    public MobData(Builder builder) {
        super(builder);
        movementController = builder.movementController;
    }

    public static class Builder<T extends Builder<?>> extends EntityData.Builder<T> {

        Class<? extends MovementController> movementController;

        public Builder(String typeId, String texture, float height, float width, float density, float[] collisionHitbox) {
            super(typeId, texture, height, width, density, collisionHitbox);
        }

        public T movementController(Class<? extends MovementController> movementController) {
            this.movementController = movementController;
            return (T) this;
        }

        public EntityData build() {
            super.build();
            return new MobData(this);
        }
    }
}
