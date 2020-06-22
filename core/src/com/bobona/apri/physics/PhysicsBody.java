package com.bobona.apri.physics;

import com.bobona.apri.entity.Entity;
import com.bobona.apri.utils.Vector2;
import com.bobona.apri.visual.Transform;

import java.util.List;
import java.util.Map;

public interface PhysicsBody {

    void setType(BodyType type);
    Vector2 getPosition();
    float getWidth();
    float getHeight();
    float[] getVertices();
    void applyLinearImpulse(Vector2 impulse, Vector2 point, boolean wake);
    void setLinearVelocity(float vX, float vY);
    Vector2 getLinearVelocity();
    Entity getEntity();
    List<Collision> getCollisions();
    List<Entity> getCollidedEntities();
    boolean pointIntersects(float x, float y);
    boolean pointIntersects(Vector2 vector2);
    float[] getCalculatedVertices();
    void updateVelocity(); // should update the impulses and set the velocity accordingly
    Map<String, Impulse> getImpulses();

    enum BodyType {
        STATIC,
        KINEMATIC,
        DYNAMIC
    }
}
