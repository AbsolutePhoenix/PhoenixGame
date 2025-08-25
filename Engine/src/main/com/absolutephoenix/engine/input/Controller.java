package com.absolutephoenix.engine.input;

import org.lwjgl.glfw.GLFW;
import java.nio.ByteBuffer;

/**
 * Polls controller/gamepad state using GLFW.
 */
public class Controller {
    private static final int MAX_BUTTONS = 32;
    private final boolean[] current = new boolean[MAX_BUTTONS];
    private final boolean[] previous = new boolean[MAX_BUTTONS];

    // Common button constants
    public static final int BUTTON_A = GLFW.GLFW_GAMEPAD_BUTTON_A;
    public static final int BUTTON_B = GLFW.GLFW_GAMEPAD_BUTTON_B;
    public static final int BUTTON_X = GLFW.GLFW_GAMEPAD_BUTTON_X; // Square on PlayStation
    public static final int BUTTON_Y = GLFW.GLFW_GAMEPAD_BUTTON_Y; // Triangle on PlayStation

    void poll() {
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1)) {
            ByteBuffer buttons = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1);
            if (buttons != null) {
                int limit = Math.min(buttons.limit(), MAX_BUTTONS);
                for (int i = 0; i < limit; i++) {
                    current[i] = buttons.get(i) == GLFW.GLFW_PRESS;
                }
            }
        }
    }

    void update() {
        System.arraycopy(current, 0, previous, 0, current.length);
    }

    public boolean buttonDown(int button) {
        return button < current.length && current[button];
    }

    public boolean buttonPressed(int button) {
        return button < current.length && current[button] && !previous[button];
    }

    public boolean buttonReleased(int button) {
        return button < current.length && !current[button] && previous[button];
    }

    // Convenience PlayStation-style names
    public boolean squarePressed() { return buttonPressed(BUTTON_X); }
    public boolean crossPressed() { return buttonPressed(BUTTON_A); }
    public boolean circlePressed() { return buttonPressed(BUTTON_B); }
    public boolean trianglePressed() { return buttonPressed(BUTTON_Y); }
}
