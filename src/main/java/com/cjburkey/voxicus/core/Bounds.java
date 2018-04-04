package com.cjburkey.voxicus.core;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Bounds {
	
	private final Vector4f bounds = new Vector4f();
	
	public Bounds(Vector4f bounds) {
		this.bounds.set(bounds);
	}
	
	public Bounds(Vector2f pos, Vector2f size) {
		this(pos.x, pos.y, size.x, size.y);
	}
	
	public Bounds(float x, float y, float width, float height) {
		bounds.set(x, y, x + width, x + height);
	}
	
	public Vector2f getMin() {
		return new Vector2f(bounds.x, bounds.y);
	}
	
	public Vector2f getMax() {
		return new Vector2f(bounds.z, bounds.w);
	}
	
	public Vector2f getSize() {
		return new Vector2f(bounds.z - bounds.x, bounds.w - bounds.y);
	}
	
	public Vector4f getBounds() {
		return new Vector4f(bounds);
	}
	
}