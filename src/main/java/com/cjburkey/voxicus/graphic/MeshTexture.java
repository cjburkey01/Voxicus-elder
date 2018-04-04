package com.cjburkey.voxicus.graphic;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.FloatBuffer;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.voxicus.Voxicus;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.texture.Texture;

public class MeshTexture extends Mesh {
	
	private Texture texture;
	private int uvbo;
	
	public MeshTexture() {
		uvbo = glGenBuffers();
	}
	
	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector2f> uvs) {
		super.setVertices(verts, inds);
		
		FloatBuffer uvBuff = Util.vec2fBuffer(uvs.toArray(new Vector2f[uvs.size()]));
		
		bindVertexArray();
		glBindBuffer(GL_ARRAY_BUFFER, uvbo);
		glBufferData(GL_ARRAY_BUFFER, uvBuff, GL_STATIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glDisableVertexAttribArray(1);
		unbindVertexBuffer();
		memFree(uvBuff);
		unbindVertexArray();
	}
	
	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector2f> uvs, Texture texture) {
		setTexture(texture);
		setMesh(verts, inds, uvs);
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	protected ShaderProgram getShader() {
		return Voxicus.getGame().shaderTextured;
	}
	
	public void onDestroy() {
		super.onDestroy();
		
		glDisableVertexAttribArray(1);
		glDeleteBuffers(uvbo);
		
		uvbo = 0;
	}
	
	public void bindUvBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, uvbo);
	}
	
	protected void onRenderCall() {
		texture.bindTexture();
		glEnableVertexAttribArray(1);
		super.onRenderCall();
		glDisableVertexAttribArray(1);
		Texture.unbindTexture();
	}
	
}