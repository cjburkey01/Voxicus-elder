package com.cjburkey.voxicus;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.cjburkey.voxicus.component.ComponentTransform;

public class Transformations {
	
	public static final Matrix4f PROJECTION = new Matrix4f();
	public static final Matrix4f VIEW = new Matrix4f();
	public static final Matrix4f MODEL = new Matrix4f();
	public static final Matrix4f MODEL_VIEW = new Matrix4f();
	
	public static final void updateProjection(float fovDeg, int width, int height, float near, float far) {
		updateProjection(fovDeg, (float) width / height, near, far);
	}
	
	public static final void updateProjection(float fovDeg, float aspect, float near, float far) {
		PROJECTION.identity().perspective(Util.DEG_RAD * fovDeg, aspect, near, far);
	}
	
	public static final void updateView(ComponentTransform camera) {
		VIEW.identity().rotate(camera.rotation).translate(new Vector3f(camera.position).mul(-1.0f));
	}
	
	public static final void updateModel(ComponentTransform obj) {
		MODEL.identity().translate(obj.position).rotate(obj.rotation).scale(obj.scale);
	}
	
	public static final Matrix4f getModelView(ComponentTransform camera, ComponentTransform obj) {
		updateView(camera);
		updateModel(obj);
		return VIEW.mul(MODEL, MODEL_VIEW.identity());
	}
	
}