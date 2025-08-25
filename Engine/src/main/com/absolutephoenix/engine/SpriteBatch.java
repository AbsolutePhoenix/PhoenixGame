package com.absolutephoenix.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public class SpriteBatch {
    private static final int MAX_SPRITES = 1000;
    private static final int VERTEX_SIZE = 4; // x, y, u, v
    private static final int VERTEX_BYTES = VERTEX_SIZE * Float.BYTES;
    private static final int SPRITE_SIZE = 4 * VERTEX_SIZE;

    private final FloatBuffer buffer;
    private final int vao;
    private final int vbo;
    private final int ebo;
    private final Shader shader;
    private final int windowWidth;
    private final int windowHeight;

    private int sprites;
    private Texture lastTexture;

    private static final String VERT_SRC = "#version 330 core\n" +
            "layout (location = 0) in vec2 aPos;\n" +
            "layout (location = 1) in vec2 aTex;\n" +
            "out vec2 vTex;\n" +
            "void main() {\n" +
            "    vTex = aTex;\n" +
            "    gl_Position = vec4(aPos, 0.0, 1.0);\n" +
            "}";

    private static final String FRAG_SRC = "#version 330 core\n" +
            "in vec2 vTex;\n" +
            "out vec4 fragColor;\n" +
            "uniform sampler2D uTexture;\n" +
            "void main() {\n" +
            "    fragColor = texture(uTexture, vTex);\n" +
            "}";

    public SpriteBatch(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        buffer = BufferUtils.createFloatBuffer(MAX_SPRITES * SPRITE_SIZE);
        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();
        ebo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, MAX_SPRITES * SPRITE_SIZE * Float.BYTES, GL15.GL_DYNAMIC_DRAW);

        int[] indices = new int[MAX_SPRITES * 6];
        int offset = 0;
        for (int i = 0; i < indices.length; i += 6) {
            indices[i] = offset;
            indices[i + 1] = offset + 1;
            indices[i + 2] = offset + 2;
            indices[i + 3] = offset + 2;
            indices[i + 4] = offset + 3;
            indices[i + 5] = offset;
            offset += 4;
        }
        java.nio.IntBuffer indexBuffer = org.lwjgl.BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, VERTEX_BYTES, 0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, VERTEX_BYTES, 2 * Float.BYTES);

        GL30.glBindVertexArray(0);

        shader = new Shader(VERT_SRC, FRAG_SRC);
    }

    public void begin() {
        sprites = 0;
        buffer.clear();
        lastTexture = null;
    }

    public void draw(Texture texture, float x, float y, float width, float height) {
        if (lastTexture != null && lastTexture != texture) {
            flush();
        }
        if (sprites >= MAX_SPRITES) {
            flush();
        }
        lastTexture = texture;

        float x0 = (x / windowWidth) * 2f - 1f;
        float y0 = (y / windowHeight) * 2f - 1f;
        float x1 = ((x + width) / windowWidth) * 2f - 1f;
        float y1 = ((y + height) / windowHeight) * 2f - 1f;

        buffer.put(x0).put(y0).put(0f).put(0f);
        buffer.put(x1).put(y0).put(1f).put(0f);
        buffer.put(x1).put(y1).put(1f).put(1f);
        buffer.put(x0).put(y1).put(0f).put(1f);
        sprites++;
    }

    private void flush() {
        if (sprites == 0) return;
        buffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
        shader.bind();
        if (lastTexture != null) {
            lastTexture.bind();
        }
        GL30.glBindVertexArray(vao);
        GL11.glDrawElements(GL11.GL_TRIANGLES, sprites * 6, GL11.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);
        shader.unbind();
        buffer.clear();
        sprites = 0;
    }

    public void end() {
        flush();
    }

    public void delete() {
        shader.delete();
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(ebo);
        GL30.glDeleteVertexArrays(vao);
    }
}
