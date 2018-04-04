package com.cjburkey.voxicus.texture;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector2i;
import com.cjburkey.voxicus.core.Bounds;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.event.EventRegistryTexture;
import com.cjburkey.voxicus.game.Game;
import com.cjburkey.voxicus.resource.Resource;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AtlasHandler {
	
	public static AtlasHandler instance;
	public static final int TEXTURE_SIZE = 64;
	
	private int atlasTextureWidth;
	private TextureAtlas texture;
	private final BiMap<Vector2i, Resource> textures = HashBiMap.create();
	
	public AtlasHandler() {
		instance = this;
	}
	
	public void gatherTextures() {
		Debug.log("Gathering textures");
		List<Resource> res = new ArrayList<>();
		Game.getGlobalEventHandler().triggerEvent(new EventRegistryTexture(res));
		if (res.size() < 1) {
			Debug.log("No textures found for the atlas, skipping creation");
			return;
		}
		Debug.log("Processing {} textures", res.size());
		while (atlasTextureWidth * atlasTextureWidth < res.size()) {
			atlasTextureWidth ++;
		}
		if (atlasTextureWidth * TEXTURE_SIZE > Game.maxTextureSize) {
			Debug.error("Too many textures for the texture atlas. Predicted size: {}^2. Maximum size: {}^2", atlasTextureWidth * TEXTURE_SIZE, Game.maxTextureSize);
		}
		Debug.log("Texture atlas size: {}", atlasTextureWidth * TEXTURE_SIZE);
		generateAtlasTexture(res);
	}
	
	private void generateAtlasTexture(List<Resource> textures) {
		int wh = TEXTURE_SIZE * atlasTextureWidth;
		texture = new TextureAtlas("", wh, wh);
		Vector2i pos = new Vector2i(0, 0);
		for (Resource texture : textures) {
			Vector2i p = new Vector2i(pos);
			if (this.texture.addTexture(p, texture.getFullPath())) {
				this.textures.put(p, texture);
				pos.x ++;
				if (pos.x >= atlasTextureWidth) {
					pos.x = 0;
					pos.y ++;
				}
			} else {
				Debug.error("Failed to add texture: {}", texture);
			}
		}
		texture.finishAtlas();
		Debug.log("Generated atlas");
	}
	
	public Vector2i getTexturePosition(Resource res) {
		return new Vector2i(textures.inverse().get(res));
	}
	
	public Bounds getTextureBounds(Resource res) {
		float w = 1.0f / atlasTextureWidth;
		Vector2i tmp = getTexturePosition(res);
		Vector2f p = new Vector2f(tmp.x, tmp.y).mul(w, new Vector2f());
		return new Bounds(p, new Vector2f(w, w));
	}
	
	public Resource getTextureAtPosition(Vector2i pos) {
		return textures.get(pos);
	}
	
	public Texture getTexture() {
		return texture;
	}
	
}