package com.absolutephoenix.game;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private final Map<String, Scene> scenes = new HashMap<>();
    private Scene currentScene;

    public void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public void setScene(String name) {
        if (currentScene != null) {
            currentScene.cleanup();
        }
        currentScene = scenes.get(name);
        if (currentScene != null) {
            currentScene.initialize();
        }
    }

    public void update() {
        if (currentScene != null) {
            currentScene.update();
        }
    }

    public void input() {
        if (currentScene != null) {
            currentScene.input();
        }
    }

    public void render() {
        if (currentScene != null) {
            currentScene.render();
        }
    }

    public void cleanup() {
        if (currentScene != null) {
            currentScene.cleanup();
        }
    }
}
