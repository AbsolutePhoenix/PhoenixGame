package com.absolutephoenix.engine.input;

import org.lwjgl.glfw.GLFW;

/**
 * Tracks mouse state, position and scroll.
 */
public class Mouse {
    private final boolean[] current = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
    private final boolean[] previous = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
    private double x;
    private double y;
    private double scrollX;
    private double scrollY;

    void handleButton(int button, int action) {
        if (button >= 0 && button < current.length) {
            current[button] = action != GLFW.GLFW_RELEASE;
        }
    }

    void handlePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    void handleScroll(double xoff, double yoff) {
        scrollX += xoff;
        scrollY += yoff;
    }

    void update() {
        System.arraycopy(current, 0, previous, 0, current.length);
        scrollX = 0;
        scrollY = 0;
    }

    public boolean buttonDown(int button) {
        return current[button];
    }

    public boolean buttonPressed(int button) {
        return current[button] && !previous[button];
    }

    public boolean buttonReleased(int button) {
        return !current[button] && previous[button];
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }
}
