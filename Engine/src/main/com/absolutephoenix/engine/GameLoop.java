package com.absolutephoenix.engine;

public interface GameLoop {
    void initialize();
    void update();
    void input();
    void render();
    void cleanup();
}
