package com.cjburkey.voxicus.graphic;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import com.cjburkey.voxicus.core.Debug;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Texture {
	
	private boolean isCreated;
	private int texture;
	
	public Texture(String path) {
		try {
			InputStream is = Texture.class.getResourceAsStream(path);
			if (is == null) {
				throw new FileNotFoundException("Could not locate texture file: " + path);
			}
			PNGDecoder img = new PNGDecoder(is);
			ByteBuffer buff = ByteBuffer.allocateDirect(4 * img.getWidth() * img.getHeight());
			img.decode(buff, img.getWidth() * 4, Format.RGBA);
			buff.flip();
			
			texture = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texture);
			glPixelStorei(GL_TEXTURE_2D, 1);	// 1 byte per component
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);	// Pixel perfect (with mipmapping)
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buff);
			glGenerateMipmap(GL_TEXTURE_2D);
			
			isCreated = true;
		} catch (Exception e) {
			Debug.error("Unable to ");
			Debug.error(e, true);
		}
	}
	
	public void destroy() {
		glDeleteTextures(texture);
		isCreated = false;
	}
	
	public boolean getIsCreated() {
		return isCreated;
	}
	
	public void bindTexture() {
		if (!isCreated) {
			return;
		}
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public static void unbindTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
}