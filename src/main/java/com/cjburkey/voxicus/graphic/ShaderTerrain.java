package com.cjburkey.voxicus.graphic;

import static org.lwjgl.opengl.GL20.*;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Util;

public class ShaderTerrain extends ShaderProgram {
	
	private boolean err = false;
	
	public ShaderTerrain() {
		if (!addShader(GL_VERTEX_SHADER, Util.getTextFromResource("/res/voxicus/shader/terrainVertex.glsl"))) {
			Debug.error("Failed to add terrain vertex shader");
			err = true;
			return;
		}
		if (!addShader(GL_FRAGMENT_SHADER, Util.getTextFromResource("/res/voxicus/shader/terrainFragment.glsl"))) {
			Debug.error("Failed to add terrain fragment shader");
			err = true;
			return;
		}
		if (!link()) {
			Debug.error("Failed to link terrain shader program");
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