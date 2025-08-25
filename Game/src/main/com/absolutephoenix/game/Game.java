package com.absolutephoenix.game;

import com.absolutephoenix.engine.Engine;
import com.absolutephoenix.engine.GameLoop;
import com.absolutephoenix.engine.SpriteBatch;
import com.absolutephoenix.engine.Texture;
import com.absolutephoenix.game.scenes.ExampleScene;
import org.lwjgl.opengl.GL11;

public class Game implements GameLoop {
    private final int width;
    private final int height;
    private SpriteBatch batch;
    private Texture texture;
    private SceneManager sceneManager;

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void initialize() {
        batch = new SpriteBatch(width, height);
        texture = new Texture("/white.png");
        sceneManager = new SceneManager();
        sceneManager.addScene("main", new ExampleScene(texture));
        sceneManager.setScene("main");
    }

    @Override
    public void update() {
        sceneManager.update();
    }

    @Override
    public void input() {
        sceneManager.input();
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sceneManager.render(batch);
        batch.end();
    }

    @Override
    public void cleanup() {
        sceneManager.cleanup();
        batch.delete();
        texture.delete();
    }

    public static void main(String[] args) {
        Game game = new Game(800, 600);
        Engine engine = new Engine(game, 800, 600, "Phoenix Game", true);
        engine.setTargetUPS(60);
        engine.setTargetFPS(60);
        engine.start();
    }
}
