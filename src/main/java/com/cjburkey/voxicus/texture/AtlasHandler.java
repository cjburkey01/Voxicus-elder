package com.cjburkey.voxicus.texture;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2i;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.event.EventRegistryTexture;
import com.cjburkey.voxicus.game.Game;
import com.cjburkey.voxicus.resource.Resource;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AtlasHandler {
	
	public static final int textureSize = 64;
	
	private static int pWidth;
	private static int width;
	private static TextureAtlas texture;
	private static BiMap<Integer, Resource> texPos = HashBiMap.create();
	
	public static void registerTextures() {
		Debug.log("Retrieving a list of registered textures");
		List<Resource> textures = new ArrayList<>();
		Game.getGlobalEventHandler().triggerEvent(new EventRegistryTexture(textures));
		Debug.log("Retrieved list of textures: {}", textures.size());
		while (width * width < textures.size()) {
			width ++;
		}
		pWidth = width * textureSize;
		Debug.log("Generating atlas: {}x{}", pWidth, pWidth);
		texture = new TextureAtlas(pWidth, textureSize);
		Debug.log("Adding images to atlas");
		int i = 0;
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < width; y ++) {
				texPos.put(x * width + y, textures.get(i));
				texture.addImage(x, y, textures.get(i));
//				Debug.log("{} at {}, {} is {}", i, x, y, textures.get(i).toString());
				i ++;
			}
		}
		texture.finish();
		Debug.log("Generated atlas");
	}
	
	public static TextureAtlas getTexture() {
		return texture;
	}
	
	public static Vector2i getTexture(Resource r) {
		return getPos(texPos.inverse().get(r));
	}
	
	public static Resource getTexture(Vector2i pos) {
		return texPos.get(getIndex(pos));
	}
	
	public static int getWidth() {
		return width * 2;
	}
	
	private static int getIndex(Vector2i pos) {
		return pos.x * width + pos.y;
	}
	
	private static Vector2i getPos(int i) {
		int x = Util.floorDiv(i, width);
		return new Vector2i(x, i - x * width);
	}
	
}