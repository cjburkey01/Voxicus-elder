package com.cjburkey.voxicus.graphic;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;
import org.joml.Vector3f;
import com.cjburkey.voxicus.component.ComponentCamera;
import com.cjburkey.voxicus.component.ComponentTransform;
import com.cjburkey.voxicus.core.Transformations;
import com.cjburkey.voxicus.core.Util;

public abstract class Mesh {
	
	protected boolean hasMesh = false;
	protected int vao;
	protected int vbo;
	protected int ebo;
	
	protected int elements = 0;
	
	protected Mesh() {
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		ebo = glGenBuffers();
	}
	
	/**
	 * This should not be overridden, create a different method and call <i>super.setVertices();</i> instead.
	 */
	public void setVertices(List<Vector3f> verts, List<Short> inds) {
		elements = inds.size();
		
		FloatBuffer vertBuff = Util.vec3fBuffer(verts.toArray(new Vector3f[verts.size()]));
		ShortBuffer indBuff = Util.shortBuffer(Util.shortListToArray(inds));
		
		bindVertexArray();
		
		bindVertexBuffer();
		glBufferData(GL_ARRAY_BUFFER, vertBuff, GL_STATIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glDisableVertexAttribArray(0);
		unbindVertexBuffer();
		memFree(vertBuff);
		
		bindElementBuffer();
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indBuff, GL_STATIC_DRAW);
		unbindElementBuffer();
		memFree(indBuff);
		
		unbindVertexArray();
		hasMesh = true;
	}
	
	/**
	 * Note: This should not be overridden unless absolutely necessary; override <i>onRenderCall()</i> instead.
	 */
	public void onRender(ComponentTransform parent) {
		if (!hasMesh) {
			return;
		}
		
		if (getShader() != null) {
			getShader().bind();
		}
		onUniform(parent);
		
		bindVertexArray();
		bindElementBuffer();
		glEnableVertexAttribArray(0);
		onRenderCall();
		glDisableVertexAttribArray(0);
		unbindElementBuffer();
		unbindVertexArray();
	}
	
	/**
	 * Replace or enhance the <i>glDrawElements()</i> function, or add before/after actions and <i>super.onRenderCall()</i>to permit default behavior.
	 */
	protected void onRenderCall() {
		glDrawElements(GL_TRIANGLES, elements, GL_UNSIGNED_SHORT, 0);
	}
	
	protected void onUniform(ComponentTransform parent) {
		getShader().setUniform("projectionMatrix", Transformations.PROJECTION);
		getShader().setUniform("modelViewMatrix", Transformations.getModelView(ComponentCamera.main.getParentObj().transform, parent));
	}
	
	protected abstract ShaderProgram getShader();
	
	public void onDestroy() {
		hasMesh = false;
		glDisableVertexAttribArray(0);
		unbindVertexArray();
		unbindVertexBuffer();
		unbindElementBuffer();
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
		
		vao = 0;
		vbo = 0;
		ebo = 0;
	}
	
	public void bindVertexArray() {
		glBindVertexArray(vao);
	}
	
	public void bindVertexBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
	}
	
	public void bindElementBuffer() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
	}
	
	public static void unbindVertexArray() {
		glBindVertexArray(0);
	}
	
	public static void unbindVertexBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public static void unbindElementBuffer() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
}