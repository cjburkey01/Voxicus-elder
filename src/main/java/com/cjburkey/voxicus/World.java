package com.cjburkey.voxicus;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

public class World {
	
	public final Camera camera = new Camera(new Vector3f(0.0f, 0.0f, 1.0f));
	private final List<GameObject> objs = new ArrayList<>();
	
	public void addObject(GameObject obj) {
		objs.add(obj);
	}
	
	public void destroyObject(GameObject obj) {
		obj.cleanup();
	}
	
	public void update() {
		for (int i = 0; i < objs.size(); i ++) {
			if (objs.get(i).getIsDestroyed()) {
				objs.remove(i);
				i --;
			}
		}
	}
	
	public void render(ShaderProgram shader) {
		for (GameObject obj : objs) {
			shader.setUniform("modelViewMatrix", Transformations.getModelView(camera.transform, obj.transform));
			obj.render();
		}
	}
	
}