package com.cjburkey.voxicus.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.FloatBuffer;
import java.util.List;
import org.joml.Vector3f;
import com.cjburkey.voxicus.Voxicus;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.shader.ShaderProgram;

public class MeshColor extends Mesh {
	
	private int cbo;
	
	public MeshColor() {
		cbo = glGenBuffers();
	}
	
	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector3f> colors) {
		super.setVertices(verts, inds);
		
		FloatBuffer colBuff = Util.vec3fBuffer(colors.toArray(new Vector3f[colors.size()]));
		
		bindVertexArray();
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, colBuff, GL_STATIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		glDisableVertexAttribArray(1);
		unbindVertexBuffer();
		memFree(colBuff);
		unbindVertexArray();
	}
	
	public ShaderProgram getShader() {
		return Voxicus.getGame().shaderColored;
	}
	
	public void onDestroy() {
		super.onDestroy();
		
		glDisableVertexAttribArray(1);
		glDeleteBuffers(cbo);
		
		cbo = 0;
	}
	
	public void bindColorBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
	}
	
	protected void onRenderCall() {
		glEnableVertexAttribArray(1);
		super.onRenderCall();
		glDisableVertexAttribArray(1);
	}
	
}