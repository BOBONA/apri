package com.bobona.apri.input;

public interface InputProcessor {

    boolean keyDown(Keyboard.Key keycode);
    boolean keyUp(Keyboard.Key keycode);
    boolean keyTyped(char character);
    boolean touchDown(int screenX, int screenY, int pointer, int button);
    boolean touchUp(int screenX, int screenY, int pointer, int button);
    boolean touchDragged(int screenX, int screenY, int pointer);
    boolean mouseMoved(int screenX, int screenY);
    boolean scrolled(int amount);

    class Event {

        Type type;
        int keycode;
        char character;
        int screenX;
        int screenY;
        int pointer;
        int button;
        int scrollAmount;

        public Event(Type type, int keycode, char character, int screenX, int screenY, int pointer, int button, int scrollAmount) {
            this.type = type;
            this.keycode = keycode;
            this.character = character;
            this.screenX = screenX;
            this.screenY = screenY;
            this.pointer = pointer;
            this.button = button;
            this.scrollAmount = scrollAmount;
        }

        public enum Type {
            KEY_DOWN,
            KEY_UP,
            KEY_TYPED,
            TOUCH_DOWN,
            TOUCH_UP,
            TOUCH_DRAGGED,
            MOUSE_MOVED,
            SCROLLED
        }
    }
}
