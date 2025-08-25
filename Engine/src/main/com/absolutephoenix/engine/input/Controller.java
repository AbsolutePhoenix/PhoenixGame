package com.absolutephoenix.engine.input;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerButton;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

/**
 * Polls controller/gamepad state using Jamepad.
 */
public class Controller {
    private static final float TRIGGER_THRESHOLD = 0.5f;

    private final ControllerManager manager = new ControllerManager();
    private ControllerState current;
    private ControllerState previous;

    // Common button constants
    public static final int BUTTON_A = ControllerButton.A.ordinal(); // Cross on PlayStation
    public static final int BUTTON_B = ControllerButton.B.ordinal(); // Circle on PlayStation
    public static final int BUTTON_X = ControllerButton.X.ordinal(); // Square on PlayStation
    public static final int BUTTON_Y = ControllerButton.Y.ordinal(); // Triangle on PlayStation
    public static final int BUTTON_START = ControllerButton.START.ordinal();
    public static final int BUTTON_SELECT = ControllerButton.BACK.ordinal();
    public static final int BUTTON_LB = ControllerButton.LEFTBUMPER.ordinal();
    public static final int BUTTON_RB = ControllerButton.RIGHTBUMPER.ordinal();
    public static final int BUTTON_LS = ControllerButton.LEFTSTICK.ordinal();
    public static final int BUTTON_RS = ControllerButton.RIGHTSTICK.ordinal();
    public static final int BUTTON_DPAD_UP = ControllerButton.DPAD_UP.ordinal();
    public static final int BUTTON_DPAD_RIGHT = ControllerButton.DPAD_RIGHT.ordinal();
    public static final int BUTTON_DPAD_DOWN = ControllerButton.DPAD_DOWN.ordinal();
    public static final int BUTTON_DPAD_LEFT = ControllerButton.DPAD_LEFT.ordinal();

    // Axis constants
    public static final int AXIS_LEFT_X = ControllerAxis.LEFTX.ordinal();
    public static final int AXIS_LEFT_Y = ControllerAxis.LEFTY.ordinal();
    public static final int AXIS_RIGHT_X = ControllerAxis.RIGHTX.ordinal();
    public static final int AXIS_RIGHT_Y = ControllerAxis.RIGHTY.ordinal();
    public static final int AXIS_LT = ControllerAxis.TRIGGERLEFT.ordinal();
    public static final int AXIS_RT = ControllerAxis.TRIGGERRIGHT.ordinal();

    public Controller() {
        manager.initSDLGamepad();
        current = manager.getState(0);
        previous = current;
    }

    void poll() {
        current = manager.getState(0);
    }

    void update() {
        previous = current;
    }

    private boolean getButton(ControllerState state, int button) {
        if (button == BUTTON_A) return state.a;
        if (button == BUTTON_B) return state.b;
        if (button == BUTTON_X) return state.x;
        if (button == BUTTON_Y) return state.y;
        if (button == BUTTON_START) return state.start;
        if (button == BUTTON_SELECT) return state.back;
        if (button == BUTTON_LB) return state.lb;
        if (button == BUTTON_RB) return state.rb;
        if (button == BUTTON_LS) return state.leftStickClick;
        if (button == BUTTON_RS) return state.rightStickClick;
        if (button == BUTTON_DPAD_UP) return state.dpadUp;
        if (button == BUTTON_DPAD_RIGHT) return state.dpadRight;
        if (button == BUTTON_DPAD_DOWN) return state.dpadDown;
        if (button == BUTTON_DPAD_LEFT) return state.dpadLeft;
        return false;
    }

    public boolean buttonDown(int button) {
        return getButton(current, button);
    }

    public boolean buttonPressed(int button) {
        return getButton(current, button) && !getButton(previous, button);
    }

    public boolean buttonReleased(int button) {
        return !getButton(current, button) && getButton(previous, button);
    }

    public float axis(int axis) {
        if (axis == AXIS_LEFT_X) return current.leftStickX;
        if (axis == AXIS_LEFT_Y) return current.leftStickY;
        if (axis == AXIS_RIGHT_X) return current.rightStickX;
        if (axis == AXIS_RIGHT_Y) return current.rightStickY;
        if (axis == AXIS_LT) return current.leftTrigger;
        if (axis == AXIS_RT) return current.rightTrigger;
        return 0f;
    }

    private float previousAxis(int axis) {
        if (axis == AXIS_LEFT_X) return previous.leftStickX;
        if (axis == AXIS_LEFT_Y) return previous.leftStickY;
        if (axis == AXIS_RIGHT_X) return previous.rightStickX;
        if (axis == AXIS_RIGHT_Y) return previous.rightStickY;
        if (axis == AXIS_LT) return previous.leftTrigger;
        if (axis == AXIS_RT) return previous.rightTrigger;
        return 0f;
    }

    private boolean triggerDown(int axis) {
        return axis(axis) > TRIGGER_THRESHOLD;
    }

    private boolean triggerPressed(int axis) {
        return triggerDown(axis) && previousAxis(axis) <= TRIGGER_THRESHOLD;
    }

    private boolean triggerReleased(int axis) {
        return !triggerDown(axis) && previousAxis(axis) > TRIGGER_THRESHOLD;
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

