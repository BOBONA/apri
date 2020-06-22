package com.bobona.apri.physics;

import com.bobona.apri.utils.Vector2;
import com.bobona.apri.utils.Vector3;

import java.util.function.Function;

public class Impulse {

    int currentFrame;
    Function<Integer, Vector2> velocity;

    public Impulse() {
        this(frame -> new Vector2(0, 0));
    }

    public Impulse(Function<Integer, Vector2> function) {
        velocity = function;
        deactivate();
    }

    public void update() {
        if (currentFrame > -1) {
            currentFrame++;
        }
    }

    public void setCurrentFrame(int frame) {
        currentFrame = frame;
    }

    public void deactivate() {
        currentFrame = -1;
    }

    public void activate() {
        currentFrame = 0;
    }

    public void setVelocityFunction(Function<Integer, Vector2> function) {
        setVelocityFunction(function, currentFrame);
    }

    public void setVelocityFunction(Function<Integer, Vector2> function, int atFrame) {
        velocity = function;
        currentFrame = atFrame;
    }

    public Vector2 getVelocity() {
        if (currentFrame > -1) {
            return velocity.apply(currentFrame);
        } else {
            return new Vector2(0, 0);
        }
    }
}
