package com.cjburkey.voxicus.component;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.voxicus.core.Input;
import com.cjburkey.voxicus.core.Time;
import com.cjburkey.voxicus.core.Util;

public class ComponentFreeMove extends ObjectComponent {
	
	private float normalSpeed = 7.5f;
	private float fastSpeed = 25.0f;
	private float smoothing = 0.15f;
	
	private final Vector3f goalPos = new Vector3f();
	private final Vector3f posV = new Vector3f();
	
	public void onUpdate() {
		float deltaTimeSpeed = Time.getDeltaTimeF() * ((Input.getIsKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) ? (fastSpeed) : (normalSpeed));
		Vector3f dir = new Vector3f(0.0f, 0.0f, 0.0f);
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_W)) {
			dir.z -= 1.0f;
		}
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_S)) {
			dir.z += 1.0f;
		}
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_A)) {
			dir.x -= 1.0f;
		}
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_D)) {
			dir.x += 1.0f;
		}
		
		if (!dir.equals(new Vector3f())) {
			dir = getParentObj().transform.transformDirection(dir.normalize(), true);
			dir.mul(deltaTimeSpeed);
		}
		
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
			dir.y -= deltaTimeSpeed;
		}
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_SPACE)) {
			dir.y += deltaTimeSpeed;
		}
		
		goalPos.add(dir);
		
		ComponentTransform p = getParentObj().transform;
		p.position.set(Util.smoothDamp(p.position, goalPos, posV, smoothing, Time.getDeltaTimeF()));
	}
	
	public ComponentFreeMove setNormalSpeed(float normalSpeed) {
		this.normalSpeed = normalSpeed;
		return this;
	}
	
	public ComponentFreeMove setFastSpeed(float fastSpeed) {
		this.fastSpeed = fastSpeed;
		return this;
	}
	
	public ComponentFreeMove setSmoothing(float smoothing) {
		this.smoothing = smoothing;
		return this;
	}
	
}