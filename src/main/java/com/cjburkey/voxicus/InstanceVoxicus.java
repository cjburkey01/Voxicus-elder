package com.cjburkey.voxicus;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class InstanceVoxicus implements IInstance {
	
	private float rotationSpeed = 90f;
	private Vector2f camRotVel = new Vector2f();
	private Vector2f camRotation = new Vector2f();
	
	private GameObject obj1;
	private GameObject obj2;
	private GameObject obj3;
	
	public void init() {
		Mesh mesh = new Mesh();
		mesh.updateMesh(Util.arrayToList(new Vector3f[] {
			new Vector3f(-0.5f, 0.5f, 0.0f),	// 0
			new Vector3f(-0.5f, -0.5f, 0.0f),	// 1
			new Vector3f(0.5f, -0.5f, 0.0f),	// 2
			new Vector3f(0.5f, 0.5f, 0.0f)	// 3
		}), Util.arrayToList(new Short[] {
			0, 1, 2,
			0, 2, 3
		}), Util.arrayToList(new Vector3f[] {
			new Vector3f(1.0f, 0.0f, 0.0f),
			new Vector3f(0.0f, 1.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 1.0f),
			new Vector3f(1.0f, 1.0f, 0.0f)
		}));
		obj1 = new GameObject(mesh);
		obj2 = new GameObject(new Transform(new Vector3f(1.38f, 0.0f, -1.0f)), mesh);
		obj3 = new GameObject(new Transform(new Vector3f(-3.1f, 0.0f, -3.0f)), mesh);
		Voxicus.getGame().world.addObject(obj1);
		Voxicus.getGame().world.addObject(obj2);
		Voxicus.getGame().world.addObject(obj3);
	}
	
	public void update() {
		Transform cam = Voxicus.getGame().world.camera.transform;

		camRotVel.set(Input.getMouseDelta().y, Input.getMouseDelta().x);
		camRotVel.mul(rotationSpeed * (float) Time.getDeltaTime());
		camRotation.add(camRotVel);
		camRotation.x = Util.clamp(camRotation.x, -90.0f, 90.0f);
		cam.rotation.rotationXYZ(camRotation.x * Util.DEG_RAD, camRotation.y * Util.DEG_RAD, 0.0f);
		
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
			dir = cam.transform(dir);
			dir.normalize();
			dir.mul((float) Time.getDeltaTime());
		}
		
		cam.position.add(dir);
	}
	
	public void render() {
	}
	
	public void exit() {
	}
	
}