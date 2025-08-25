package com.absolutephoenix.game.scenes;

import com.absolutephoenix.engine.SpriteBatch;
import com.absolutephoenix.engine.Texture;
import com.absolutephoenix.game.Scene;

public class ExampleScene implements Scene {
    private final Texture texture;

    public ExampleScene(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update() {
    }

    @Override
    public void input() {
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, 100, 100, 128, 128);
    }

    @Override
    public void cleanup() {
    }
}
