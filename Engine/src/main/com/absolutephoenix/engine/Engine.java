package com.absolutephoenix.engine;

import org.lwjgl.opengl.GL11;

public class Engine {
    private Window window;
    private SpriteBatch batch;
    private Texture texture;

    public void start() {
        window = new Window(800, 600, "Phoenix Game");
        window.init();
        batch = new SpriteBatch(window.getWidth(), window.getHeight());
        texture = new Texture("/white.png");

        while (!window.shouldClose()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            batch.begin();
            batch.draw(texture, 100, 100, 128, 128);
            batch.end();
            window.update();
        }

        batch.delete();
        texture.delete();
        window.destroy();
    }
}
