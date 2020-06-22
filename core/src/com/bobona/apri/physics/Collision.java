package com.bobona.apri.physics;

import com.bobona.apri.entity.Entity;
import com.bobona.apri.utils.Vector2;

import java.util.Objects;

public class Collision {

    public Entity entity;
    // these sensor id strings should be 'this' if the collision is with a normal fixture
    public String sensor;
    public Entity other;
    public String otherSensor;
    public Vector2 collisionPoint;

    public Collision(Entity entity, String sensor, Entity other, String otherSensor) {
        this.entity = entity;
        this.sensor = sensor;
        this.other = other;
        this.otherSensor = otherSensor;
        collisionPoint = entity.body.getPosition();
    }

    public Collision(Entity entity, String sensor, Entity other, String otherSensor, Vector2 collisionPoint) {
        this.entity = entity;
        this.sensor = sensor;
        this.other = other;
        this.otherSensor = otherSensor;
        this.collisionPoint = collisionPoint;
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, sensor, other, otherSensor);
    }
}
