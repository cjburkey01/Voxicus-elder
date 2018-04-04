package com.cjburkey.voxicus.core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.voxicus.component.ComponentTransform;

public class Transformations {
	
	public static final Matrix4f PROJECTION = new Matrix4f();
	public static final Matrix4f VIEW = new Matrix4f();
	public static final Matrix4f MODEL = new Matrix4f();
	public static final Matrix4f MODEL_VIEW = new Matrix4f();
	
	public static final Matrix4f ORTHOGRAPHIC = new Matrix4f();
	public static final Matrix4f MODEL_ORTHOGRAPHIC = new Matrix4f();
	
	public static final void updateProjection(float fovDeg, int width, int height, float near, float far) {
		PROJECTION.identity().perspective(Util.DEG_RAD * fovDeg, (float) width / height, near, far);
		updateOrthographic(0, width, height, 0, 0.001f, 10.0f);
	}
	
	public static final void updateOrthographic(float left, float right, float bottom, float top, float near, float far) {
		ORTHOGRAPHIC.identity().ortho(left, right, bottom, top, near, far);
	}
	
	public static final void updateView(ComponentTransform camera) {
		VIEW.identity().rotate(camera.rotation).translate(new Vector3f(camera.position).mul(-1.0f));
	}
	
	public static final void updateModel(ComponentTransform obj) {
		MODEL.identity().translate(obj.position).rotate(obj.rotation).scale(obj.scale);
	}
	
	public static final void updateModelOrthographics(Vector2f pos, float rotation, Vector2f scale) {
		MODEL_ORTHOGRAPHIC.identity().translate(pos.x, pos.y, -1.0f).rotateZ(rotation).scale(scale.x, scale.y, 1.0f);
	}
	
	public static final Matrix4f getModelView(ComponentTransform camera, ComponentTransform obj) {
		updateView(camera);
		updateModel(obj);
		return VIEW.mul(MODEL, MODEL_VIEW.identity());
	}
	
}