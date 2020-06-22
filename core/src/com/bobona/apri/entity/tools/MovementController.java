package com.bobona.apri.entity.tools;

import com.badlogic.gdx.physics.box2d.World;
import com.bobona.apri.ApriWorld;
import com.bobona.apri.entity.Entity;
import com.bobona.apri.input.InputProcessor;
import com.bobona.apri.input.Keyboard;

public abstract class MovementController implements InputProcessor {

    Entity entity;
    ApriWorld world;

    public MovementController(ApriWorld world, Entity entity) {
        this.entity = entity;
        this.world = world;
    }

    public abstract void update();

    public void close() {
        world.inputController.removeInputProcessor(this);
    }

    @Override
    public boolean keyDown(Keyboard.Key keycode) {
        return false;
    }

    @Override
    public boolean keyUp(Keyboard.Key keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

