package com.absolutephoenix.game.scenes;

import com.absolutephoenix.engine.input.InputHandler;
import java.util.Optional;
import com.absolutephoenix.engine.rendering.SpriteBatch;
import com.absolutephoenix.engine.rendering.Texture;
import com.absolutephoenix.game.Scene;
import org.lwjgl.glfw.GLFW;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example scene demonstrating input handling and rendering.
 */
public class ExampleScene implements Scene {
    private static final Logger LOGGER = Logger.getLogger(ExampleScene.class.getName());

    static {
        boolean enableLogs = Boolean.parseBoolean(System.getProperty("exampleScene.logging", "true"));
        LOGGER.setLevel(enableLogs ? Level.INFO : Level.OFF);
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
            LOGGER.info("Space pressed");
        }
        if (input.mouse.buttonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            LOGGER.info("Mouse clicked at " + input.mouse.getX() + ", " + input.mouse.getY());
        }
        if (input.mouse.getScrollY() != 0) {
            LOGGER.info("Scrolled: " + input.mouse.getScrollY());
        }
        if (input.controller.squarePressed()) {
            LOGGER.info("Square button pressed");
        }
        if (input.controller.crossPressed()) {
            LOGGER.info("Cross button pressed");
        }
        if (input.controller.circlePressed()) {
            LOGGER.info("Circle button pressed");
        }
        if (input.controller.trianglePressed()) {
            LOGGER.info("Triangle button pressed");
        }
        if (input.controller.startPressed()) {
            LOGGER.info("Start button pressed");
        }
        if (input.controller.selectPressed()) {
            LOGGER.info("Select button pressed");
        }
        if (input.controller.dpadUpPressed()) {
            LOGGER.info("DPad Up pressed");
        }
        if (input.controller.dpadDownPressed()) {
            LOGGER.info("DPad Down pressed");
        }
        if (input.controller.dpadLeftPressed()) {
            LOGGER.info("DPad Left pressed");
        }
        if (input.controller.dpadRightPressed()) {
            LOGGER.info("DPad Right pressed");
        }
        if (input.controller.leftBumperPressed()) {
            LOGGER.info("Left bumper pressed");
        }
        if (input.controller.rightBumperPressed()) {
            LOGGER.info("Right bumper pressed");
        }
        if (input.controller.leftStickPressed()) {
            LOGGER.info("Left stick pressed");
        }
        if (input.controller.rightStickPressed()) {
            LOGGER.info("Right stick pressed");
        }

        float TRIGGER_DEADZONE = 0.05f; // tweak as needed

        float lt = input.controller.leftTrigger();
        float rt = input.controller.rightTrigger();

        if (lt > TRIGGER_DEADZONE) {
            LOGGER.info("Left trigger: " + lt);
        }

        if (rt > TRIGGER_DEADZONE) {
            LOGGER.info("Right trigger: " + rt);
        }

        float lx = input.controller.leftStickX();
        float ly = input.controller.leftStickY();
        float rx = input.controller.rightStickX();
        float ry = input.controller.rightStickY();

        float DEADZONE = 0.05f;

        // Left stick
        float leftMag = (float)Math.sqrt(lx * lx + ly * ly);
        if (leftMag > DEADZONE) {
            LOGGER.info("Left stick: " + lx + ", " + ly);
        }

        // Right stick
        float rightMag = (float)Math.sqrt(rx * rx + ry * ry);
        if (rightMag > DEADZONE) {
            LOGGER.info("Right stick: " + rx + ", " + ry);
        }

        if (input.controller.psTrackpadPressed()) LOGGER.info("TrackPad PRESSED");
        if (input.controller.psMutePressed())     LOGGER.info("Mute PRESSED");
        if (input.controller.guidePressed())      LOGGER.info("Guide PRESSED");

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
