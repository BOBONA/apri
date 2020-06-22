package com.bobona.apri.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.bobona.apri.ApriWorld;
import com.bobona.apri.utils.ConfigConstructor;

import java.util.*;

public class Box2DWorld implements PhysicsWorld {

    ApriWorld apriWorld;
    World world;
    ContactListener contactListener;
    List<PhysicsBody> bodies;

    @ConfigConstructor
    public Box2DWorld(ApriWorld apriWorld) {
        this.apriWorld = apriWorld;
        world = new World(new Vector2(0, -98), true);
        bodies = new ArrayList<>();
    }

    @Override
    public void step(float time, int velocityIterations, int positionIterations) {
        world.step(time, velocityIterations, positionIterations);
        Array<com.badlogic.gdx.physics.box2d.Body> bodies = new Array<>();
        world.getBodies(bodies);
        // todo why did I do this?
        this.bodies.clear();
        for (com.badlogic.gdx.physics.box2d.Body body : bodies) {
            PhysicsBody physicsBody = (PhysicsBody) body.getUserData();
            physicsBody.updateVelocity();
            this.bodies.add(physicsBody);
        }
    }

    @Override
    public void setGravity(float x, float y) {
        world.setGravity(new Vector2(x, y));
    }

    @Override
    public List<PhysicsBody> getBodies() {
        return bodies;
    }

    @Override
    public void destroyBody(PhysicsBody body) {
        world.destroyBody(((Box2DPhysicsBody) body).body);
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    @Override
    public void setContactListener(ContactListener contactListener) {
        world.setContactListener(contactListener);
        this.contactListener = contactListener;
    }

    @Override
    public ContactListener getContactListener() {
        return contactListener;
    }

    @Override
    public List<Collision> getCollisions() {
        Collision[] collisions = Arrays.stream(world.getContactList().toArray())
                .map(contact -> {
                    Box2DPhysicsBody body = (Box2DPhysicsBody) contact.getFixtureA().getBody().getUserData();
                    // getPath the sensor id string from the body (or 'this' if it isn't a sensor)
                    String sensor = contact.getFixtureA().isSensor() ? body.sensors.findKey(contact.getFixtureA(), false) : "this";
                    Box2DPhysicsBody other = (Box2DPhysicsBody) contact.getFixtureB().getBody().getUserData();
                    String otherSensor = contact.getFixtureB().isSensor() ? other.sensors.findKey(contact.getFixtureB(), false) : "this";
                    return new Collision(body.entity, sensor, other.entity, otherSensor);
                })
                .toArray(Collision[]::new);
        return Arrays.asList(collisions);
    }

    @Override
    public List<Collision> raycast(float fromX, float fromY, float toX, float toY) {
        List<Collision> collisions = new ArrayList<>();
        RayCastCallback rayCastCallback = (fixture, point, normal, fraction) -> {
            collisions.add(new Collision(
                    ((PhysicsBody) fixture.getBody().getUserData()).getEntity(),
                    "this",
                    null,
                    null,
                    new com.bobona.apri.utils.Vector2(normal.x, normal.y)));
            return 1;
        };
        world.rayCast(rayCastCallback, fromX, fromY, toX, toY);
        return collisions;
    }
}
