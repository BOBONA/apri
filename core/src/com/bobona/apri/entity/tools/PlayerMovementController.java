package com.bobona.apri.entity.tools;

import com.bobona.apri.ApriWorld;
import com.bobona.apri.entity.Entity;
import com.bobona.apri.input.Keyboard;
import com.bobona.apri.physics.Impulse;
import com.bobona.apri.utils.ConfigConstructor;
import com.bobona.apri.utils.Vector2;
import com.bobona.apri.utils.Vector3;
import com.bobona.apri.visual.Transform;

import java.util.Map;

import static com.bobona.apri.entity.Entity.Properties.CAN_JUMP;
import static com.bobona.apri.input.Keyboard.o;

public class PlayerMovementController extends MovementController {

    EntityProperty<Boolean> canJump;
    Map<String, Impulse> impulses;
    Impulse jump;

    private static String JUMP = "jump";
    private static int JUMP_SLOW_FRAME = 30;
    private static String LEFT_WALK = "left_walk";
    private static String RIGHT_WALK = "right_walk";

    @ConfigConstructor
    public PlayerMovementController(ApriWorld world, Entity entity) {
        super(world, entity);
        impulses = entity.body.getImpulses();
        jump = impulses.put(JUMP, new Impulse(frame -> {
            if (frame < JUMP_SLOW_FRAME) {
                return new Vector2(0, 35);
            } else {
                float yVelocity = (float) (35 * Math.pow(0.95, frame - JUMP_SLOW_FRAME));
                if (yVelocity <= 0.5f) {
                    impulses.get(JUMP).deactivate();
                }
                return new Vector2(0, yVelocity);
            }
        }));
        impulses.put(LEFT_WALK, new Impulse(frame -> new Vector2(-13, 0)));
        impulses.put(RIGHT_WALK, new Impulse(frame -> new Vector2(13, 0)));
        canJump = entity.getProperty(CAN_JUMP, Boolean.class);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean keyDown(Keyboard.Key keycode) {
        switch (keycode) {
            case A:
                impulses.get(LEFT_WALK).activate();
                break;
            case D:
                impulses.get(RIGHT_WALK).activate();
                break;
            case W:
                impulses.get(JUMP).activate();
                break;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(Keyboard.Key keycode) {
        switch (keycode) {
            case A:
                impulses.get(LEFT_WALK).deactivate();
                break;
            case D:
                impulses.get(RIGHT_WALK).deactivate();
                break;
            case W:
                if (!canJump.get()) {
                    impulses.get(JUMP).setCurrentFrame(JUMP_SLOW_FRAME);
                }
                break;
        }
        return super.keyUp(keycode);
    }

    public boolean isKeyPressed(Keyboard.Key key) {
        return o.isKeyPressed(o.keycode(key));
    }
}
