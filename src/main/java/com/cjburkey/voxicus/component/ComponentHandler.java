package com.cjburkey.voxicus.component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import com.cjburkey.voxicus.Debug;
import com.cjburkey.voxicus.Util;

public class ComponentHandler {
	
	private List<Component> components = new ArrayList<Component>();
	
	public void onInit() {
		components.forEach(c -> c.onInit());
	}
	
	public void onUpdateStart() {
		components.forEach(c -> c.onUpdateStart());
	}
	
	public void onUpdate() {
		components.forEach(c -> c.onUpdate());
	}
	
	public void onUpdateEnd() {
		components.forEach(c -> c.onUpdateEnd());
	}
	
	public void onRenderStart() {
		components.forEach(c -> c.onRenderStart());
	}
	
	public void onRender() {
		components.forEach(c -> c.onRender());
	}
	
	public void onRenderEnd() {
		components.forEach(c -> c.onRenderEnd());
	}
	
	public void onDestroy() {
		components.forEach(c -> c.onDestroy());
	}
	
	/**
	 * Adds a component to the handler if a component of the same type is not found on the handler
	 */
	public final <T extends Component> T addComponent(T component) {
		if (component == null) {
			return null;
		}
		for (Component comp : components) {
			if (comp.getClass().equals(component.getClass())) {
				return null;
			}
		}
		
		// Assign the parent using reflection because speed isn't important and we don't want to impose a constructor
		try {
			Field parentField = Util.getFieldFromNameInClassOrSupers(component.getClass(), "parent");
			if (parentField == null) {
				throw new Exception("Parent field was null");
			}
			parentField.setAccessible(true);
			parentField.set(component, this);
			//parentField.setInt(parentField, parentField.getModifiers() & ~Modifier.FINAL);
			return ((components.add(component)) ? (component) : (null));
		} catch (Exception e) {
			Debug.error("Failed to set component parent: " + component.getClass());
			Debug.error(e, false);
			return null;
		}
	}
	
	/**
	 * Retrieves the component of the specified type from the handler, or returns null if there is no component of that type present
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Component> T getComponent(Class<T> type) {
		for (Component component : components) {
			if (type.equals(component.getClass())) {
				return (T) component;
			}
		}
		return null;
	}
	
	/**
	 * Removes the component from the hander and returns it, or returns null if there is no component of that type associated with the handler
	 */
	public final <T extends Component> T removeComponent(Class<T> type) {
		T comp = getComponent(type);
		if (comp == null) {
			return null;
		}
		components.remove(comp);
		return comp;
	}
	
}