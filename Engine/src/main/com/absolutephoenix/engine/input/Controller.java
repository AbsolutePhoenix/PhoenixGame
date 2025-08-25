package com.absolutephoenix.engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Polls controller/gamepad state using GLFW with standardized + raw extras. */
public class Controller {
    private static final int JID = GLFW.GLFW_JOYSTICK_1;

    private static final int MAX_BUTTONS = 32; // storage for standardized or fallback raw
    private static final int MAX_AXES    = 8;  // allow a bit more room
    private static final float TRIGGER_THRESHOLD = 0.5f;

    // Standardized (or fallback) state
    private final boolean[] current = new boolean[MAX_BUTTONS];
    private final boolean[] previous = new boolean[MAX_BUTTONS];
    private final float[]   axesCurrent = new float[MAX_AXES];
    private final float[]   axesPrevious = new float[MAX_AXES];

    // Reusable state buffer to avoid per-frame allocations
    private final GLFWGamepadState state = GLFWGamepadState.create();

    // Raw state (for extra buttons like PS5 trackpad/mute)
    private final boolean[] rawCurrent = new boolean[MAX_BUTTONS];
    private final boolean[] rawPrevious = new boolean[MAX_BUTTONS];

    // Identity
    private String joystickName = null;
    private String gamepadName  = null;
    private String guid         = null;
    private boolean standardized = false;

    // Common button constants (GLFW standardized layout)
    public static final int BUTTON_A = GLFW.GLFW_GAMEPAD_BUTTON_A;         // Cross on PS
    public static final int BUTTON_B = GLFW.GLFW_GAMEPAD_BUTTON_B;         // Circle on PS
    public static final int BUTTON_X = GLFW.GLFW_GAMEPAD_BUTTON_X;         // Square on PS
    public static final int BUTTON_Y = GLFW.GLFW_GAMEPAD_BUTTON_Y;         // Triangle on PS
    public static final int BUTTON_START  = GLFW.GLFW_GAMEPAD_BUTTON_START;   // Options on PS
    public static final int BUTTON_SELECT = GLFW.GLFW_GAMEPAD_BUTTON_BACK;    // Create on PS
    public static final int BUTTON_LB = GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER;
    public static final int BUTTON_RB = GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER;
    public static final int BUTTON_LS = GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB;
    public static final int BUTTON_RS = GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB;
    public static final int BUTTON_DPAD_UP    = GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP;
    public static final int BUTTON_DPAD_RIGHT = GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT;
    public static final int BUTTON_DPAD_DOWN  = GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN;
    public static final int BUTTON_DPAD_LEFT  = GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT;

    public static final int BUTTON_GUIDE      = GLFW.GLFW_GAMEPAD_BUTTON_GUIDE; // PS / Xbox guide in GLFW’s standard set

    // Axis constants (GLFW standardized)
    public static final int AXIS_LEFT_X  = GLFW.GLFW_GAMEPAD_AXIS_LEFT_X;
    public static final int AXIS_LEFT_Y  = GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y;
    public static final int AXIS_RIGHT_X = GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X;
    public static final int AXIS_RIGHT_Y = GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;
    public static final int AXIS_LT      = GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER;   // 0..1
    public static final int AXIS_RT      = GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER;  // 0..1

    public boolean isPlayStationLike() {
        String n = (gamepadName != null) ? gamepadName : joystickName;
        if (n == null) return false;
        n = n.toLowerCase();
        return n.contains("dualsense") || n.contains("playstation") || n.contains("sony") || n.contains("wireless controller") || n.contains("ps4") || n.contains("ps5");
    }
    public boolean isXboxLike() {
        String n = (gamepadName != null) ? gamepadName : joystickName;
        if (n == null) return false;
        n = n.toLowerCase();
        return n.contains("xbox") || n.contains("xinput");
    }

    // --- FRAME FLOW ---
    // Call this at the **start** of each frame, before poll()
    public void update() {
        System.arraycopy(current,    0, previous,    0, current.length);
        System.arraycopy(axesCurrent,0, axesPrevious,0, axesCurrent.length);
        System.arraycopy(rawCurrent, 0, rawPrevious, 0, rawCurrent.length);
    }

