package com.absolutephoenix.game.scenes;

import com.absolutephoenix.engine.input.InputHandler;
import java.util.Optional;
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
        Optional<InputHandler> maybeInput = InputHandler.get();
        if (maybeInput.isEmpty()) {
            return;
        }
        InputHandler input = maybeInput.get();
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
        if (input.controller.circlePressed()) {
            System.out.println("Circle button pressed");
        }
        if (input.controller.trianglePressed()) {
            System.out.println("Triangle button pressed");
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
        if (input.controller.dpadDownPressed()) {
            System.out.println("DPad Down pressed");
        }
        if (input.controller.dpadLeftPressed()) {
            System.out.println("DPad Left pressed");
        }
        if (input.controller.dpadRightPressed()) {
            System.out.println("DPad Right pressed");
        }
        if (input.controller.leftBumperPressed()) {
            System.out.println("Left bumper pressed");
        }
        if (input.controller.rightBumperPressed()) {
            System.out.println("Right bumper pressed");
        }
        if (input.controller.leftStickPressed()) {
            System.out.println("Left stick pressed");
        }
        if (input.controller.rightStickPressed()) {
            System.out.println("Right stick pressed");
        }

        float TRIGGER_DEADZONE = 0.05f; // tweak as needed

        float lt = input.controller.leftTrigger();
        float rt = input.controller.rightTrigger();

        if (lt > TRIGGER_DEADZONE) {
            System.out.println("Left trigger: " + lt);
        }

        if (rt > TRIGGER_DEADZONE) {
            System.out.println("Right trigger: " + rt);
        }

        float lx = input.controller.leftStickX();
        float ly = input.controller.leftStickY();
        float rx = input.controller.rightStickX();
        float ry = input.controller.rightStickY();

        float DEADZONE = 0.05f;

        // Left stick
        float leftMag = (float)Math.sqrt(lx * lx + ly * ly);
        if (leftMag > DEADZONE) {
            System.out.println("Left stick: " + lx + ", " + ly);
        }

        // Right stick
        float rightMag = (float)Math.sqrt(rx * rx + ry * ry);
        if (rightMag > DEADZONE) {
            System.out.println("Right stick: " + rx + ", " + ry);
        }

        if (input.controller.psTrackpadPressed()) System.out.println("TrackPad PRESSED");
        if (input.controller.psMutePressed())     System.out.println("Mute PRESSED");
        if (input.controller.guidePressed())      System.out.println("Guide PRESSED");

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
