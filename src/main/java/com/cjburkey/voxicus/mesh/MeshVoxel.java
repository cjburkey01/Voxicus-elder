package com.cjburkey.voxicus.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.FloatBuffer;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.voxicus.Voxicus;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.shader.ShaderProgram;
import com.cjburkey.voxicus.texture.AtlasHandler;
import com.cjburkey.voxicus.texture.Texture;

public class MeshVoxel extends Mesh {
	
	private Texture texture;
	private int nbo;
	private int obo;
	private int pbo;
	
	public MeshVoxel() {
		nbo = glGenBuffers();
		obo = glGenBuffers();
		pbo = glGenBuffers();
	}
	
	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector3f> normals, List<Vector2f> offsets, List<Vector3f> pos) {
		super.setVertices(verts, inds);
		
		FloatBuffer nBuff = Util.vec3fBuffer(normals.toArray(new Vector3f[normals.size()]));
		FloatBuffer oBuff = Util.vec2fBuffer(offsets.toArray(new Vector2f[offsets.size()]));
		FloatBuffer pBuff = Util.vec3fBuffer(pos.toArray(new Vector3f[pos.size()]));
		
		bindVertexArray();
		bindNormalBuffer();
		glBufferData(GL_ARRAY_BUFFER, nBuff, GL_STATIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		glDisableVertexAttribArray(1);
		unbindVertexBuffer();
		memFree(nBuff);
		
		bindOffsetBuffer();
		glBufferData(GL_ARRAY_BUFFER, oBuff, GL_STATIC_DRAW);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
		glDisableVertexAttribArray(2);
		unbindVertexBuffer();
		memFree(oBuff);
		
		bindPosBuffer();
		glBufferData(GL_ARRAY_BUFFER, pBuff, GL_STATIC_DRAW);
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
		glDisableVertexAttribArray(3);
		unbindVertexBuffer();
		memFree(pBuff);
		unbindVertexArray();
	}
	
	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector3f> normals, List<Vector2f> offsets, List<Vector3f> pos, Texture texture) {
		setTexture(texture);
		setMesh(verts, inds, normals, offsets, pos);
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public ShaderProgram getShader() {
		return Voxicus.getGame().shaderVoxel;
	}
	
	public void onDestroy() {
		super.onDestroy();
		
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDeleteBuffers(nbo);
		glDeleteBuffers(obo);
		glDeleteBuffers(pbo);
		
		nbo = 0;
		obo = 0;
		pbo = 0;
	}
	
	public void bindNormalBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, nbo);
	}
	
	public void bindOffsetBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, obo);
	}
	
	public void bindPosBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, pbo);
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	protected void onRenderCall() {
		getShader().setUniform("tileSize", 1.0f / (float) AtlasHandler.getWidth());
		texture.bindTexture();
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		super.onRenderCall();
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(1);
		Texture.unbindTexture();
	}
	
}