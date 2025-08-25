package com.absolutephoenix.game;

public interface Scene {
    void initialize();
    void update();
    void input();
    void render();
    void cleanup();
}
