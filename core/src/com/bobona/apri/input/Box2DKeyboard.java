package com.bobona.apri.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.bobona.apri.Demo;
import com.bobona.apri.utils.ConfigConstructor;
import com.bobona.apri.utils.Vector2;
import com.bobona.apri.utils.Vector3;

import java.security.Key;

import static com.bobona.apri.input.Keyboard.Key.*;

public class Box2DKeyboard implements Keyboard {

    @ConfigConstructor
    public Box2DKeyboard() {

    }

    @Override
    public boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }

    @Override
    public boolean isKeyJustPressed(int key) {
        return Gdx.input.isKeyJustPressed(key);
    }

    @Override
    public Vector2 getMousePos() {
        Vector3 point = new Vector3(Demo.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0).libgdx()));
        return new Vector2(point.x, point.y);
    }

    @Override
    public float getMouseX() {
        return getMousePos().x;
    }

    @Override
    public float getMouseY() {
        return getMousePos().y;
    }

    public static ObjectMap<Key, Integer> keyCodeMap = new ObjectMap<>();
    static {
        Object[] keyCodeArray = new Object[]{
            ANY_KEY, -1,
            NUM_0, 7,
            NUM_1, 8,
            NUM_2, 9,
            NUM_3, 10,
            NUM_4, 11,
            NUM_5, 12,
            NUM_6, 13,
            NUM_7, 14,
            NUM_8, 15,
            NUM_9, 16,
            A, 29,
            ALT_LEFT, 57,
            ALT_RIGHT, 58,
            APOSTROPHE, 75,
            AT, 77,
            B, 30,
            BACK, 4,
            BACKSLASH, 73,
            C, 31,
            CALL, 5,
            CAMERA, 27,
            CLEAR, 28,
            COMMA, 55,
            D, 32,
            DEL, 67,
            BACKSPACE, 67,
            FORWARD_DEL, 112,
            DPAD_CENTER, 23,
            DPAD_DOWN, 20,
            DPAD_LEFT, 21,
            DPAD_RIGHT, 22,
            DPAD_UP, 19,
            CENTER, 23,
            DOWN, 20,
            LEFT, 21,
            RIGHT, 22,
            UP, 19,
            E, 33,
            ENDCALL, 6,
            ENTER, 66,
            ENVELOPE, 65,
            EQUALS, 70,
            EXPLORER, 64,
            F, 34,
            FOCUS, 80,
            G, 35,
            GRAVE, 68,
            H, 36,
            HEADSETHOOK, 79,
            HOME, 3,
            I, 37,
            J, 38,
            K, 39,
            L, 40,
            LEFT_BRACKET, 71,
            M, 41,
            MEDIA_FAST_FORWARD, 90,
            MEDIA_NEXT, 87,
            MEDIA_PLAY_PAUSE, 85,
            MEDIA_PREVIOUS, 88,
            MEDIA_REWIND, 89,
            MEDIA_STOP, 86,
            MENU, 82,
            MINUS, 69,
            MUTE, 91,
            N, 42,
            NOTIFICATION, 83,
            NUM, 78,
            O, 43,
            P, 44,
            PERIOD, 56,
            PLUS, 81,
            POUND, 18,
            POWER, 26,
            Q, 45,
            R, 46,
            RIGHT_BRACKET, 72,
            S, 47,
            SEARCH, 84,
            SEMICOLON, 74,
            SHIFT_LEFT, 59,
            SHIFT_RIGHT, 60,
            SLASH, 76,
            SOFT_LEFT, 1,
            SOFT_RIGHT, 2,
            SPACE, 62,
            STAR, 17,
            SYM, 63,
            T, 48,
            TAB, 61,
            U, 49,
            UNKNOWN, 0,
            V, 50,
            VOLUME_DOWN, 25,
            VOLUME_UP, 24,
            W, 51,
            X, 52,
            Y, 53,
            Z, 54,
            META_ALT_LEFT_ON, 16,
            META_ALT_ON, 2,
            META_ALT_RIGHT_ON, 32,
            META_SHIFT_LEFT_ON, 64,
            META_SHIFT_ON, 1,
            META_SHIFT_RIGHT_ON, 128,
            META_SYM_ON, 4,
            CONTROL_LEFT, 129,
            CONTROL_RIGHT, 130,
            ESCAPE, 131,
            END, 132,
            INSERT, 133,
            PAGE_UP, 92,
            PAGE_DOWN, 93,
            PICTSYMBOLS, 94,
            SWITCH_CHARSET, 95,
            BUTTON_CIRCLE, 255,
            BUTTON_A, 96,
            BUTTON_B, 97,
            BUTTON_C, 98,
            BUTTON_X, 99,
            BUTTON_Y, 100,
            BUTTON_Z, 101,
            BUTTON_L1, 102,
            BUTTON_R1, 103,
            BUTTON_L2, 104,
            BUTTON_R2, 105,
            BUTTON_THUMBL, 106,
            BUTTON_THUMBR, 107,
            BUTTON_START, 108,
            BUTTON_SELECT, 109,
            BUTTON_MODE, 110,
            NUMPAD_0, 144,
            NUMPAD_1, 145,
            NUMPAD_2, 146,
            NUMPAD_3, 147,
            NUMPAD_4, 148,
            NUMPAD_5, 149,
            NUMPAD_6, 150,
            NUMPAD_7, 151,
            NUMPAD_8, 152,
            NUMPAD_9, 153,
            COLON, 243,
            F1, 244,
            F2, 245,
            F3, 246,
            F4, 247,
            F5, 248,
            F6, 249,
            F7, 250,
            F8, 251,
            F9, 252,
            F10, 253,
            F11, 254,
            F12, 255
        };
        for (int i = 0; i < keyCodeArray.length; i += 2) {
            keyCodeMap.put((Key) keyCodeArray[i], (Integer) keyCodeArray[i + 1]);
        }
    }

    @Override
    public int keycode(Key key) {
        return keyCodeMap.get(key);
    }

    @Override
    public Key key(int keycode) {
        return keyCodeMap.findKey(keycode, false);
    }
}
