package com.absolutephoenix.game;

import com.absolutephoenix.engine.Engine;

public class Game {
    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.start();
        System.out.println("Game started.");
    }
}
