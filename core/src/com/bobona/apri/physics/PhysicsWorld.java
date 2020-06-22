package com.bobona.apri.physics;

import com.bobona.apri.utils.Vector2;

import java.util.List;
import java.util.Map;

public interface PhysicsWorld {

    void setGravity(float x, float y);
    List<PhysicsBody> getBodies();
    void destroyBody(PhysicsBody body);
    void step(float time, int velocityIterations, int positionIterations);
    void dispose();
    void setContactListener(ContactListener contactListener);
    ContactListener getContactListener();
    List<Collision> getCollisions();
    List<Collision> raycast(float fromX, float fromY, float toX, float toY);
}
