package com.absolutephoenix.engine.input;

import org.lwjgl.glfw.GLFW;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Polls controller/gamepad state using GLFW.
 */
public class Controller {
    private static final int MAX_BUTTONS = 32;
    private static final int MAX_AXES = 6;
    private static final float TRIGGER_THRESHOLD = 0.5f;

    private final boolean[] current = new boolean[MAX_BUTTONS];
    private final boolean[] previous = new boolean[MAX_BUTTONS];
    private final float[] axesCurrent = new float[MAX_AXES];
    private final float[] axesPrevious = new float[MAX_AXES];

    // Common button constants
    public static final int BUTTON_A = GLFW.GLFW_GAMEPAD_BUTTON_A; // Cross on PlayStation
    public static final int BUTTON_B = GLFW.GLFW_GAMEPAD_BUTTON_B; // Circle on PlayStation
    public static final int BUTTON_X = GLFW.GLFW_GAMEPAD_BUTTON_X; // Square on PlayStation
    public static final int BUTTON_Y = GLFW.GLFW_GAMEPAD_BUTTON_Y; // Triangle on PlayStation
    public static final int BUTTON_START = GLFW.GLFW_GAMEPAD_BUTTON_START;
    public static final int BUTTON_SELECT = GLFW.GLFW_GAMEPAD_BUTTON_BACK;
    public static final int BUTTON_LB = GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER;
    public static final int BUTTON_RB = GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER;
    public static final int BUTTON_LS = GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB;
    public static final int BUTTON_RS = GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB;
    public static final int BUTTON_DPAD_UP = GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP;
    public static final int BUTTON_DPAD_RIGHT = GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT;
    public static final int BUTTON_DPAD_DOWN = GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN;
    public static final int BUTTON_DPAD_LEFT = GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

    // Axis constants
    public static final int AXIS_LEFT_X = GLFW.GLFW_GAMEPAD_AXIS_LEFT_X;
    public static final int AXIS_LEFT_Y = GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y;
    public static final int AXIS_RIGHT_X = GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X;
    public static final int AXIS_RIGHT_Y = GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;
    public static final int AXIS_LT = GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER;
    public static final int AXIS_RT = GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER;

