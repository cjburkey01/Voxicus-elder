package com.cjburkey.voxicus.mesh;

import com.cjburkey.voxicus.component.ComponentTransform;
import com.cjburkey.voxicus.shader.ShaderProgram;

public class MeshTextureUI extends MeshTexture {
	
	// Handled by the GUIHandler
	protected void onUniform(ComponentTransform parent) {
	}
	
	// Handled by the GUIHandler
	public ShaderProgram getShader() {
		return null;
	}
	
	public boolean getShouldAutoUniform() {
		return false;
	}
	
}