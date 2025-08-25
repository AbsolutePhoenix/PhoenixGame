package com.absolutephoenix.engine.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 * Simple GLFW window wrapper.
 */
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

    /** Creates the window and OpenGL context. */
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

    /** Returns the native GLFW window handle. */
    public long getHandle() {
        return handle;
    }

    /** Returns true if the window should close. */
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    /** Polls pending window events. */
    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    /** Swaps the front and back buffers. */
    public void swapBuffers() {
        GLFW.glfwSwapBuffers(handle);
    }

    /** Destroys the window and terminates GLFW. */
    public void destroy() {
        if (handle != 0L) {
            Callbacks.glfwFreeCallbacks(handle);
            GLFW.glfwDestroyWindow(handle);
            handle = 0L;
        }
        GLFW.glfwTerminate();
        initialized = false;
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