    // Call this after update() each frame
    public synchronized void poll() {

        if (!GLFW.glfwJoystickPresent(JID)) {
            // Clear states if absent
            clear(current); clear(rawCurrent);
            clear(axesCurrent);
            joystickName = gamepadName = guid = null;
            standardized = false;
            return;
        }

        // Identity
        joystickName = GLFW.glfwGetJoystickName(JID);
        guid         = GLFW.glfwGetJoystickGUID(JID);
        standardized = GLFW.glfwJoystickIsGamepad(JID);
        gamepadName  = standardized ? GLFW.glfwGetGamepadName(JID) : null;

        // Always read RAW so we can catch extras (PS5 trackpad/mute) or unknown pads
        ByteBuffer rawButtons = GLFW.glfwGetJoystickButtons(JID);
        if (rawButtons != null) {
            int limit = Math.min(rawButtons.limit(), MAX_BUTTONS);
            for (int i = 0; i < limit; i++) rawCurrent[i] = rawButtons.get(i) == GLFW.GLFW_PRESS;
            for (int i = limit; i < MAX_BUTTONS; i++) rawCurrent[i] = false;
        } else {
            clear(rawCurrent);
        }

        // Prefer standardized mapping if available
        if (standardized) {
            if (GLFW.glfwGetGamepadState(JID, state)) {
                ByteBuffer stdButtons = state.buttons();
                int blim = Math.min(stdButtons.limit(), MAX_BUTTONS);
                for (int i = 0; i < blim; i++) current[i] = stdButtons.get(i) == GLFW.GLFW_PRESS;
                for (int i = blim; i < MAX_BUTTONS; i++) current[i] = false;

                FloatBuffer stdAxes = state.axes();
                int alim = Math.min(stdAxes.limit(), MAX_AXES);
                for (int i = 0; i < alim; i++) axesCurrent[i] = stdAxes.get(i); // overwrite with standardized
                for (int i = alim; i < MAX_AXES; i++) axesCurrent[i] = 0f;

                axesCurrent[AXIS_LT] = normalizeTriggerPossiblySigned(axesCurrent[AXIS_LT]);
                axesCurrent[AXIS_RT] = normalizeTriggerPossiblySigned(axesCurrent[AXIS_RT]);
            } else {
                // If standardized read failed, fall back to raw buttons already copied above
                fallbackReadRawAxesAndButtons();
            }
        } else {
            // No standardized layout → use raw directly
            fallbackReadRawAxesAndButtons();
        }
    }

    private void fallbackReadRawAxesAndButtons() {
        // Buttons already in rawCurrent → mirror into current
        System.arraycopy(rawCurrent, 0, current, 0, MAX_BUTTONS);

        // Read raw axes then normalize LT/RT to 0..1 for consistency
        FloatBuffer rawAxes = GLFW.glfwGetJoystickAxes(JID);
        if (rawAxes != null) {
            int limit = Math.min(rawAxes.limit(), MAX_AXES);
            for (int i = 0; i < limit; i++) axesCurrent[i] = rawAxes.get(i);
            for (int i = limit; i < MAX_AXES; i++) axesCurrent[i] = 0f;
        } else {
            clear(axesCurrent);
        }

        // Normalize triggers if we’re not standardized (common raw range: -1..1)
        axesCurrent[AXIS_LT] = normalizeTriggerRaw(axesCurrent[AXIS_LT]);
        axesCurrent[AXIS_RT] = normalizeTriggerRaw(axesCurrent[AXIS_RT]);
    }

    private static float normalizeTriggerPossiblySigned(float v) {
        // If already 0..1 leave it; else map -1..1 → 0..1
        if (v >= 0f && v <= 1f) return v;
        float nv = (v + 1f) * 0.5f;
        return (nv < 0f) ? 0f : (nv > 1f ? 1f : nv);
    }

    private static float normalizeTriggerRaw(float v) {
        // Converts -1..1 to 0..1 (clamped)
        float nv = (v + 1f) * 0.5f;
        if (nv < 0f) nv = 0f;
        if (nv > 1f) nv = 1f;
        return nv;
    }

    // --- STANDARDIZED ACCESSORS ---
    public boolean buttonDown(int button) {
        return (button >= 0 && button < current.length) && current[button];
    }
    public boolean buttonPressed(int button) {
        return (button >= 0 && button < current.length) && current[button] && !previous[button];
    }
    public boolean buttonReleased(int button) {
        return (button >= 0 && button < current.length) && !current[button] && previous[button];
    }

