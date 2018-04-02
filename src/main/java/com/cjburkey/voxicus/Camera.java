package com.cjburkey.voxicus;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera extends GameObject {

	public Camera() {
	}
	
	public Camera(Vector3f pos) {
		super(new Transform(pos));
	}
	
	public Camera(Vector3f pos, Quaternionf rot) {
		super(new Transform(pos, rot));
	}
	
}