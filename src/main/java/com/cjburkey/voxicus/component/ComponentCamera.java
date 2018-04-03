package com.cjburkey.voxicus.component;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Transformations;

public final class ComponentCamera extends ObjectComponent {
	
	public static ComponentCamera main;
	
	private float fov = 90.0f;
	private Vector3f clearColor = new Vector3f(0.0f, 0.0f, 0.0f);
	private float minZ = 0.01f;
	private float maxZ = 1000.0f;
	private float aspect;
	
	public ComponentCamera(Vector2i windowSize) {
		this((float) windowSize.x / windowSize.y);
	}
	
	public ComponentCamera(float aspect) {
		setAspect(aspect);
		if (main != null) {
			Debug.warn("There is already a main camera defined");
			return;
		}
		main = this;
	}
	
	/**
	 * @param fov The field of view in degrees
	 */
	public void setFov(float fov) {
		this.fov = fov;
		update();
	}
	
	/**
	 * @param clearColor The color to use as the clearing color (RGB 0.0-1.0) (alpha is max)
	 */
	public void setClearColor(Vector3f clearColor) {
		this.clearColor = clearColor;
		update();
	}
	
	/**
	 * @param minZ The near clipping plane
	 */
	public void setMinZ(float minZ) {
		this.minZ = minZ;
		update();
	}
	
	/**
	 * @param maxZ The far clipping plane
	 */
	public void setMaxZ(float maxZ) {
		this.maxZ = maxZ;
		update();
	}
	
	/**
	 * @param aspect The aspect ratio of the window (width / height)
	 */
	public void setAspect(float aspect) {
		this.aspect = aspect;
		update();
	}
	
	private void update() {
		GL11.glClearColor(clearColor.x, clearColor.y, clearColor.z, 1.0f);
		Transformations.updateProjection(fov, aspect, minZ, maxZ);
	}
	
	public float getFov() {
		return fov;
	}
	
	public Vector3f getClearColor() {
		return clearColor;
	}
	
	public float getMinZ() {
		return minZ;
	}
	
	public float getMaxZ() {
		return maxZ;
	}
	
	public float getAspect() {
		return aspect;
	}
	
}