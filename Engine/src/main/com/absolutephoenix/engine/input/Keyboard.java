package com.absolutephoenix.engine.input;

import org.lwjgl.glfw.GLFW;

/**
 * Tracks keyboard state across frames.
 */
public class Keyboard {
    private final boolean[] current = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private final boolean[] previous = new boolean[GLFW.GLFW_KEY_LAST + 1];

    void handle(int key, int action) {
        if (key >= 0 && key < current.length) {
            current[key] = action != GLFW.GLFW_RELEASE;
        }
    }

    void update() {
        System.arraycopy(current, 0, previous, 0, current.length);
    }

    /** Returns true while the given key is held down. */
    public boolean keyDown(int key) {
        return current[key];
    }

    /** Returns true only on the frame the key was pressed. */
    public boolean keyPressed(int key) {
        return current[key] && !previous[key];
    }

    /** Returns true on the frame the key was released. */
    public boolean keyReleased(int key) {
        return !current[key] && previous[key];
    }
}
