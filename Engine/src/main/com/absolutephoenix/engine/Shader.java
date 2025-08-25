package com.absolutephoenix.engine;

import org.lwjgl.opengl.GL20;

public class Shader {
    private final int programId;

    public Shader(String vertexSrc, String fragmentSrc) {
        int vertexId = compile(vertexSrc, GL20.GL_VERTEX_SHADER);
        int fragmentId = compile(fragmentSrc, GL20.GL_FRAGMENT_SHADER);
        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexId);
        GL20.glAttachShader(programId, fragmentId);
        GL20.glLinkProgram(programId);
        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Failed to link shader program: " + GL20.glGetProgramInfoLog(programId));
        }
        GL20.glDetachShader(programId, vertexId);
        GL20.glDetachShader(programId, fragmentId);
        GL20.glDeleteShader(vertexId);
        GL20.glDeleteShader(fragmentId);
    }

    private int compile(String src, int type) {
        int id = GL20.glCreateShader(type);
        GL20.glShaderSource(id, src);
        GL20.glCompileShader(id);
        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling shader: " + GL20.glGetShaderInfoLog(id));
        }
        return id;
    }

    public void bind() {
        GL20.glUseProgram(programId);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void delete() {
        GL20.glDeleteProgram(programId);
    }
}
