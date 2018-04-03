package com.cjburkey.voxicus;

import com.cjburkey.voxicus.component.ComponentHandler;
import com.cjburkey.voxicus.component.ComponentTransform;

public final class GameObject extends ComponentHandler {
	
	/**
	 * An easier access to this object's transform
	 */
	public final ComponentTransform transform;
	private boolean destroyed = false;
	
	public GameObject() {
		transform = addComponent(new ComponentTransform());
	}
	
	public void onUpdateStart() {
		if (destroyed) {
			return;
		}
		super.onUpdateStart();
	}
	
	public void onUpdate() {
		if (destroyed) {
			return;
		}
		super.onUpdate();
	}
	
	public void onUpdateEnd() {
		if (destroyed) {
			return;
		}
		super.onUpdateEnd();
	}
	
	public void onRenderStart() {
		if (destroyed) {
			return;
		}
		super.onRenderStart();
	}
	
	public void onRender() {
		if (destroyed) {
			return;
		}
		super.onRender();
	}
	
	public void onRenderEnd() {
		if (destroyed) {
			return;
		}
		super.onRenderEnd();
	}
	
	public void destroy() {
		destroyed = true;
	}
	
	public boolean getIsDestroyed() {
		return destroyed;
	}
	
}