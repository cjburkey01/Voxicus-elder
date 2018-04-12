package com.cjburkey.voxicus.generation;

import org.joml.Vector3i;
import com.cjburkey.voxicus.block.Blocks;
import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.chunk.IChunkHandler;
import com.cjburkey.voxicus.core.Debug;

public class ChunkGeneratorOverworld implements IChunkGenerator {
	
	public static final double cutoff = 0.2d;
	
	// Position variable caching for a semi-minor speedup.
	private final Vector3i pos = new Vector3i();
	
	public void generate(IChunkHandler chunkHandler, Chunk chunk) {
		pos.zero();
		Debug.log("Generating chunk: {}, {}, {}", chunk.getChunkPos().x, chunk.getChunkPos().y, chunk.getChunkPos().z);
		
		for (int i = 0; i < Chunk.SIZE * Chunk.SIZE * Chunk.SIZE; i ++) {
			pos.set(Chunk.getPosition(i));
			if (GeneratorHelper.getNoise(chunkHandler.getSeed(), chunk.getChunkPos(), pos) >= cutoff) {
				chunk.setBlock(pos, Blocks.blockStone);
			} else {
				chunk.setBlock(pos, null);
			}
		}
	}
	
}