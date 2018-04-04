package com.cjburkey.voxicus.component;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ComponentTransform extends ObjectComponent {
	
	public String name;
	public final Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	public final Quaternionf rotation = new Quaternionf();
	public final Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
	
	public ComponentTransform(String name) {
		this.name = name;
	}
	
	public ComponentTransform(String name, ComponentTransform copy) {
		this(name);
		copy(copy);
	}
	
	public ComponentTransform(String name, Vector3f position) {
		this(name);
		this.position.set(position);
	}
	
	public ComponentTransform(String name, Vector3f position, Quaternionf rotation) {
		this(name, position);
		this.rotation.set(rotation);
	}
	
	public ComponentTransform(String name, Vector3f position, Quaternionf rotation, Vector3f scale) {
		this(name, position, rotation);
		this.scale.set(scale);
	}
	
	public void copy(ComponentTransform other) {
		position.set(other.position);
		rotation.set(other.rotation);
		scale.set(other.scale);
	}
	
	public Vector3f getForward() {
		return transformDirection(new Vector3f(0.0f, 0.0f, 1.0f), true);
	}
	
	public Vector3f getRight() {
		return transformDirection(new Vector3f(1.0f, 0.0f, 0.0f), true);
	}
	
	public Vector3f getUp() {
		return transformDirection(new Vector3f(0.0f, 1.0f, 0.0f), true);
	}
	
	public Vector3f transformDirection(Vector3f dir, boolean normalize) {
		Matrix4f inv = rotation.get(new Matrix4f()).invert();
		Vector3f vec = inv.transformDirection(dir);
		return (normalize) ? vec.normalize() : vec;
	}
	
	public Vector3f transform(Vector3f pos) {
		Matrix4f inv = rotation.get(new Matrix4f()).invert();
		return inv.transformPosition(pos);
	}
	
}