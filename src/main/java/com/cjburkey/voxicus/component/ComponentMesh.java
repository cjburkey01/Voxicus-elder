package com.cjburkey.voxicus.component;

import com.cjburkey.voxicus.graphic.MeshColored;

public final class ComponentMesh extends ObjectComponent {
	
	private MeshColored mesh;
	
	public ComponentMesh() {
	}
	
	public ComponentMesh(MeshColored mesh) {
		setMesh(mesh);
	}
	
	public void setMesh(MeshColored mesh) {
		this.mesh = mesh;
	}
	
	public MeshColored getMesh() {
		return mesh;
	}
	
	public void onRender() {
		if (mesh != null) {
			mesh.onRender(getParentObj().transform);
		}
	}
	
	public void onDestroy() {
		if (mesh != null) {
			mesh.onDestroy();
		}
	}
	
//	private final int vao;
//	private final int vbo;
//	private final int ebo;
//	private final int cbo;
//	
//	private int elements = 0;
//	
//	public ComponentMesh() {
//		vao = glGenVertexArrays();
//		vbo = glGenBuffers();
//		ebo = glGenBuffers();
//		cbo = glGenBuffers();
//	}
//	
//	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector3f> colors) {
//		elements = inds.size();
//		
//		FloatBuffer vertBuff = Util.vec3fBuffer(verts.toArray(new Vector3f[verts.size()]));
//		ShortBuffer indBuff = Util.shortBuffer(Util.shortListToArray(inds));
//		FloatBuffer colBuff = Util.vec3fBuffer(colors.toArray(new Vector3f[colors.size()]));
//		
//		bindVertexArray();
//		
//		bindVertexBuffer();
//		glBufferData(GL_ARRAY_BUFFER, vertBuff, GL_STATIC_DRAW);
//		glEnableVertexAttribArray(0);
//		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
//		glDisableVertexAttribArray(0);
//		unbindVertexBuffer();
//		memFree(vertBuff);
//		
//		bindElementBuffer();
//		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indBuff, GL_STATIC_DRAW);
//		unbindElementBuffer();
//		memFree(indBuff);
//		
//		glBindBuffer(GL_ARRAY_BUFFER, cbo);
//		glBufferData(GL_ARRAY_BUFFER, colBuff, GL_STATIC_DRAW);
//		glEnableVertexAttribArray(1);
//		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
//		glDisableVertexAttribArray(0);
//		unbindVertexBuffer();
//		
//		unbindVertexArray();
//	}
//	
//	public void onRender() {
//		glBindVertexArray(vao);
//		bindElementBuffer();
//		glEnableVertexAttribArray(0);
//		glEnableVertexAttribArray(1);
//		glDrawElements(GL_TRIANGLES, elements, GL_UNSIGNED_SHORT, 0);
//		glDisableVertexAttribArray(1);
//		glDisableVertexAttribArray(0);
//		unbindElementBuffer();
//		glBindVertexArray(0);
//	}
//	
//	public void destroy() {
//		glDisableVertexAttribArray(0);
//		glDisableVertexAttribArray(1);
//		unbindVertexArray();
//		unbindVertexBuffer();
//		unbindElementBuffer();
//		glDeleteVertexArrays(vao);
//		glDeleteBuffers(vbo);
//		glDeleteBuffers(ebo);
//		glDeleteBuffers(cbo);
//	}
//	
//	public void bindVertexArray() {
//		glBindVertexArray(vao);
//	}
//	
//	public void bindVertexBuffer() {
//		glBindBuffer(GL_ARRAY_BUFFER, vbo);
//	}
//	
//	public void bindElementBuffer() {
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
//	}
//	
//	public static void unbindVertexArray() {
//		glBindVertexArray(0);
//	}
//	
//	public static void unbindVertexBuffer() {
//		glBindBuffer(GL_ARRAY_BUFFER, 0);
//	}
//	
//	public static void unbindElementBuffer() {
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
//	}
	
}