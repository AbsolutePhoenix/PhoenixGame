package com.absolutephoenix.engine.core;

import com.absolutephoenix.engine.input.InputHandler;
import com.absolutephoenix.engine.window.Window;

/**
 * Main engine that owns the window and drives the game loop.
 */
public class Engine {
    private final Window window;
    private final GameLoop gameLoop;
    private InputHandler input;
    private int targetUPS = 60;
    private int targetFPS = 60;
    private boolean vsync;
    private final String baseTitle;

    public Engine(GameLoop gameLoop, int width, int height, String title, boolean vsync) {
        this.gameLoop = gameLoop;
        this.window = new Window(width, height, title);
        this.vsync = vsync;
        this.baseTitle = title;
    }

    /** Returns the input handler associated with this engine. */
    public InputHandler getInput() {
        return input;
    }

    /** Sets the target updates per second. */
    public void setTargetUPS(int ups) {
        this.targetUPS = ups;
    }

    /** Sets the target frames per second. */
    public void setTargetFPS(int fps) {
        this.targetFPS = fps;
    }

    /** Enables or disables vertical synchronization. */
    public void setVSync(boolean vsync) {
        this.vsync = vsync;
        if (window.isInitialized()) {
            window.setVSync(vsync);
        }
    }

    /** Starts the engine and enters the main loop. */
    public void start() {
        window.init();
        window.setVSync(vsync);
        input = new InputHandler(window.getHandle());
        input.update(); // prime previous state
        gameLoop.initialize();

        double timePerUpdate = 1.0 / targetUPS;
        double timePerFrame = 1.0 / targetFPS;
        double deltaUpdate = 0;
        double deltaFrame = 0;
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        while (!window.shouldClose()) {
            long now = System.nanoTime();
            double delta = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;
            deltaUpdate += delta;
            deltaFrame += delta;

            while (deltaUpdate >= timePerUpdate) {
                input.poll();
                gameLoop.input();
                input.update();
                gameLoop.update();
                updates++;
                deltaUpdate -= timePerUpdate;
            }

            if (deltaFrame >= timePerFrame) {
                window.pollEvents();
                gameLoop.render();
                window.swapBuffers();
                frames++;
                deltaFrame -= timePerFrame;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                window.setTitle(baseTitle + " (UPS:" + updates + " FPS:" + frames + ")");
                updates = 0;
                frames = 0;
                timer += 1000;
            }
        }

        gameLoop.cleanup();
        window.destroy();
    }
}
