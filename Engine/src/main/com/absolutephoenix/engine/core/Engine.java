package com.absolutephoenix.engine.core;

import com.absolutephoenix.engine.input.InputHandler;
import com.absolutephoenix.engine.window.Window;

import static org.lwjgl.glfw.GLFW.glfwWaitEventsTimeout;

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

        final double timePerUpdate = 1.0 / targetUPS;
        final Double timePerFrame = (targetFPS > 0) ? (1.0 / targetFPS) : null;

        long lastTimeNs = System.nanoTime();
        long timerMs = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        // Schedule the next deadlines
        long nextUpdateNs = lastTimeNs + (long)(timePerUpdate * 1_000_000_000L);
        long nextFrameNs  = (timePerFrame != null)
                ? lastTimeNs + (long)(timePerFrame * 1_000_000_000L)
                : Long.MAX_VALUE; // unlimited FPS => no frame deadline

        while (!window.shouldClose()) {
            long nowNs = System.nanoTime();

            // --- UPDATE(S) ---
            while (nowNs >= nextUpdateNs) {
                input.poll();
                gameLoop.input();
                input.update();
                gameLoop.update();
                updates++;
                nextUpdateNs += (long)(timePerUpdate * 1_000_000_000L);

                // catch-up guard: if we fell very far behind, jump to now + period
                if (nowNs - nextUpdateNs > 1_000_000_000L) {
                    nextUpdateNs = nowNs + (long)(timePerUpdate * 1_000_000_000L);
                }
            }

            // --- RENDER ---
            boolean rendered = false;
            if (vsync) {
                // With vsync, swap buffers will block; render every iteration.
                window.pollEvents();
                gameLoop.render();
                window.swapBuffers();
                frames++;
                rendered = true;
            } else {
                if (timePerFrame == null || nowNs >= nextFrameNs) {
                    window.pollEvents();
                    gameLoop.render();
                    window.swapBuffers();
                    frames++;
                    if (timePerFrame != null) {
                        nextFrameNs += (long)(timePerFrame * 1_000_000_000L);
                    }
                    rendered = true;
                }
            }

            // --- TITLE/COUNTERS ---
            long nowMs = System.currentTimeMillis();
            if (nowMs - timerMs >= 1000) {
                window.setTitle(baseTitle + " (UPS:" + updates + " FPS:" + frames + ")");
                updates = 0;
                frames = 0;
                timerMs += 1000;
            }

            // --- IDLE / YIELD WHEN NOTHING TO DO ---
            if (!rendered) {
                // figure out the next time we need to do *anything*
                long nextWorkNs = Math.min(nextUpdateNs, nextFrameNs);
                long waitNs = nextWorkNs - System.nanoTime();
                if (waitNs > 0) {
                    // Prefer GLFW's event wait (lets the OS wake us early on input/window events)
                    // Falls back to a precise park if you don’t want to block on events.
                    double waitSec = Math.min(waitNs / 1_000_000_000.0, 0.010); // cap at 10ms to stay responsive
                    glfwWaitEventsTimeout(waitSec);

                    // If you prefer not to use glfwWaitEventsTimeout, comment it out and use:
                    // LockSupport.parkNanos(Math.min(waitNs, 2_000_000L)); // up to ~2ms park
                    // Thread.onSpinWait(); // optional tiny spin for sub-millisecond accuracy
                } else {
                    // We're due right away; a short spin hint helps perf without burning a core
                    Thread.onSpinWait();
                }
            }
        }

        gameLoop.cleanup();
        window.destroy();
    }
}
