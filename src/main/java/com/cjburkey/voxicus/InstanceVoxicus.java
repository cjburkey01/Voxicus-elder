package com.cjburkey.voxicus;

import org.joml.Vector3f;
import com.cjburkey.voxicus.component.ComponentCamera;
import com.cjburkey.voxicus.component.ComponentFreeMove;
import com.cjburkey.voxicus.component.ComponentMesh;
import com.cjburkey.voxicus.component.ComponentMouseLook;

public class InstanceVoxicus implements IInstance {
	
	private GameObject obj1;
	private GameObject obj2;
	private GameObject obj3;
	
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
		
		obj1 = Game.getWorld().addObject();
		obj2 = Game.getWorld().addObject();
		obj3 = Game.getWorld().addObject();
		
		obj1.transform.position.set(1.0f, 3.0f, -10.0f);
		obj2.transform.position.set(1.38f, 0.0f, -1.0f);
		obj3.transform.position.set(-3.1f, 0.0f, -3.0f);
		
		obj1.addComponent(mesh);
		obj2.addComponent(mesh);
		obj3.addComponent(mesh);
		
		GameObject camObj = Game.getWorld().addObject();
		camObj.addComponent(new ComponentCamera(Game.getWindow().getWindowSize())).setClearColor(new Vector3f(0.1f, 0.1f, 0.1f));
		camObj.addComponent(new ComponentMouseLook()).setMouseLock(true);
		camObj.addComponent(new ComponentFreeMove());
	}
	
	public void update() {
	}
	
	public void render() {
	}
	
	public void exit() {
	}
	
}