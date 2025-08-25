package com.absolutephoenix.engine.input;

import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import static org.junit.jupiter.api.Assertions.*;

public class KeyboardTest {
    @Test
    void keyPressAndReleaseFlowWorks() {
        Keyboard keyboard = new Keyboard();
        int key = GLFW.GLFW_KEY_A;

        // Initial state
        assertFalse(keyboard.keyDown(key));
        assertFalse(keyboard.keyPressed(key));
        assertFalse(keyboard.keyReleased(key));

        // Press key
        keyboard.handle(key, GLFW.GLFW_PRESS);
        assertTrue(keyboard.keyDown(key));
        assertTrue(keyboard.keyPressed(key));
        assertFalse(keyboard.keyReleased(key));

        // Advance frame
        keyboard.update();
        assertTrue(keyboard.keyDown(key));
        assertFalse(keyboard.keyPressed(key));
        assertFalse(keyboard.keyReleased(key));

        // Release key
        keyboard.handle(key, GLFW.GLFW_RELEASE);
        assertFalse(keyboard.keyDown(key));
        assertFalse(keyboard.keyPressed(key));
        assertTrue(keyboard.keyReleased(key));

        // Final update
        keyboard.update();
        assertFalse(keyboard.keyDown(key));
        assertFalse(keyboard.keyPressed(key));
        assertFalse(keyboard.keyReleased(key));
    }
}
