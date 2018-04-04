package com.cjburkey.voxicus.gui;

import org.joml.Vector2f;
import com.cjburkey.voxicus.core.Bounds;

public abstract class GuiElement {
	
	private boolean isDestroyed;
	protected Bounds bounds;
	protected Vector2f scale = new Vector2f(1.0f, 1.0f);
	protected float rotation = 0.0f;
	
	protected GuiElement(Bounds bounds) {
		this.bounds = bounds;
	}
	
	public abstract void onCreate(GuiHandler parent);
	public abstract void onRender(GuiHandler parent);
	public abstract void onRemove(GuiHandler parent);
	
	public final Bounds getBounds() {
		return bounds;
	}
	
	public final Vector2f getScale() {
		return scale;
	}
	
	public final float getRotation() {
		return rotation;
	}
	
	public final void setDestroyed(GuiHandler parent) {
		isDestroyed = true;
	}
	
	public final boolean getIsDestroyed() {
		return isDestroyed;
	}
	
}