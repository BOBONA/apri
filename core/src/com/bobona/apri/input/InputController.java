package com.bobona.apri.input;

public interface InputController {

    void update();
    void registerInputProcessor(InputProcessor processor);
    void removeInputProcessor(InputProcessor processor);
}
