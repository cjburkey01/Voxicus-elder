package com.cjburkey.voxicus.component;

import com.cjburkey.voxicus.core.Transformations;
import com.cjburkey.voxicus.mesh.Mesh;

public final class ComponentMesh extends ObjectComponent {
	
	private Mesh mesh;
	
	public ComponentMesh() {
	}
	
	public ComponentMesh(Mesh mesh) {
		setMesh(mesh);
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public void onRender() {
		if (mesh != null) {
			if (mesh.getShader() != null && mesh.getShouldAutoUniform()) {
				mesh.getShader().bind();
				mesh.getShader().setUniform("projectionMatrix", Transformations.PROJECTION);
				mesh.getShader().setUniform("modelViewMatrix", Transformations.getModelView(ComponentCamera.main.getParentObj().transform, getParentObj().transform));
			}
			mesh.onRender();
		}
	}
	
	public void onDestroy() {
		if (mesh != null) {
			mesh.onDestroy();
		}
	}
	
}