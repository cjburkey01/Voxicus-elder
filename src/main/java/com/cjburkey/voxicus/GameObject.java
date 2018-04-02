package com.cjburkey.voxicus;

public class GameObject {
	
	public final Transform transform = new Transform();
	public Mesh mesh;
	private boolean destroyed = false;
	
	public GameObject() {
	}
	
	public GameObject(Transform transform) {
		this.transform.copy(transform);
	}
	
	public GameObject(Transform transform, Mesh mesh) {
		this.transform.copy(transform);
		this.mesh = mesh;
	}
	
	public GameObject(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public void update() {
		if (destroyed) {
			return;
		}
	}
	
	public void render() {
		if (destroyed) {
			return;
		}
		if (mesh != null) {
			mesh.render();
		}
	}
	
	public void cleanup() {
		destroyed = true;
		if (mesh != null) {
			mesh.destroy();
		}
	}
	
	public boolean getIsDestroyed() {
		return destroyed;
	}
	
}