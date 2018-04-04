package com.cjburkey.voxicus.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.IntBuffer;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.voxicus.Voxicus;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.shader.ShaderProgram;
import com.cjburkey.voxicus.texture.Texture;
import com.cjburkey.voxicus.texture.TextureArray;

public class MeshTextureArray extends MeshTexture {
	
	private int tbo;
	
	public MeshTextureArray() {
		tbo = glGenBuffers();
	}
	
	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector2f> uvs, List<Integer> textures) {
		super.setMesh(verts, inds, uvs);
		
		IntBuffer tBuff = Util.iBuffer(textures);
		
		bindVertexArray();
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, tBuff, GL_STATIC_DRAW);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 1, GL_INT, false, 0, 0);
		glDisableVertexAttribArray(2);
		unbindVertexBuffer();
		memFree(tBuff);
		unbindVertexArray();
	}
	
	public void setMesh(List<Vector3f> verts, List<Short> inds, List<Vector2f> uvs, List<Integer> textures, TextureArray texture) {
		setTexture(texture);
		setMesh(verts, inds, uvs, textures);
	}
	
	public ShaderProgram getShader() {
		return Voxicus.getGame().shaderTextureArray;
	}
	
	public void onDestroy() {
		super.onDestroy();
		
		glDisableVertexAttribArray(2);
		glDeleteBuffers(tbo);
		
		tbo = 0;
	}
	
	public void bindTextureBuffer() {
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
	}
	
	protected void onRenderCall() {
		if (!getTexture().getClass().equals(TextureArray.class)) {
			throw new RuntimeException("Invalid texture: not a texture array");
		}
		getTexture().bindTexture();
		glEnableVertexAttribArray(2);
		super.onRenderCall();
		glDisableVertexAttribArray(2);
		Texture.unbindTexture();
	}
	
}