package com.cjburkey.voxicus.component;

public abstract class Component {
	
	protected final ComponentHandler parent = null;
	
	public void onInit() {
	}
	
	public void onUpdateStart() {
	}
	
	public void onUpdate() {
	}
	
	public void onUpdateEnd() {
	}
	
	public void onRenderStart() {
	}
	
	public void onRender() {
	}
	
	public void onRenderEnd() {
	}
	
	public void onDestroy() {
	}
	
	public final ComponentHandler getParent() {
		return parent;
	}
	
}