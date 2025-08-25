package com.absolutephoenix.engine.input;

import org.lwjgl.glfw.GLFW;
import java.util.Optional;

/**
 * Central access point for keyboard, mouse and controller input.
 */
public class InputHandler {
    private static InputHandler instance;

    public final Keyboard keyboard = new Keyboard();
    public final Mouse mouse = new Mouse();
    public final Controller controller = new Controller();

    public InputHandler(long window) {
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
        return Optional.ofNullable(instance);
    }

    /** Resets the singleton instance. */
    public static void dispose() {
        instance = null;
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
