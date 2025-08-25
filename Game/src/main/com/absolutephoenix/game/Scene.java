package com.absolutephoenix.game;

import com.absolutephoenix.engine.SpriteBatch;

public interface Scene {
    void initialize();
    void update();
    void input();
    void render(SpriteBatch batch);
    void cleanup();
}
