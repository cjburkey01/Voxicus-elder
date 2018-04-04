package com.cjburkey.voxicus.block;

import com.cjburkey.voxicus.core.Bounds;
import com.cjburkey.voxicus.core.Util;

public class Block {
	
	public final Bounds uvBounds = Util.UV_DEF;
	
	public boolean isFullBlock(BlockState state) {
		return true;
	}
	
}