package com.cjburkey.voxicus.component;

import com.cjburkey.voxicus.graphic.Mesh;

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
			mesh.onRender(getParentObj().transform);
		}
	}
	
	public void onDestroy() {
		if (mesh != null) {
			mesh.onDestroy();
		}
	}
	
}