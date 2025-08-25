package com.absolutephoenix.game;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages game scenes. Scenes remain in the manager after they are
 * deactivated and can be reactivated later unless they are explicitly
 * removed. Use {@link #removeScene(String)} or
 * {@link #setScene(String, boolean)} with {@code true} to discard scenes
 * that are no longer needed.
 */
public class SceneManager {
    private final Map<String, Scene> scenes = new HashMap<>();
    private Scene currentScene;
    private String currentSceneName;

    public void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public void removeScene(String name) {
        if (name != null && name.equals(currentSceneName)) {
            if (currentScene != null) {
                currentScene.cleanup();
            }
            currentScene = null;
            currentSceneName = null;
        }
        scenes.remove(name);
    }

    public void setScene(String name) {
        setScene(name, false);
    }

    public void setScene(String name, boolean removePrevious) {
        String previousSceneName = currentSceneName;
        if (currentScene != null) {
            currentScene.cleanup();
            if (removePrevious && previousSceneName != null) {
                scenes.remove(previousSceneName);
            }
        }
        currentScene = scenes.get(name);
        currentSceneName = name;
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
