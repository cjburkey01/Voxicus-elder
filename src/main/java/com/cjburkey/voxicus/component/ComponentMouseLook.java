package com.cjburkey.voxicus.component;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.voxicus.Game;
import com.cjburkey.voxicus.Input;
import com.cjburkey.voxicus.Time;
import com.cjburkey.voxicus.Util;

public class ComponentMouseLook extends ObjectComponent {

	private boolean mouseLocked = false;
	
	private float sensitivity = 120.0f;
	private float smoothing = 0.1f;
	
	private Vector2f rotChange = new Vector2f();
	private Vector2f goalRotation = new Vector2f();
	private Vector2f rotation = new Vector2f();
	private Vector2f rotationV = new Vector2f();
	
	public void onUpdate() {
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			toggleMouseLock();
		}
		if (!mouseLocked) {
			Input.voidMouseMovement();
		}
		
		rotChange.set(Input.getMouseDelta().y, Input.getMouseDelta().x);
		rotChange.mul(sensitivity * (float) Time.getDeltaTime());
		goalRotation.add(rotChange);
		goalRotation.x = Util.clamp(goalRotation.x, -90.0f, 90.0f);
		
		rotation = Util.smoothDamp(rotation, goalRotation, rotationV, smoothing, Time.getDeltaTimeF());
		getParentObj().transform.rotation.rotationXYZ(rotation.x * Util.DEG_RAD, rotation.y * Util.DEG_RAD, 0.0f);
	}
	
	public ComponentMouseLook toggleMouseLock() {
		setMouseLock(!mouseLocked);
		return this;
	}
	
	public ComponentMouseLook setMouseLock(boolean lock) {
		mouseLocked = lock;
		GLFW.glfwSetInputMode(Game.getWindow().getId(), GLFW.GLFW_CURSOR, (mouseLocked) ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
		return this;
	}
	
	public ComponentMouseLook setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
		return this;
	}
	
	public ComponentMouseLook setSmoothing(float smoothing) {
		this.smoothing = smoothing;
		return this;
	}
	
}