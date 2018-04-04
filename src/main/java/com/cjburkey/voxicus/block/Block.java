package com.cjburkey.voxicus.block;

import com.cjburkey.voxicus.resource.Resource;

public class Block {
	
	public final Resource texture = new Resource("voxicus", "texture/terrain/main.png");
	
	public boolean isFullBlock(BlockState state) {
		return true;
	}
	
}