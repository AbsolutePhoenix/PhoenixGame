package com.absolutephoenix.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {
    private long handle;
    private final int width;
    private final int height;
    private final String title;
    private boolean initialized;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        handle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (handle == 0L) {
            throw new RuntimeException("Failed to create window");
        }
        GLFW.glfwMakeContextCurrent(handle);
        GL.createCapabilities();
        GL11.glClearColor(0f, 0f, 0f, 1f);
        GLFW.glfwShowWindow(handle);
        initialized = true;
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void update() {
        GLFW.glfwSwapBuffers(handle);
        GLFW.glfwPollEvents();
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(handle, title);
    }

    public void setVSync(boolean vsync) {
        GLFW.glfwSwapInterval(vsync ? 1 : 0);
    }

    public boolean isInitialized() {
        return initialized;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
