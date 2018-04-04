package com.cjburkey.voxicus.graphic;

import com.cjburkey.voxicus.component.ComponentTransform;

public class MeshTextureUI extends MeshTexture {
	
	// Handled by the GUIHandler
	protected void onUniform(ComponentTransform parent) {
	}
	
	// Handled by the GUIHandler
	protected ShaderProgram getShader() {
		return null;
	}
	
}