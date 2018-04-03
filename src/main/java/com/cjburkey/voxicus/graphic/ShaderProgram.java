package com.cjburkey.voxicus.graphic;

import static org.lwjgl.opengl.GL20.*;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import com.cjburkey.voxicus.core.Debug;

public abstract class ShaderProgram {
	
	private final int program;
	private final Map<Integer, Integer> shaders = new HashMap<>(); // Key of map is only used to verify that only <= 1 shader of each type is used.
	private final Map<String, Integer> uniforms = new HashMap<>();
	
	protected ShaderProgram() {
		program = glCreateProgram();
		if (program == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create shader program");
		}
	}
	
	protected final boolean addShader(int type, String source) {
		if (source == null || source.trim().isEmpty()) {
			Debug.error("Failed to add shader to shader program: the input source was null or empty");
			return false;
		}
		int shader = glCreateShader(type);
		glShaderSource(shader, source);
		glCompileShader(shader);
		int[] len = new int[] { -1 };
		glGetShaderiv(shader, GL_INFO_LOG_LENGTH, len);
		if (len[0] > 0) {
			Debug.error("Failed to compile shader: {}", glGetShaderInfoLog(shader, len[0]));
			return false;
		}
		shaders.put(type, shader);
		return true;
	}
	
	protected final boolean link() {
		for (Entry<Integer, Integer> entry : shaders.entrySet()) {
			glAttachShader(program, entry.getValue());
		}
		glLinkProgram(program);
		int[] len = new int[] { -1 };
		glGetProgramiv(program, GL_INFO_LOG_LENGTH, len);
		if (len[0] > 0) {
			Debug.error("Failed to link shader program: {}", glGetProgramInfoLog(program, len[0]));
			return false;
		}
		for (Entry<Integer, Integer> entry : shaders.entrySet()) {
			glDetachShader(program, entry.getValue());
			glDeleteShader(entry.getValue());
		}
		shaders.clear();
		return true;
	}
	
	protected void addUniform(String name) {
		int loc = glGetUniformLocation(program, name);
		if (loc < 0) {
			Debug.error("Uniform not found: " + name);
			return;
		}
		uniforms.put(name, loc);
	}
	
	public void setUniform(String name, Matrix4f mat) {
		if (!uniforms.containsKey(name)) {
			Debug.error("Failed to find uniform: " + name);
			return;
		}
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			mat.get(fb);
			glUniformMatrix4fv(uniforms.get(name), false, fb);
		}
	}
	
	public void setUniform(String name, int val) {
		if (!uniforms.containsKey(name)) {
			Debug.error("Failed to find uniform: " + name);
			return;
		}
		glUniform1i(uniforms.get(name), val);
	}
	
	public void setUniform(String name, float val) {
		if (!uniforms.containsKey(name)) {
			Debug.error("Failed to find uniform: " + name);
			return;
		}
		glUniform1f(uniforms.get(name), val);
	}
	
	public void bind() {
		glUseProgram(program);
	}
	
	public static void unbind() {
		glUseProgram(0);
	}
	
	public abstract boolean getHasError();
	
}