    public float axis(int axis) {
        return (axis >= 0 && axis < axesCurrent.length) ? axesCurrent[axis] : 0f;
    }
    public boolean leftTriggerDown()    { return axis(AXIS_LT) > TRIGGER_THRESHOLD; }
    public boolean rightTriggerDown()   { return axis(AXIS_RT) > TRIGGER_THRESHOLD; }
    public boolean leftTriggerPressed() { return leftTriggerDown()  && axesPrevious[AXIS_LT] <= TRIGGER_THRESHOLD; }
    public boolean rightTriggerPressed(){ return rightTriggerDown() && axesPrevious[AXIS_RT] <= TRIGGER_THRESHOLD; }
    public boolean leftTriggerReleased(){ return !leftTriggerDown() && axesPrevious[AXIS_LT] >  TRIGGER_THRESHOLD; }
    public boolean rightTriggerReleased(){return !rightTriggerDown()&& axesPrevious[AXIS_RT] >  TRIGGER_THRESHOLD; }

    // --- RAW ACCESS (for extras) ---
    public boolean rawButtonDown(int rawIndex) {
        return (rawIndex >= 0 && rawIndex < rawCurrent.length) && rawCurrent[rawIndex];
    }
    public boolean rawButtonPressed(int rawIndex) {
        return (rawIndex >= 0 && rawIndex < rawCurrent.length) && rawCurrent[rawIndex] && !rawPrevious[rawIndex];
    }
    public boolean rawButtonReleased(int rawIndex) {
        return (rawIndex >= 0 && rawIndex < rawCurrent.length) && !rawCurrent[rawIndex] && rawPrevious[rawIndex];
    }

    // --- PlayStation-style convenience (fixed aliases) ---
    // Cross = A(0), Circle = B(1), Square = X(2), Triangle = Y(3)
    public boolean crossDown()     { return buttonDown(BUTTON_A); }
    public boolean crossPressed()  { return buttonPressed(BUTTON_A); }
    public boolean crossReleased() { return buttonReleased(BUTTON_A); }

    public boolean circleDown()     { return buttonDown(BUTTON_B); }
    public boolean circlePressed()  { return buttonPressed(BUTTON_B); }
    public boolean circleReleased() { return buttonReleased(BUTTON_B); }

    public boolean squareDown()     { return buttonDown(BUTTON_X); }
    public boolean squarePressed()  { return buttonPressed(BUTTON_X); }
    public boolean squareReleased() { return buttonReleased(BUTTON_X); }

    public boolean triangleDown()     { return buttonDown(BUTTON_Y); }
    public boolean trianglePressed()  { return buttonPressed(BUTTON_Y); }
    public boolean triangleReleased() { return buttonReleased(BUTTON_Y); }

    // Start/Select (Options/Create)
    public boolean startDown()     { return buttonDown(BUTTON_START); }
    public boolean startPressed()  { return buttonPressed(BUTTON_START); }
    public boolean startReleased() { return buttonReleased(BUTTON_START); }

    public boolean selectDown()     { return buttonDown(BUTTON_SELECT); }
    public boolean selectPressed()  { return buttonPressed(BUTTON_SELECT); }
    public boolean selectReleased() { return buttonReleased(BUTTON_SELECT); }

    // Bumpers
    public boolean leftBumperDown()     { return buttonDown(BUTTON_LB); }
    public boolean leftBumperPressed()  { return buttonPressed(BUTTON_LB); }
    public boolean leftBumperReleased() { return buttonReleased(BUTTON_LB); }

    public boolean rightBumperDown()     { return buttonDown(BUTTON_RB); }
    public boolean rightBumperPressed()  { return buttonPressed(BUTTON_RB); }
    public boolean rightBumperReleased() { return buttonReleased(BUTTON_RB); }

    // Sticks
    public boolean leftStickDown()     { return buttonDown(BUTTON_LS); }
    public boolean leftStickPressed()  { return buttonPressed(BUTTON_LS); }
    public boolean leftStickReleased() { return buttonReleased(BUTTON_LS); }

    public boolean rightStickDown()     { return buttonDown(BUTTON_RS); }
    public boolean rightStickPressed()  { return buttonPressed(BUTTON_RS); }
    public boolean rightStickReleased() { return buttonReleased(BUTTON_RS); }

    // D-pad
    public boolean dpadUpDown()     { return buttonDown(BUTTON_DPAD_UP); }
    public boolean dpadUpPressed()  { return buttonPressed(BUTTON_DPAD_UP); }
    public boolean dpadUpReleased() { return buttonReleased(BUTTON_DPAD_UP); }

