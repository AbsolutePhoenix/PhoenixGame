package com.absolutephoenix.engine.core;

/**
 * Defines the callbacks required by the {@link Engine} to run a game.
 */
public interface GameLoop {
    /** Called once on startup. */
    void initialize();

    /** Called on each update tick to advance game state. */
    void update();

    /** Handles user input for the current frame. */
    void input();

    /** Renders the current frame. */
    void render();

    /** Called when the game is shutting down for cleanup. */
    void cleanup();
}
