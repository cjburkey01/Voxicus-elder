package com.cjburkey.voxicus;

import static org.lwjgl.glfw.GLFW.*;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2f;

public class Input {
	
	private static final Map<Integer, Boolean> keys = new HashMap<>();
	
	private static boolean firstMouse = true;
	private static final Vector2f prevMousePos = new Vector2f(0.0f, 0.0f);
	private static final Vector2f currMousePos = new Vector2f(0.0f, 0.0f);
	private static final Vector2f mouseDelta = new Vector2f(0.0f, 0.0f);
	
	public static void init(Window window) {
		glfwSetKeyCallback(window.getId(), (win, key, scan, action, mods) -> {
			if (action == GLFW_PRESS) {
				onKeyPressed(key);
			} else if (action == GLFW_RELEASE) {
				onKeyReleased(key);
			}
		});
		glfwSetCursorPosCallback(window.getId(), (win, x, y) -> onMouseMove(x, y));
	}
	
	public static void update() {
		for (Integer key : keys.keySet()) {
			keys.put(key, false);
		}
		mouseDelta.set(0.0f, 0.0f);
	}
	
	private static void onKeyPressed(int key) {
		keys.put(key, true);
	}
	
	private static void onKeyReleased(int key) {
		keys.remove(key);
	}
	
	private static void onMouseMove(double x, double y) {
		prevMousePos.set(currMousePos);
		currMousePos.set((float) x, (float) y);
		if (firstMouse) {
			prevMousePos.set(currMousePos);
			mouseDelta.set(0.0f, 0.0f);
			firstMouse = false;
			return;
		}
		mouseDelta.set(currMousePos.x - prevMousePos.x, currMousePos.y - prevMousePos.y);
	}
	
	/**
	 * True for every frame that a key is held down
	 */
	public static boolean getIsKeyDown(int key) {
		return keys.containsKey(key);
	}
	
	/**
	 * True for the first frame that a key is held down
	 */
	public static boolean getIsKeyPressed(int key) {
		return keys.containsKey(key) && keys.get(key);
	}
	
	public static Vector2f getMousePos() {
		return new Vector2f(currMousePos);
	}
	
	public static Vector2f getMouseDelta() {
		return new Vector2f(mouseDelta);
	}
	
}