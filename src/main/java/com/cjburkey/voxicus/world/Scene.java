package com.cjburkey.voxicus.world;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.voxicus.Voxicus;
import com.cjburkey.voxicus.component.Component;
import com.cjburkey.voxicus.component.ComponentCamera;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.event.EventSystem;
import com.cjburkey.voxicus.gui.GuiHandler;

public class Scene {
	
	private static Scene active;
	
	private boolean camWasNull = true;
	private final GuiHandler guiHandler = new GuiHandler();
	private final List<GameObject> objs = new ArrayList<>();
	private final List<GameObject> addQueue = new ArrayList<>();
	private final EventSystem eventHandler = new EventSystem();
	
	public Scene() {
		active = this;
	}
	
	public GameObject addObject(String name) {
		GameObject obj = new GameObject(name);
		addQueue.add(obj);
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
		while (!addQueue.isEmpty()) {
			objs.add(addQueue.get(0));
			addQueue.get(0).onInit();
			addQueue.remove(0);
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
		objs.forEach(obj -> obj.onRenderStart());
		objs.forEach(obj -> obj.onRender());
		objs.forEach(obj -> obj.onRenderEnd());
		guiHandler.onRender(Voxicus.getGame().shaderTexturedUI);
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
	
	public EventSystem getEventHandler() {
		return eventHandler;
	}
	
	public GuiHandler getGuiHandler() {
		return guiHandler;
	}
	
	public static Scene getActive() {
		return active;
	}
	
}