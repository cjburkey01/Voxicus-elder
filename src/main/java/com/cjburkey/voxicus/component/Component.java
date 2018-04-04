package com.cjburkey.voxicus.component;

public abstract class Component {
	
	protected final ComponentHandler parent = null;
	private boolean destroyed = false;
	private boolean hasInit = false;
	
	protected final void doDestroy() {
		destroyed = true;
		doRemove();
		onDestroy();
	}
	
	protected final void doInit() {
		if (hasInit) {
			return;
		}
		hasInit = true;
		onInit();
	}
	
	protected final void doRemove() {
		onRemove();
	}
	
	protected final void doUpdateStart() {
		onUpdateStart();
	}
	
	protected final void doUpdate() {
		onUpdate();
	}
	
	protected final void doUpdateEnd() {
		onUpdateEnd();
	}
	
	protected final void doRenderStart() {
		onRenderStart();
	}
	
	protected final void doRender() {
		onRender();
	}
	
	protected final void doRenderEnd() {
		onRenderEnd();
	}
	
	protected void onInit() {
	}
	
	protected void onRemove() {
	}
	
	protected void onUpdateStart() {
	}
	
	protected void onUpdate() {
	}
	
	private void onUpdateEnd() {
	}
	
	protected void onRenderStart() {
	}
	
	protected void onRender() {
	}
	
	protected void onRenderEnd() {
	}
	
	protected void onDestroy() {
	}
	
	public final boolean getIsDestroyed() {
		return destroyed;
	}
	
	public final ComponentHandler getParent() {
		return parent;
	}
	
	public final boolean getHasInit() {
		return hasInit;
	}
	
}