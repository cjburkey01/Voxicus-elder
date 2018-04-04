package com.cjburkey.voxicus.block;

import com.cjburkey.voxicus.resource.Resource;

public class Block {
	
	public final Resource texture;
	
	public Block(Resource res) {
		texture = res;
	}
	
	public boolean isFullBlock(BlockState state) {
		return true;
	}
	
}