    public boolean dpadRightDown()     { return buttonDown(BUTTON_DPAD_RIGHT); }
    public boolean dpadRightPressed()  { return buttonPressed(BUTTON_DPAD_RIGHT); }
    public boolean dpadRightReleased() { return buttonReleased(BUTTON_DPAD_RIGHT); }

    public boolean dpadDownDown()     { return buttonDown(BUTTON_DPAD_DOWN); }
    public boolean dpadDownPressed()  { return buttonPressed(BUTTON_DPAD_DOWN); }
    public boolean dpadDownReleased() { return buttonReleased(BUTTON_DPAD_DOWN); }

    public boolean dpadLeftDown()     { return buttonDown(BUTTON_DPAD_LEFT); }
    public boolean dpadLeftPressed()  { return buttonPressed(BUTTON_DPAD_LEFT); }
    public boolean dpadLeftReleased() { return buttonReleased(BUTTON_DPAD_LEFT); }

    public boolean guideDown()     { return buttonDown(BUTTON_GUIDE); }
    public boolean guidePressed()  { return buttonPressed(BUTTON_GUIDE); }
    public boolean guideReleased() { return buttonReleased(BUTTON_GUIDE); }

    // Axes convenience
    public float leftStickX()  { return axis(AXIS_LEFT_X); }
    public float leftStickY()  { return axis(AXIS_LEFT_Y); }
    public float rightStickX() { return axis(AXIS_RIGHT_X); }
    public float rightStickY() { return axis(AXIS_RIGHT_Y); }
    public float leftTrigger() { return axis(AXIS_LT); }
    public float rightTrigger(){ return axis(AXIS_RT); }

    // Trackpad / Mute convenience using registry resolution
    public boolean psTrackpadDown()     { Integer idx = mapIndex(MappingRegistry.Logical.PS_TRACKPAD); return idx != null && rawButtonDown(idx); }
    public boolean psTrackpadPressed()  { Integer idx = mapIndex(MappingRegistry.Logical.PS_TRACKPAD); return idx != null && rawButtonPressed(idx); }
    public boolean psTrackpadReleased() { Integer idx = mapIndex(MappingRegistry.Logical.PS_TRACKPAD); return idx != null && rawButtonReleased(idx); }

    public boolean psMuteDown()         { Integer idx = mapIndex(MappingRegistry.Logical.PS_MUTE); return idx != null && rawButtonDown(idx); }
    public boolean psMutePressed()      { Integer idx = mapIndex(MappingRegistry.Logical.PS_MUTE); return idx != null && rawButtonPressed(idx); }
    public boolean psMuteReleased()     { Integer idx = mapIndex(MappingRegistry.Logical.PS_MUTE); return idx != null && rawButtonReleased(idx); }

    // Optional: one-shot learn helpers for your input-binding UI
    public void learnOnceTrackpad() { int i = findFirstNewlyPressedRaw(); if (i >= 0) MappingRegistry.learn(identityKey(), MappingRegistry.Logical.PS_TRACKPAD, i); }
    public void learnOnceMute()     { int i = findFirstNewlyPressedRaw(); if (i >= 0) MappingRegistry.learn(identityKey(), MappingRegistry.Logical.PS_MUTE, i); }

    public int firstButtonDownIndex() {
        for (int i = 0; i < current.length; i++) if (current[i]) return i;
        return -1;
    }

    private int findFirstNewlyPressedRaw() {
        for (int i = 0; i < rawCurrent.length; i++) {
            if (rawCurrent[i] && !rawPrevious[i]) return i;
        }
        return -1;
    }

    private Integer mapIndex(String logicalControl) {
        return MappingRegistry.resolve(identityKey(), normalizedName(), logicalControl);
    }

    private String identityKey() {
        if (guid != null && !guid.isEmpty()) return "guid:" + guid.toLowerCase();
        String n = (gamepadName != null ? gamepadName : joystickName);
        if (n == null) n = "unknown";
        return "name:" + normalizeName(n);
    }
    private String normalizedName() {
        String n1 = (gamepadName  != null ? gamepadName  : "");
        String n2 = (joystickName != null ? joystickName : "");
        return normalizeName((n1 + " " + n2).trim());
    }
    private static String normalizeName(String n) {
        return n.toLowerCase().replaceAll("[^a-z0-9]+", " ").trim();
    }

    private static void clear(boolean[] arr) { for (int i = 0; i < arr.length; i++) arr[i] = false; }
    private static void clear(float[] arr)   { for (int i = 0; i < arr.length; i++) arr[i] = 0f; }

}
