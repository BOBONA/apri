package com.bobona.apri.input;

import com.badlogic.gdx.Gdx;
import com.bobona.apri.utils.ConfigConstructor;

import java.util.ArrayList;
import java.util.List;

public class Box2DInputController implements InputController, com.badlogic.gdx.InputProcessor {

    List<InputProcessor.Event> inputEvents;
    List<InputProcessor> inputProcessors;

    @ConfigConstructor
    public Box2DInputController() {
        inputEvents = new ArrayList<>();
        inputProcessors = new ArrayList<>();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update() {
        for (InputProcessor processor : inputProcessors) {
            for (InputProcessor.Event event : inputEvents) {
                switch (event.type) {
                    case KEY_DOWN:
                        processor.keyDown(Keyboard.o.key(event.keycode));
                        break;
                    case KEY_UP:
                        processor.keyUp(Keyboard.o.key(event.keycode));
                        break;
                    case KEY_TYPED:
                        processor.keyTyped(event.character);
                        break;
                    case TOUCH_DOWN:
                        processor.touchDown(event.screenX, event.screenY, event.pointer, event.button);
                        break;
                    case TOUCH_UP:
                        processor.touchUp(event.screenX, event.screenY, event.pointer, event.button);
                        break;
                    case TOUCH_DRAGGED:
                        processor.touchDragged(event.screenX, event.screenY, event.pointer);
                        break;
                    case MOUSE_MOVED:
                        processor.mouseMoved(event.screenX, event.screenY);
                        break;
                    case SCROLLED:
                        processor.scrolled(event.scrollAmount);
                        break;
                }
            }
        }
        inputEvents.clear();
    }

    @Override
    public void registerInputProcessor(InputProcessor processor) {
        inputProcessors.add(processor);
    }

    @Override
    public void removeInputProcessor(InputProcessor processor) {
        inputProcessors.remove(processor);
    }

    @Override
    public boolean keyDown(int keycode) {
        inputEvents.add(new InputProcessor.Event(
                InputProcessor.Event.Type.KEY_DOWN,
                keycode,
                Character.MIN_VALUE, 0, 0, 0, 0, 0
        ));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        inputEvents.add(new InputProcessor.Event(
                InputProcessor.Event.Type.KEY_UP,
                keycode,
                Character.MIN_VALUE, 0, 0, 0, 0, 0
        ));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        inputEvents.add(new InputProcessor.Event(
                InputProcessor.Event.Type.KEY_TYPED,
                0,
                character,
                0, 0, 0, 0, 0
        ));
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        inputEvents.add(new InputProcessor.Event(
                InputProcessor.Event.Type.TOUCH_DOWN,
                0, Character.MIN_VALUE,
                screenX, screenY, pointer, button, 0
        ));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        inputEvents.add(new InputProcessor.Event(
                InputProcessor.Event.Type.TOUCH_UP,
                0, Character.MIN_VALUE,
                screenX, screenY, pointer, button, 0
        ));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        inputEvents.add(new InputProcessor.Event(
                InputProcessor.Event.Type.TOUCH_DRAGGED,
                0, Character.MIN_VALUE,
                screenX, screenY, pointer, 0, 0
        ));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        inputEvents.add(new InputProcessor.Event(
                InputProcessor.Event.Type.MOUSE_MOVED,
                0, Character.MIN_VALUE,
                screenX, screenY, 0, 0, 0
        ));
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        inputEvents.add(new InputProcessor.Event(
                InputProcessor.Event.Type.KEY_DOWN,
                0, Character.MIN_VALUE, 0, 0, 0, 0,
                amount
        ));
        return false;
    }
}