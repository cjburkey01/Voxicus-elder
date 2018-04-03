package com.cjburkey.voxicus.game;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.voxicus.component.ComponentCamera;
import com.cjburkey.voxicus.component.ComponentFreeMove;
import com.cjburkey.voxicus.component.ComponentMesh;
import com.cjburkey.voxicus.component.ComponentMouseLook;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.IInstance;
import com.cjburkey.voxicus.core.Input;
import com.cjburkey.voxicus.core.SemVer;
import com.cjburkey.voxicus.core.Time;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.graphic.MeshTexture;
import com.cjburkey.voxicus.graphic.Texture;
import com.cjburkey.voxicus.world.GameObject;

public class InstanceVoxicus implements IInstance {
	
	private GameObject[] objs = new GameObject[100];
	
	// Test values
	public float a = 25.0f;
	public float b = 5.0f;
	public float min = -5.0f;
	public float max = 5.0f;
	
	private float t = 0.0f;
	private float delta = 2.0f;
	
	public String getName() {
		return "Voxicus";
	}
	
	public SemVer getVersion() {
		return new SemVer(0, 0, 1, "alpha");
	}
	
	public void init() {
		Texture texture = new Texture("/res/voxicus/texture/debug/test.png");
		MeshTexture mesh = new MeshTexture();
		
		List<Vector3f> v = new ArrayList<>();
		List<Short> i = new ArrayList<>();
		List<Vector2f> t = new ArrayList<>();
		Util.addRect(v, i, t, new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(1.0f, 2.0f, 3.0f));
		mesh.setMesh(v, i, t, texture);
		
//		for (int y = 0; y < Math.sqrt(objs.length); y ++) {
//			for (int x = 0; x < Math.sqrt(objs.length); x ++) {
//				objs[y * (int) Math.sqrt(objs.length) + x] = Game.getWorld().addObject();
//				objs[y * (int) Math.sqrt(objs.length) + x].transform.position.set(x - (float) Math.sqrt(objs.length) / 2, 0.0f, y - (float) Math.sqrt(objs.length) / 2);
//				objs[y * (int) Math.sqrt(objs.length) + x].addComponent(new ComponentMesh(mesh));
//			}
//		}
		
		objs[0] = Game.getWorld().addObject();
		objs[0].transform.position.zero();
		objs[0].addComponent(new ComponentMesh(mesh));
		
		GameObject camObj = Game.getWorld().addObject();
		camObj.addComponent(new ComponentCamera(Game.getWindow().getWindowSize())).setClearColor(new Vector3f(0.1f, 0.1f, 0.1f));
		camObj.addComponent(new ComponentMouseLook()).setMouseLock(true).setPauseTimeOnFreeCursor(true).setPausedTimeScale(0.0d).setSmoothing(0.13f);
		camObj.addComponent(new ComponentFreeMove()).doManualMove(new Vector3f(0.0f, 0.0f, 10.0f), false);
	}
	
	public void update() {
		t += Time.getDeltaTimeF();
		
//		for (int y = 0; y < Math.sqrt(objs.length); y ++) {
//			for (int x = 0; x < Math.sqrt(objs.length); x ++) {
//				objs[y * (int) Math.sqrt(objs.length) + x].transform.position.y = Util.sin(min, max, b, (x + y) / (a / b) + t);
//			}
//		}
		
		boolean p = false;
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_DOWN)) {
			Time.setTimeScale(Time.getTimeScale() - ((Input.getIsKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) ? (delta * 10) :  (delta)) * Time.getPureDeltaTime());
			p = true;
		}
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_UP)) {
			Time.setTimeScale(Time.getTimeScale() + ((Input.getIsKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) ? (delta * 10) :  (delta)) * Time.getPureDeltaTime());
			p = true;
		}
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_P)) {
			Time.setTimeScale(1.0d);
			p = true;
		}
		if (p) {
			Debug.log("Time scale: {}", Time.getTimeScale());
		}
	}
	
	public void render() {
	}
	
	public void exit() {
	}
	
}