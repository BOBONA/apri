package com.bobona.apri.entity.tools;

import com.bobona.apri.entity.Entity;
import com.bobona.apri.entity.EntityData;
import com.bobona.apri.physics.Collision;

public abstract class Mixin<E extends Entity, B extends EntityData.Builder> {

    public Class<?>[] requiredArgTypes;

    public Mixin(Class<?>[] argTypes) {
        this.requiredArgTypes = argTypes;
    }

    public void buildingPhase(B builder) {}
    public void init(E entity, Object... args) {}
    public void update(E entity) {}
    public void contactBegin(E entity, Collision collision) {}
    public void contactEnd(E entity, Collision endCollision) {}
    public String getName() {
        return null;
    }
}
