package com.cjburkey.voxicus.texture;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.event.EventRegistryTexture;
import com.cjburkey.voxicus.game.Game;
import com.cjburkey.voxicus.resource.Resource;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AtlasHandler {
	
	public static AtlasHandler instance;
	public static final int TEXTURE_SIZE = 64;
	
	private TextureArray texture;
	private final BiMap<Integer, Resource> textures = HashBiMap.create();
	
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
		int i = 0;
		for (Resource img : res) {
			textures.put(i ++, img);
		}
		
		// Generate the array of textures
		texture = new TextureArray("", TEXTURE_SIZE, TEXTURE_SIZE, res.toArray(new Resource[res.size()]));
	}
	
	public int getTextureId(Resource res) {
		return textures.inverse().get(res);
	}
	
	public Resource getTexture(int id) {
		return textures.get(id);
	}
	
	public TextureArray getTexture() {
		return texture;
	}
	
}