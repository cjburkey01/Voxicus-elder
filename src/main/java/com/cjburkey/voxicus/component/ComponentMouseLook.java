package com.cjburkey.voxicus.component;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.voxicus.core.Input;
import com.cjburkey.voxicus.core.Time;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.game.Game;

public class ComponentMouseLook extends ObjectComponent {

	private boolean mouseLocked = false;
	private boolean pauseOnNotLocked = false;
	private double timeScaleBeforePause = 0.0d;
	private double pausedTimeScale = 1.0d;
	
	private float sensitivity = 0.5f;
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
			Time.setTimeScale(pausedTimeScale);
			Input.voidMouseMovement();
		}
		
		rotChange.set(Input.getMouseDelta().y, Input.getMouseDelta().x);
		rotChange.mul(sensitivity);
		goalRotation.add(rotChange);
		goalRotation.x = Util.clamp(goalRotation.x, -90.0f, 90.0f);
		
		updateRotation();
	}
	
	private void updateRotation() {
		rotation = Util.smoothDamp(rotation, goalRotation, rotationV, smoothing, Time.getPureDeltaTimeF());
		getParentObj().transform.rotation.rotationXYZ(rotation.x * Util.DEG_RAD, rotation.y * Util.DEG_RAD, 0.0f);
	}
	
	public ComponentMouseLook toggleMouseLock() {
		setMouseLock(!mouseLocked);
		return this;
	}
	
	public ComponentMouseLook setMouseLock(boolean lock) {
		mouseLocked = lock;
		GLFW.glfwSetInputMode(Game.getWindow().getId(), GLFW.GLFW_CURSOR, (mouseLocked) ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
		if (pauseOnNotLocked) {
			if (!mouseLocked) {
				timeScaleBeforePause = Time.getTimeScale();
			} else {
				Time.setTimeScale(timeScaleBeforePause);
			}
		}
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
	
	public ComponentMouseLook setPauseTimeOnFreeCursor(boolean pauseOnNotLocked) {
		this.pauseOnNotLocked = pauseOnNotLocked;
		return this;
	}
	
	public ComponentMouseLook setPausedTimeScale(double pausedTimeScale) {
		this.pausedTimeScale = pausedTimeScale;
		return this;
	}
	
	public ComponentMouseLook doManualLook(Vector2f look, boolean smooth) {
		goalRotation.set(look);
		if (!smooth) {
			rotation.set(look);
		}
		updateRotation();
		return this;
	}
	
}