package com.cjburkey.voxicus.shader;

import static org.lwjgl.opengl.GL20.*;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Util;

public class ShaderTextureUI extends ShaderProgram {
	
	private boolean err = false;
	
	public ShaderTextureUI(String path, String vertex, String fragment) {
		if (!addShader(GL_VERTEX_SHADER, Util.getTextFromResource('/' + path + '/' + vertex + ".glsl"))) {
			Debug.error("Failed to add textured ui vertex shader");
			err = true;
			return;
		}
		if (!addShader(GL_FRAGMENT_SHADER, Util.getTextFromResource('/' + path + '/' + fragment + ".glsl"))) {
			Debug.error("Failed to add textured ui fragment shader");
			err = true;
			return;
		}
		if (!link()) {
			Debug.error("Failed to link textured ui shader program");
			err = true;
			return;
		}
		
		addUniform("projectionMatrix");
		addUniform("guiMatrix");
		addUniform("sampler");
		
		setUniform("sampler", 0);
	}
	
	public boolean getHasError() {
		return err;
	}
	
}