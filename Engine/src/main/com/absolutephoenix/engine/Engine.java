package com.absolutephoenix.engine;

public class Engine {
    private final Window window;
    private final GameLoop gameLoop;
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

    public void setTargetUPS(int ups) {
        this.targetUPS = ups;
    }

    public void setTargetFPS(int fps) {
        this.targetFPS = fps;
    }

    public void setVSync(boolean vsync) {
        this.vsync = vsync;
        if (window.isInitialized()) {
            window.setVSync(vsync);
        }
    }

    public void start() {
        window.init();
        window.setVSync(vsync);
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
                gameLoop.input();
                gameLoop.update();
                updates++;
                deltaUpdate -= timePerUpdate;
            }

            if (deltaFrame >= timePerFrame) {
                gameLoop.render();
                window.update();
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
