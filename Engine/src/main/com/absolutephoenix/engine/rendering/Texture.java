package com.absolutephoenix.engine.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Represents an OpenGL texture loaded from the classpath.
 */
public class Texture {
    private final int id;
    private final int width;
    private final int height;

    public Texture(String resource) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer imageBuffer;
            try (InputStream is = Texture.class.getResourceAsStream(resource)) {
                if (is == null) {
                    throw new RuntimeException("Resource not found: " + resource);
                }
                byte[] data = is.readAllBytes();
                imageBuffer = BufferUtils.createByteBuffer(data.length);
                imageBuffer.put(data);
                imageBuffer.flip();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);
            STBImage.stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());
            }
            width = w.get(0);
            height = h.get(0);
            id = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            STBImage.stbi_image_free(image);
        }
    }

    public void bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public void delete() {
        GL11.glDeleteTextures(id);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
