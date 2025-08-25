package com.absolutephoenix.game.scenes;

import com.absolutephoenix.engine.input.InputHandler;
import com.absolutephoenix.engine.rendering.SpriteBatch;
import com.absolutephoenix.engine.rendering.Texture;
import com.absolutephoenix.game.Scene;
import org.lwjgl.glfw.GLFW;

/**
 * Example scene demonstrating input handling and rendering.
 */
public class ExampleScene implements Scene {
    private final Texture texture;
    private final SpriteBatch batch;

    public ExampleScene(Texture texture, SpriteBatch batch) {
        this.texture = texture;
        this.batch = batch;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update() {
    }

    @Override
    public void input() {
        InputHandler input = InputHandler.get();
        if (input.keyboard.keyPressed(GLFW.GLFW_KEY_SPACE)) {
            System.out.println("Space pressed");
        }
        if (input.mouse.buttonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            System.out.println("Mouse clicked at " + input.mouse.getX() + ", " + input.mouse.getY());
        }
        if (input.mouse.getScrollY() != 0) {
            System.out.println("Scrolled: " + input.mouse.getScrollY());
        }
        if (input.controller.squarePressed()) {
            System.out.println("Square button pressed");
        }
        if (input.controller.crossPressed()) {
            System.out.println("Cross button pressed");
        }
        if (input.controller.startPressed()) {
            System.out.println("Start button pressed");
        }
        if (input.controller.selectPressed()) {
            System.out.println("Select button pressed");
        }
        if (input.controller.dpadUpPressed()) {
            System.out.println("DPad Up pressed");
        }
        if (input.controller.leftBumperPressed()) {
            System.out.println("Left bumper pressed");
        }
        if (input.controller.leftTriggerPressed()) {
            System.out.println("Left trigger value: " + input.controller.leftTrigger());
        }
        if (Math.abs(input.controller.leftStickX()) > 0.1f || Math.abs(input.controller.leftStickY()) > 0.1f) {
            System.out.println("Left stick: " + input.controller.leftStickX() + ", " + input.controller.leftStickY());
        }
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(texture, 100, 100, 128, 128);
        batch.end();
    }

    @Override
    public void cleanup() {
    }
}
