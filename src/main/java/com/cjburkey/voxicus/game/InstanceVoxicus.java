package com.cjburkey.voxicus.game;

import org.joml.Vector3f;
import com.cjburkey.voxicus.component.ComponentCamera;
import com.cjburkey.voxicus.component.ComponentFreeMove;
import com.cjburkey.voxicus.component.ComponentMesh;
import com.cjburkey.voxicus.component.ComponentMouseLook;
import com.cjburkey.voxicus.core.IInstance;
import com.cjburkey.voxicus.core.Time;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.world.GameObject;

public class InstanceVoxicus implements IInstance {
	
	private GameObject[] objs = new GameObject[100];
	
	private float a = 25.0f;
	private float b = 5.0f;
	private float min = -5.0f;
	private float max = 5.0f;
	
	private float t = 0.0f;
	
	public void init() {
		ComponentMesh mesh = new ComponentMesh();
		mesh.setMesh(Util.arrayToList(new Vector3f[] {
			// Vertices
			new Vector3f(-0.5f, 0.5f, 0.5f),	// 0
			new Vector3f(-0.5f, -0.5f, 0.5f),	// 1
			new Vector3f(0.5f, -0.5f, 0.5f),	// 2
			new Vector3f(0.5f, 0.5f, 0.5f),		// 3
			new Vector3f(-0.5f, 0.5f, -0.5f),	// 4
			new Vector3f(-0.5f, -0.5f, -0.5f),	// 5
			new Vector3f(0.5f, -0.5f, -0.5f),	// 6
			new Vector3f(0.5f, 0.5f, -0.5f)		// 7
		}), Util.arrayToList(new Short[] {
			// Indices
			0, 1, 2,	0, 2, 3,	// Front
			6, 5, 4,	7, 6, 4,	// Back
			3, 2, 6,	3, 6, 7,	// Right
			4, 5, 1,	4, 1, 0,	// Left
			4, 0, 3,	4, 3, 7,	// Top
			2, 1, 5,	6, 2, 5		// Bottom
		}), Util.arrayToList(new Vector3f[] {
			// Colors
			new Vector3f(1.0f, 0.0f, 0.0f),
			new Vector3f(0.0f, 1.0f, 0.0f),
			new Vector3f(1.0f, 1.0f, 0.0f),
			new Vector3f(0.0f, 0.0f, 1.0f),
			new Vector3f(1.0f, 0.0f, 1.0f),
			new Vector3f(0.0f, 1.0f, 1.0f),
			new Vector3f(1.0f, 1.0f, 1.0f),
			new Vector3f(0.5f, 0.5f, 0.5f)
		}));
		
		for (int y = 0; y < Math.sqrt(objs.length); y ++) {
			for (int x = 0; x < Math.sqrt(objs.length); x ++) {
				objs[y * (int) Math.sqrt(objs.length) + x] = Game.getWorld().addObject();
				objs[y * (int) Math.sqrt(objs.length) + x].transform.position.set(x - (float) Math.sqrt(objs.length) / 2, 0.0f, y - (float) Math.sqrt(objs.length) / 2);
				objs[y * (int) Math.sqrt(objs.length) + x].addComponent(mesh);
			}
		}
		
		GameObject camObj = Game.getWorld().addObject();
		camObj.addComponent(new ComponentCamera(Game.getWindow().getWindowSize())).setClearColor(new Vector3f(0.1f, 0.1f, 0.1f));
		camObj.addComponent(new ComponentMouseLook()).setMouseLock(true).setPauseTimeOnFreeCursor(true).setPausedTimeScale(0.1d);
		camObj.addComponent(new ComponentFreeMove()).doManualMove(new Vector3f(0.0f, 0.0f, 10.0f), false);
	}
	
	public void update() {
		t += Time.getDeltaTimeF();
		
		for (int y = 0; y < Math.sqrt(objs.length); y ++) {
			for (int x = 0; x < Math.sqrt(objs.length); x ++) {
				objs[y * (int) Math.sqrt(objs.length) + x].transform.position.y = Util.sin(min, max, b, (x + y) / (a / b) + t);
			}
		}
	}
	
	public void render() {
	}
	
	public void exit() {
	}
	
}