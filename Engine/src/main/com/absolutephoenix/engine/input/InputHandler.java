package com.absolutephoenix.engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import java.util.Optional;

/**
 * Central access point for keyboard, mouse and controller input.
 */
public class InputHandler {
    private static volatile InputHandler instance;

    private final long window;

    public final Keyboard keyboard = new Keyboard();
    public final Mouse mouse = new Mouse();
    public final Controller controller = new Controller();

    public InputHandler(long window) {
        this.window = window;
        instance = this;
        GLFW.glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> keyboard.handle(key, action));
        GLFW.glfwSetMouseButtonCallback(window, (w, button, action, mods) -> mouse.handleButton(button, action));
        GLFW.glfwSetCursorPosCallback(window, (w, xpos, ypos) -> mouse.handlePosition(xpos, ypos));
        GLFW.glfwSetScrollCallback(window, (w, xoff, yoff) -> mouse.handleScroll(xoff, yoff));
    }

    /**
     * Returns the singleton instance, if it has been initialized.
     *
     * @return an {@link Optional} containing the current instance or empty if
     *         no handler has been created yet
     */
    public static Optional<InputHandler> get() {
        synchronized (InputHandler.class) {
            return Optional.ofNullable(instance);
        }
    }

    /** Resets the singleton instance. */
    public static void dispose() {
        synchronized (InputHandler.class) {
            instance = null;
        }
    }

    /**
     * Removes GLFW callbacks and frees the associated resources.
     */
    public void close() {
        GLFWKeyCallback keyCb = GLFW.glfwSetKeyCallback(window, null);
        if (keyCb != null) keyCb.free();
        GLFWMouseButtonCallback mouseBtnCb = GLFW.glfwSetMouseButtonCallback(window, null);
        if (mouseBtnCb != null) mouseBtnCb.free();
        GLFWCursorPosCallback cursorPosCb = GLFW.glfwSetCursorPosCallback(window, null);
        if (cursorPosCb != null) cursorPosCb.free();
        GLFWScrollCallback scrollCb = GLFW.glfwSetScrollCallback(window, null);
        if (scrollCb != null) scrollCb.free();
    }

    /** Polls devices that require manual polling. */
    public void poll() {
        controller.poll();
    }

    /** Updates internal state after input has been processed. */
    public void update() {
        keyboard.update();
        mouse.update();
        controller.update();
    }
}
