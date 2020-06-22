package com.bobona.apri.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bobona.apri.utils.ConfigConstructor;

import java.util.ArrayList;
import java.util.List;

public class Box2DContactListener extends ContactListener {

    public List<Collision> collisions;

    @ConfigConstructor
    public Box2DContactListener() {
        collisions = new ArrayList<>();
    }

    @Override
    public void beginContact(Contact contact) {
        Box2DPhysicsBody body = (Box2DPhysicsBody) contact.getFixtureA().getBody().getUserData();
        // getPath the sensor id string from the body (or 'this' if it isn't a sensor)
        String sensor = contact.getFixtureA().isSensor() ? body.sensors.findKey(contact.getFixtureA(), false) : "this";
        Box2DPhysicsBody other = (Box2DPhysicsBody) contact.getFixtureB().getBody().getUserData();
        String otherSensor = contact.getFixtureB().isSensor() ? other.sensors.findKey(contact.getFixtureB(), false) : "this";
        Collision collision = new Collision(body.entity, sensor, other.entity, otherSensor);
        body.entity.contactBegin(collision);
        other.entity.contactBegin(new Collision(other.entity, otherSensor, body.entity, sensor));
        collisions.add(collision);
    }

    @Override
    public void endContact(Contact contact) {
        Box2DPhysicsBody body = (Box2DPhysicsBody) contact.getFixtureA().getBody().getUserData();
        // getPath the sensor id string from the body (or 'this' if it isn't a sensor)
        String sensor = contact.getFixtureA().isSensor() ? body.sensors.findKey(contact.getFixtureA(), false) : "this";
        Box2DPhysicsBody other = (Box2DPhysicsBody) contact.getFixtureB().getBody().getUserData();
        String otherSensor = contact.getFixtureB().isSensor() ? other.sensors.findKey(contact.getFixtureB(), false) : "this";
        Collision collision = new Collision(body.entity, sensor, other.entity, otherSensor);
        body.entity.contactEnd(collision);
        other.entity.contactEnd(new Collision(other.entity, otherSensor, body.entity, sensor));
        collisions.remove(collision);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
