package com.cjburkey.voxicus.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import com.cjburkey.voxicus.core.Debug;

public class Texture {
	
	protected int texture;
	protected boolean isCreated;
	
	protected Texture() {
		glActiveTexture(GL_TEXTURE0);
		texture = glGenTextures();
		isCreated = true;
	}
	
	public void destroy() {
		glDeleteTextures(texture);
		isCreated = false;
	}
	
	public boolean getIsCreated() {
		return isCreated;
	}
	
	public void bindTexture() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public static void unbindTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public static final byte[] getPureBytesFromResource(String path) {
		try {
			InputStream is = Texture.class.getResourceAsStream(path);
			if (is == null) {
				throw new FileNotFoundException("Could not locate texture file: " + path);
			}
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			Debug.error("Unable to get bytes from path: {}", path);
			Debug.error(e, true);
		}
		return new byte[0];
	}
	
	public static final Texture getFromResource(String path) {
		try {
			return getTextureFromPureBytes(getPureBytesFromResource(path));
		} catch (Exception e) {
			Debug.error("Unable to create texture from path: {}", path);
			Debug.error(e, true);
		}
		return null;
	}
	
	public static final ByteBuffer getBufferFromPureBytes(byte[] data, int[] w, int[] h, int[] comp) {
		ByteBuffer image = BufferUtils.createByteBuffer(data.length);
		image.put(data).flip();
		ByteBuffer buff = stbi_load_from_memory(image, w, h, comp, 0);
		return buff;
	}
	
	public static final Texture getTextureFromPureBytes(byte[] data) {
		Texture texture = new Texture();
		
		int[] w = new int[1];
		int[] h = new int[1];
		ByteBuffer img = getBufferFromPureBytes(data, w, h, new int[1]);
		
		glBindTexture(GL_TEXTURE_2D, texture.texture);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);	// 1 byte per component
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);	// Pixel perfect (with mipmapping)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w[0], h[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, img);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		return texture;
	}
	
}