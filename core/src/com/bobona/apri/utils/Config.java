package com.bobona.apri.utils;

import com.bobona.apri.input.*;
import com.bobona.apri.physics.*;
import com.bobona.apri.utils.ConfigConstructor;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class Config {

    public static Class<? extends PhysicsWorld> PHYSICS_WORLD_CLASS = Box2DWorld.class;
    public static Class<? extends PhysicsBody> PHYSICS_BODY_CLASS = Box2DPhysicsBody.class;
    public static Class<? extends InputController> INPUT_CONTROLLER_CLASS = Box2DInputController.class;
    public static Class<? extends Keyboard> KEYBOARD_CLASS = Box2DKeyboard.class;
    public static Class<? extends ContactListener> CONTACT_LISTENER_CLASS = Box2DContactListener.class;

    public static <T> T instantiate(Class<T> clazz, Object... initargs) {
        try {
            Constructor<?> configConstructor = Arrays.stream(clazz.getDeclaredConstructors())
                    .filter(constructor -> constructor.isAnnotationPresent(ConfigConstructor.class))
                    .findFirst()
                    .orElseThrow(() -> new Exception(clazz.getName() + " does not have a config constructor."));
            return clazz.getConstructor(configConstructor.getParameterTypes()).newInstance(initargs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
