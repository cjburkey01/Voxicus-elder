package com.cjburkey.voxicus.graphic;

import static org.lwjgl.opengl.GL20.*;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Util;

public class ShaderColor extends ShaderProgram {
	
	private boolean err = false;
	
	public ShaderColor() {
		if (!addShader(GL_VERTEX_SHADER, Util.getTextFromResource("/res/voxicus/shader/colorVertex.glsl"))) {
			Debug.error("Failed to add color vertex shader");
			err = true;
			return;
		}
		if (!addShader(GL_FRAGMENT_SHADER, Util.getTextFromResource("/res/voxicus/shader/colorFragment.glsl"))) {
			Debug.error("Failed to add color fragment shader");
			err = true;
			return;
		}
		if (!link()) {
			Debug.error("Failed to link color shader program");
			err = true;
			return;
		}
		
		addUniform("projectionMatrix");
		addUniform("modelViewMatrix");
	}
	
	public boolean getHasError() {
		return err;
	}
	
}