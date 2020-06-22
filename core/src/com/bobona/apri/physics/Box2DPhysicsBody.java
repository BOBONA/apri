package com.bobona.apri.physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.ObjectMap;
import com.bobona.apri.entity.Entity;
import com.bobona.apri.utils.ConfigConstructor;
import com.bobona.apri.utils.Vector2;
import com.bobona.apri.visual.Transform;

import java.util.*;

public class Box2DPhysicsBody implements PhysicsBody {

    public com.badlogic.gdx.physics.box2d.Body body;
    Box2DWorld box2DWorld;
    Entity entity;
    float width;
    float height;
    float[] vertices;
    ObjectMap<String, Fixture> sensors;
    Map<String, Impulse> impulses;

    @ConfigConstructor
    public Box2DPhysicsBody(Box2DWorld box2DWorld, Entity entity, float density, float xPos, float yPos, float width, float height, float[] vertices, Map<String, float[]> sensors) {
        this.box2DWorld = box2DWorld;
        this.entity = entity;
        this.width = width;
        this.height = height;
        this.vertices = vertices;
        this.sensors = new ObjectMap<>();
        this.impulses = new HashMap<>();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // set position at bottom right corner instead of center
        bodyDef.position.set(xPos - width / 2, yPos + height / 2);
        body = box2DWorld.world.createBody(bodyDef);
        body.setUserData(this);
        // basic shape
        PolygonShape shape = new PolygonShape();
        float[] calculatedVertices = new float[vertices.length];
        for (int i = 0; i < vertices.length; i += 2) {
            calculatedVertices[i] = vertices[i] * width;
            calculatedVertices[i + 1] = vertices[i + 1] * height;
        }
        shape.set(calculatedVertices);
        body.createFixture(shape, density);
        for (Map.Entry<String, float[]> sensor : sensors.entrySet()) {
            PolygonShape sensorShape = new PolygonShape();
            float[] sensorCalculatedVertices = new float[sensor.getValue().length];
            for (int i = 0; i < sensor.getValue().length; i += 2) {
                sensorCalculatedVertices[i] = sensor.getValue()[i] * width;
                sensorCalculatedVertices[i + 1] = sensor.getValue()[i + 1] * height;
            }
            sensorShape.set(sensorCalculatedVertices);
            FixtureDef sensorFixtureDef = new FixtureDef();
            sensorFixtureDef.shape = sensorShape;
            sensorFixtureDef.density = 0.0f;
            sensorFixtureDef.isSensor = true;
            Fixture sensorFixture = body.createFixture(sensorFixtureDef);
            this.sensors.put(sensor.getKey(), sensorFixture);
        }
        body.setGravityScale(0);
        body.setFixedRotation(true);
        body.setTransform(xPos, yPos, 0);
        shape.dispose();
    }

    static ObjectMap<BodyType, BodyDef.BodyType> bodyTypeMap = new ObjectMap<>();
    static {
        bodyTypeMap.put(BodyType.DYNAMIC, BodyDef.BodyType.DynamicBody);
        bodyTypeMap.put(BodyType.KINEMATIC, BodyDef.BodyType.KinematicBody);
        bodyTypeMap.put(BodyType.STATIC, BodyDef.BodyType.StaticBody);
    }

    @Override
    public void setType(BodyType type) {
        body.setType(bodyTypeMap.get(type));
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(
                body.getPosition().x,
                body.getPosition().y);
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void applyLinearImpulse(Vector2 impulse, Vector2 point, boolean wake) {
        body.applyLinearImpulse(impulse.libgdx(), point.libgdx(), wake);
    }

    @Override
    public void setLinearVelocity(float vX, float fY) {
        body.setLinearVelocity(vX, fY);
    }

    @Override
    public Vector2 getLinearVelocity() {
        return new Vector2(body.getLinearVelocity());
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public List<Collision> getCollisions() {
        return Arrays.asList(
                ((Box2DContactListener) box2DWorld.getContactListener()).collisions
                    .stream()
                        .filter(collision -> collision.entity.equals(entity) || collision.other.equals(entity))
                        .map(collision -> {
                            if (collision.other.equals(entity)) {
                                return new Collision(collision.other, collision.otherSensor, collision.entity, collision.sensor);
                            }
                            return collision;
                        })
                        .toArray(Collision[]::new)
        );
    }

    @Override
    public List<Entity> getCollidedEntities() {
        return Arrays.asList(getCollisions()
                .stream()
                .filter(collision -> collision.otherSensor.equals("this"))
                .map(collision -> collision.other)
                .toArray(Entity[]::new));
    }

    @Override
    public boolean pointIntersects(float x, float y) {
        try {
            return body.getFixtureList().get(0).testPoint(x, y);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean pointIntersects(Vector2 vector2) {
        return pointIntersects(vector2.x, vector2.y);
    }

    @Override
    public float[] getVertices() {
        return vertices;
    }

    @Override
    public float[] getCalculatedVertices() {
        float[] calculatedVertices = new float[vertices.length];
        for (int i = 0; i < calculatedVertices.length; i += 2) {
            calculatedVertices[i] = vertices[i] * getWidth();
            calculatedVertices[i + 1] = vertices[i + 1] * getHeight();
        }
        return calculatedVertices;
    }

    @Override
    public void updateVelocity() {
        Vector2 velocity = new Vector2(0, 0);
        for (String key : impulses.keySet()) {
            Impulse impulse = impulses.get(key);
            Vector2 vec = impulse.getVelocity();
            velocity.add(vec);
            impulse.update();
        }
        // maybe make an option for this
        setLinearVelocity(velocity.x, velocity.y);
    }

    @Override
    public Map<String, Impulse> getImpulses() {
        return impulses;
    }
}
