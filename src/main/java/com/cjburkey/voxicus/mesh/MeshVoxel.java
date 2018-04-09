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
	private int tbo;
	
	public MeshVoxel() {
		nbo = glGenBuffers();
		tbo = glGenBuffers();
	}
	
	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector3f> normals, List<Vector2f> tileTexturePos) {
		super.setVertices(verts, inds);
		
		FloatBuffer nBuff = Util.vec3fBuffer(normals.toArray(new Vector3f[normals.size()]));
		FloatBuffer tBuff = Util.vec2fBuffer(tileTexturePos.toArray(new Vector2f[tileTexturePos.size()]));
		
		bindVertexArray();
		bindNormalBuffer();
		glBufferData(GL_ARRAY_BUFFER, nBuff, GL_STATIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		glDisableVertexAttribArray(1);
		unbindVertexBuffer();
		memFree(nBuff);
		
		bindTileBuffer();
		glBufferData(GL_ARRAY_BUFFER, tBuff, GL_STATIC_DRAW);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
		glDisableVertexAttribArray(2);
		unbindVertexBuffer();
		memFree(tBuff);
		unbindVertexArray();
	}
	
	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector3f> normals, List<Vector2f> tileTexturePos, Texture texture) {
		setTexture(texture);
		setMesh(verts, inds, normals, tileTexturePos);
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
		glDeleteBuffers(nbo);
		glDeleteBuffers(tbo);
		
		nbo = 0;
		tbo = 0;
	}
	
	public void bindNormalBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, nbo);
	}
	
	public void bindTileBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	protected void onRenderCall() {
		getShader().setUniform("atlasPreDoubleWidth", AtlasHandler.getWidth() / 2);
		texture.bindTexture();
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		super.onRenderCall();
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(1);
		Texture.unbindTexture();
	}
	
}