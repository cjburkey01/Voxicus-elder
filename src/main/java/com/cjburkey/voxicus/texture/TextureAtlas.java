package com.cjburkey.voxicus.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.stb.STBImageResize.*;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.resource.Resource;

public class TextureAtlas extends Texture {
	
	protected int width;
	protected int size;
	
	public TextureAtlas(int width, int textureSize) {
		this.width = width;
		size = textureSize;
		
		glBindTexture(GL_TEXTURE_2D, texture);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);	// 1 byte per component
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);	// Pixel perfect (with mipmapping)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 6);
		
		for (int lod = 0; lod < 7; lod ++) {
			int w = (width * 2) / (int) Math.pow(2, lod);
			glTexImage2D(GL_TEXTURE_2D, lod, GL_RGBA, w, w, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
		}
		
		isCreated = false;
	}
	
	public void addImage(int posX, int posY, Resource image) {
		for (int x = 0; x < 2; x ++) {
			for (int y = 0; y < 2; y ++) {
				// Quad-texture method to prevent lines with multisampling and mipmapping
				ByteBuffer img = Texture.getBufferFromPureBytes(Texture.getPureBytesFromResource(image.getFullPath()), new int[1],new int[1],new int[1]);
				for (int lod = 0; lod < 7; lod ++) {
					addSubImage((posX * 2 + x) * size, (posY * 2 + y) * size, img, lod);
				}
			}
		}
	}
	
	protected void addSubImage(int x, int y, ByteBuffer img, int lod) {
		int i = (int) Math.pow(2, lod);
		int s = size / i;
		ByteBuffer image = BufferUtils.createByteBuffer(s * s * 4);
		img.position(0);
		Debug.log("{},{} at {} with {}", x / i, y / i, lod, s);
		if (i != 1) {
			stbir_resize(img, size, size, 0, image, s, s, 0, STBIR_TYPE_UINT8, 4, 3, 0, STBIR_EDGE_CLAMP, STBIR_EDGE_CLAMP, STBIR_FILTER_BOX, STBIR_FILTER_BOX, STBIR_COLORSPACE_LINEAR);
		} else {
			image.put(img).flip();
		}
		
		glBindTexture(GL_TEXTURE_2D, texture);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexSubImage2D(GL_TEXTURE_2D, lod, x / i, y / i, s, s, GL_RGBA, GL_UNSIGNED_BYTE, image);
	}
	
	public void finish() {
		isCreated = true;
	}
	
}