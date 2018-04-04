package com.cjburkey.voxicus.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.resource.Resource;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureArray extends Texture {
	
	private final int textureCount;
	
	public TextureArray(String path, int textureWidth, int textureHeight, Resource[] textures) {
		super(path, new Object[] { textureWidth, textureHeight, textures });
		this.textureCount = textures.length;
	}
	
	public int getTextureCount() {
		return textureCount;
	}
	
	protected void init(Object... data) {
		int w = (int) data[0];
		int h = (int) data[1];
		Resource[] images = (Resource[]) data[2];
		if (images.length > glGetInteger(GL_MAX_ARRAY_TEXTURE_LAYERS)) {
			throw new RuntimeException("There are too many textures registered to the texture atlas to generate a texture array");
		}
		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D_ARRAY, texture);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);	// 1 byte per component
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);	// Pixel perfect (with mipmapping)
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, GL_RGBA, w, h, images.length, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
		try {
			for (int i = 0; i < images.length; i ++) {
				InputStream is = Texture.class.getResourceAsStream(images[i].getFullPath());
				if (is == null) {
					throw new FileNotFoundException("Could not locate texture file: " + images[i].getFullPath());
				}
				PNGDecoder img = new PNGDecoder(is);
				ByteBuffer buff = ByteBuffer.allocateDirect(4 * img.getWidth() * img.getHeight());
				img.decode(buff, img.getWidth() * 4, Format.RGBA);
				glPixelStorei(GL_UNPACK_ALIGNMENT, 4);	// 1 byte per component
				buff.flip();
				glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, w, h, 1, GL_RGBA, GL_UNSIGNED_BYTE, buff);
				Debug.log("{} => {}", i, images[i].getFullPath());
			}
			
			glGenerateMipmap(GL_TEXTURE_2D_ARRAY);
			
			isCreated = true;
		} catch (Exception e) {
			Debug.error("Unable to create texture from path: {}", path);
			Debug.error(e, true);
		}
	}
	
}