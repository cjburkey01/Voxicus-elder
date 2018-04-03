package com.cjburkey.voxicus.world;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.voxicus.component.Component;
import com.cjburkey.voxicus.component.ComponentCamera;
import com.cjburkey.voxicus.core.Debug;

public class Scene {
	
	private boolean camWasNull = true;
	private final List<GameObject> objs = new ArrayList<>();
	
	public GameObject addObject() {
		GameObject obj = new GameObject();
		objs.add(obj);
		obj.onInit();
		return obj;
	}
	
	public void update() {
		for (int i = 0; i < objs.size(); i ++) {
			if (objs.get(i).getIsDestroyed()) {
				objs.get(i).onDestroy();
				objs.remove(i);
				i --;
			}
		}
		objs.forEach(obj -> obj.onUpdateStart());
		objs.forEach(obj -> obj.onUpdate());
		objs.forEach(obj -> obj.onUpdateEnd());
	}
	
	public void render() {
		if (ComponentCamera.main == null) {
			camWasNull = true;
			Debug.warn("Camera not found in scene");
			return;
		} else if (camWasNull) {
			camWasNull = false;
			Debug.log("Camera found in scene");
		}
		
		objs.forEach(obj -> {
			obj.onRenderStart();
		});
		objs.forEach(obj -> {
			obj.onRender();
		});
		objs.forEach(obj -> {
			obj.onRenderEnd();
		});
	}
	
	public <T extends Component> T getComponentInScene(Class<T> type) {
		for (GameObject obj : objs) {
			T comp = obj.getComponent(type);
			if (comp != null) {
				return comp;
			}
		}
		return null;
	}
	
}