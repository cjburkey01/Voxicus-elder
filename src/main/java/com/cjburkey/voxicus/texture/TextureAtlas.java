package com.cjburkey.voxicus.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.joml.Vector2i;
import com.cjburkey.voxicus.core.Debug;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureAtlas extends Texture {
	
	/**
	 * Width and height are in pixels
	 */
	public TextureAtlas(String path, int width, int height) {
		super(path, new Object[] { width, height });
	}
	
	// Overridden, we don't want to use the default
	protected void init(Object... data) {
		glActiveTexture(GL_TEXTURE0);
		texture = glGenTextures();
		bindTexture();
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);	// 1 byte per component
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,  GL_NEAREST_MIPMAP_LINEAR);	// Pixel perfect (with mipmapping)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE); 
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (int) data[0], (int) data[1], 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
	}
	
	public boolean addTexture(Vector2i loc, String path) {
		try {
			InputStream is = TextureAtlas.class.getResourceAsStream(path);
			if (is == null) {
				throw new FileNotFoundException("Could not locate texture file: " + path);
			}
			PNGDecoder img = new PNGDecoder(is);
			ByteBuffer buff = ByteBuffer.allocateDirect(4 * img.getWidth() * img.getHeight());
			img.decode(buff, img.getWidth() * 4, Format.RGBA);
			buff.flip();

			bindTexture();
			glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,  GL_NEAREST_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
			glTexSubImage2D(GL_TEXTURE_2D, 0, loc.x * AtlasHandler.TEXTURE_SIZE, loc.y * AtlasHandler.TEXTURE_SIZE, AtlasHandler.TEXTURE_SIZE, AtlasHandler.TEXTURE_SIZE, GL_RGBA, GL_UNSIGNED_BYTE, buff);
			return true;
		} catch (Exception e) {
			Debug.error("Unable to add subtexture from path: {}", path);
			Debug.error(e, false);
		}
		return false;
	}
	
	public void finishAtlas() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		glGenerateMipmap(GL_TEXTURE_2D);
		isCreated = true;
	}
	
}