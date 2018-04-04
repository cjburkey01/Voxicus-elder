package com.cjburkey.voxicus.event;

import java.util.List;
import com.cjburkey.voxicus.resource.Resource;

public class EventRegistryTexture implements IEvent {
	
	private final List<Resource> textures;
	
	public EventRegistryTexture(List<Resource> textureList) {
		textures = textureList;
	}
	
	public String getName() {
		return getClass().getSimpleName();
	}
	
	public boolean getCanCancel() {
		return false;
	}
	
	public boolean getIsCancelled() {
		return false;
	}
	
	public void cancel() {
	}
	
	public void addTexture(Resource path) {
		if (!textures.contains(path)) {
			textures.add(path);
		}
	}
	
}