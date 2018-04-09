package com.cjburkey.voxicus.shader;

import static org.lwjgl.opengl.GL20.*;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Util;

public class ShaderVoxel extends ShaderProgram {
	
	private boolean err = false;
	
	public ShaderVoxel(String path, String vertex, String fragment) {
		if (!addShader(GL_VERTEX_SHADER, Util.getTextFromResource('/' + path + '/' + vertex + ".glsl"))) {
			Debug.error("Failed to add voxel vertex shader");
			err = true;
			return;
		}
		if (!addShader(GL_FRAGMENT_SHADER, Util.getTextFromResource('/' + path + '/' + fragment + ".glsl"))) {
			Debug.error("Failed to add voxel fragment shader");
			err = true;
			return;
		}
		if (!link()) {
			Debug.error("Failed to link voxel shader program");
			err = true;
			return;
		}
		
		addUniform("projectionMatrix");
		addUniform("modelViewMatrix");
		addUniform("sampler");
		addUniform("atlasPreDoubleWidth");
		
		setUniform("sampler", 0);
	}
	
	public boolean getHasError() {
		return err;
	}
	
}