    void poll() {
        if (GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1)) {
            ByteBuffer buttons = GLFW.glfwGetJoystickButtons(GLFW.GLFW_JOYSTICK_1);
            if (buttons != null) {
                int limit = Math.min(buttons.limit(), MAX_BUTTONS);
                for (int i = 0; i < limit; i++) {
                    current[i] = buttons.get(i) == GLFW.GLFW_PRESS;
                }
            }

            FloatBuffer axes = GLFW.glfwGetJoystickAxes(GLFW.GLFW_JOYSTICK_1);
            if (axes != null) {
                int limit = Math.min(axes.limit(), MAX_AXES);
                for (int i = 0; i < limit; i++) {
                    axesCurrent[i] = axes.get(i);
                }
            }
        }
    }

    void update() {
        System.arraycopy(current, 0, previous, 0, current.length);
        System.arraycopy(axesCurrent, 0, axesPrevious, 0, axesCurrent.length);

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
  
    public float axis(int axis) {
        return axis < axesCurrent.length ? axesCurrent[axis] : 0f;
    }

    private boolean triggerDown(int axis) {
        return axis(axis) > TRIGGER_THRESHOLD;
    }

    private boolean triggerPressed(int axis) {
        return triggerDown(axis) && axesPrevious[axis] <= TRIGGER_THRESHOLD;
    }

    private boolean triggerReleased(int axis) {
        return !triggerDown(axis) && axesPrevious[axis] > TRIGGER_THRESHOLD;
    }

    // Convenience PlayStation-style names for face buttons
    public boolean squareDown() { return buttonDown(BUTTON_X); }
    public boolean squarePressed() { return buttonPressed(BUTTON_X); }
    public boolean squareReleased() { return buttonReleased(BUTTON_X); }

    public boolean crossDown() { return buttonDown(BUTTON_A); }
    public boolean crossPressed() { return buttonPressed(BUTTON_A); }
    public boolean crossReleased() { return buttonReleased(BUTTON_A); }

    public boolean circleDown() { return buttonDown(BUTTON_B); }
    public boolean circlePressed() { return buttonPressed(BUTTON_B); }
    public boolean circleReleased() { return buttonReleased(BUTTON_B); }

    public boolean triangleDown() { return buttonDown(BUTTON_Y); }
    public boolean trianglePressed() { return buttonPressed(BUTTON_Y); }
    public boolean triangleReleased() { return buttonReleased(BUTTON_Y); }

    // Start/Select
    public boolean startDown() { return buttonDown(BUTTON_START); }
    public boolean startPressed() { return buttonPressed(BUTTON_START); }
    public boolean startReleased() { return buttonReleased(BUTTON_START); }

    public boolean selectDown() { return buttonDown(BUTTON_SELECT); }
    public boolean selectPressed() { return buttonPressed(BUTTON_SELECT); }
    public boolean selectReleased() { return buttonReleased(BUTTON_SELECT); }

    // Bumpers
    public boolean leftBumperDown() { return buttonDown(BUTTON_LB); }
    public boolean leftBumperPressed() { return buttonPressed(BUTTON_LB); }
    public boolean leftBumperReleased() { return buttonReleased(BUTTON_LB); }

    public boolean rightBumperDown() { return buttonDown(BUTTON_RB); }
    public boolean rightBumperPressed() { return buttonPressed(BUTTON_RB); }
    public boolean rightBumperReleased() { return buttonReleased(BUTTON_RB); }

    // D-pad
    public boolean dpadUpDown() { return buttonDown(BUTTON_DPAD_UP); }
    public boolean dpadUpPressed() { return buttonPressed(BUTTON_DPAD_UP); }
    public boolean dpadUpReleased() { return buttonReleased(BUTTON_DPAD_UP); }

    public boolean dpadRightDown() { return buttonDown(BUTTON_DPAD_RIGHT); }
    public boolean dpadRightPressed() { return buttonPressed(BUTTON_DPAD_RIGHT); }
    public boolean dpadRightReleased() { return buttonReleased(BUTTON_DPAD_RIGHT); }

    public boolean dpadDownDown() { return buttonDown(BUTTON_DPAD_DOWN); }
    public boolean dpadDownPressed() { return buttonPressed(BUTTON_DPAD_DOWN); }
    public boolean dpadDownReleased() { return buttonReleased(BUTTON_DPAD_DOWN); }

    public boolean dpadLeftDown() { return buttonDown(BUTTON_DPAD_LEFT); }
    public boolean dpadLeftPressed() { return buttonPressed(BUTTON_DPAD_LEFT); }
    public boolean dpadLeftReleased() { return buttonReleased(BUTTON_DPAD_LEFT); }

    // Stick buttons
    public boolean leftStickDown() { return buttonDown(BUTTON_LS); }
    public boolean leftStickPressed() { return buttonPressed(BUTTON_LS); }
    public boolean leftStickReleased() { return buttonReleased(BUTTON_LS); }

    public boolean rightStickDown() { return buttonDown(BUTTON_RS); }
    public boolean rightStickPressed() { return buttonPressed(BUTTON_RS); }
    public boolean rightStickReleased() { return buttonReleased(BUTTON_RS); }

    // Triggers
    public float leftTrigger() { return axis(AXIS_LT); }
    public boolean leftTriggerDown() { return triggerDown(AXIS_LT); }
    public boolean leftTriggerPressed() { return triggerPressed(AXIS_LT); }
    public boolean leftTriggerReleased() { return triggerReleased(AXIS_LT); }

    public float rightTrigger() { return axis(AXIS_RT); }
    public boolean rightTriggerDown() { return triggerDown(AXIS_RT); }
    public boolean rightTriggerPressed() { return triggerPressed(AXIS_RT); }
    public boolean rightTriggerReleased() { return triggerReleased(AXIS_RT); }

    // Joystick axes
    public float leftStickX() { return axis(AXIS_LEFT_X); }
    public float leftStickY() { return axis(AXIS_LEFT_Y); }
    public float rightStickX() { return axis(AXIS_RIGHT_X); }
    public float rightStickY() { return axis(AXIS_RIGHT_Y); }
}
