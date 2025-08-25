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
