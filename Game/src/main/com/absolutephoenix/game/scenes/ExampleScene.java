package com.absolutephoenix.game.scenes;

import com.absolutephoenix.engine.input.InputHandler;
import java.util.Optional;
import com.absolutephoenix.engine.rendering.SpriteBatch;
import com.absolutephoenix.engine.rendering.Texture;
import com.absolutephoenix.game.Scene;
import org.lwjgl.glfw.GLFW;
import com.absolutephoenix.engine.logging.LogLevel;
import com.absolutephoenix.engine.logging.Logger;

/**
 * Example scene demonstrating input handling and rendering.
 */
public class ExampleScene implements Scene {
    static {
        boolean enableLogs = Boolean.parseBoolean(System.getProperty("exampleScene.logging", "true"));
        Logger.setLevel(enableLogs ? LogLevel.INFO : LogLevel.OFF);
    }

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
            Logger.info("Space pressed");
        }
        if (input.mouse.buttonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            Logger.info("Mouse clicked at " + input.mouse.getX() + ", " + input.mouse.getY());
        }
        if (input.mouse.getScrollY() != 0) {
            Logger.info("Scrolled: " + input.mouse.getScrollY());
        }
        if (input.controller.squarePressed()) {
            Logger.info("Square button pressed");
        }
        if (input.controller.crossPressed()) {
            Logger.info("Cross button pressed");
        }
        if (input.controller.circlePressed()) {
            Logger.info("Circle button pressed");
        }
        if (input.controller.trianglePressed()) {
            Logger.info("Triangle button pressed");
        }
        if (input.controller.startPressed()) {
            Logger.info("Start button pressed");
        }
        if (input.controller.selectPressed()) {
            Logger.info("Select button pressed");
        }
        if (input.controller.dpadUpPressed()) {
            Logger.info("DPad Up pressed");
        }
        if (input.controller.dpadDownPressed()) {
            Logger.info("DPad Down pressed");
        }
        if (input.controller.dpadLeftPressed()) {
            Logger.info("DPad Left pressed");
        }
        if (input.controller.dpadRightPressed()) {
            Logger.info("DPad Right pressed");
        }
        if (input.controller.leftBumperPressed()) {
            Logger.info("Left bumper pressed");
        }
        if (input.controller.rightBumperPressed()) {
            Logger.info("Right bumper pressed");
        }
        if (input.controller.leftStickPressed()) {
            Logger.info("Left stick pressed");
        }
        if (input.controller.rightStickPressed()) {
            Logger.info("Right stick pressed");
        }

        float TRIGGER_DEADZONE = 0.05f; // tweak as needed

        float lt = input.controller.leftTrigger();
        float rt = input.controller.rightTrigger();

        if (lt > TRIGGER_DEADZONE) {
            Logger.info("Left trigger: " + lt);
        }

        if (rt > TRIGGER_DEADZONE) {
            Logger.info("Right trigger: " + rt);
        }

        float lx = input.controller.leftStickX();
        float ly = input.controller.leftStickY();
        float rx = input.controller.rightStickX();
        float ry = input.controller.rightStickY();

        float DEADZONE = 0.05f;

        // Left stick
        float leftMag = (float)Math.sqrt(lx * lx + ly * ly);
        if (leftMag > DEADZONE) {
            Logger.info("Left stick: " + lx + ", " + ly);
        }

        // Right stick
        float rightMag = (float)Math.sqrt(rx * rx + ry * ry);
        if (rightMag > DEADZONE) {
            Logger.info("Right stick: " + rx + ", " + ry);
        }

        if (input.controller.psTrackpadPressed()) Logger.info("TrackPad PRESSED");
        if (input.controller.psMutePressed())     Logger.info("Mute PRESSED");
        if (input.controller.guidePressed())      Logger.info("Guide PRESSED");

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
