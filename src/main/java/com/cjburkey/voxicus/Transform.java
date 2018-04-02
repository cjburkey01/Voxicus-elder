package com.cjburkey.voxicus;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {
	
	public final Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	public final Quaternionf rotation = new Quaternionf();
	public final Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
	
	public Transform() {
	}
	
	public Transform(Transform copy) {
		copy(copy);
	}
	
	public Transform(Vector3f position) {
		this.position.set(position);
	}
	
	public Transform(Vector3f position, Quaternionf rotation) {
		this(position);
		this.rotation.set(rotation);
	}
	
	public Transform(Vector3f position, Quaternionf rotation, Vector3f scale) {
		this(position, rotation);
		this.scale.set(scale);
	}
	
	public void copy(Transform other) {
		position.set(other.position);
		rotation.set(other.rotation);
		scale.set(other.scale);
	}
	
	public Vector3f transform(Vector3f in) {
		return rotation.transform(in, new Vector3f());
	}
	
}