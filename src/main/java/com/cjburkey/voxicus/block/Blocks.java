package com.cjburkey.voxicus.block;

import com.cjburkey.voxicus.resource.Resource;

public class Blocks {
	
	public static Block blockStone;
	public static Block blockGrass;
	public static Block blockDirt;
	
	public static void registerBlocks() {
		blockStone = new Block(new Resource("voxicus", "texture/terrain/blockStone.png"));
		blockGrass = new Block(new Resource("voxicus", "texture/terrain/blockGrass.png"));
		blockDirt = new Block(new Resource("voxicus", "texture/terrain/blockDirt.png"));
	}
	
}