package com.cjburkey.voxicus.generation;

import java.util.Random;
import org.joml.Vector3i;
import com.cjburkey.voxicus.block.Blocks;
import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.chunk.IChunkHandler;
import com.cjburkey.voxicus.core.Debug;

public class ChunkGeneratorOverworld implements IChunkGenerator {
	
	public static final double cutoff = 0.2d;
	
	// Position variable caching for a semi-minor speedup.
	private final Vector3i pos = new Vector3i();
	
	public void generate(long seed, Random random, IChunkHandler chunkHandler, Chunk chunk) {
		pos.zero();
		Debug.log("Generating chunk: {}, {}, {}", chunk.getChunkPos().x, chunk.getChunkPos().y, chunk.getChunkPos().z);
		
		for (pos.z = 0; pos.z < Chunk.SIZE; pos.z ++) {
			for (pos.y = 0; pos.y < Chunk.SIZE; pos.y ++) {
				for (pos.x = 0; pos.x < Chunk.SIZE; pos.x ++) {
					if (GeneratorHelper.getNoise(seed, chunk.getChunkPos(), pos) >= cutoff) {
						chunk.setBlock(pos, Blocks.blockStone);
					} else {
						chunk.setBlock(pos, null);
					}
				}
			}
		}
	}
	
}