package com.cjburkey.voxicus.generation;

import java.util.Random;
import org.joml.Vector3i;
import com.cjburkey.voxicus.block.Blocks;
import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.chunk.IChunkHandler;
import com.cjburkey.voxicus.core.Debug;

public class ChunkGeneratorOverworld implements IChunkGenerator {
	
	public static final double cutoff = 0.2d;
	
	// Position variable caching for a VERY minor speedup.
	private final Vector3i pos = new Vector3i();
	private final Vector3i cp = new Vector3i();
	
	public void generate(long seed, Random random, IChunkHandler chunkHandler, Chunk chunk) {
		cp.set(chunk.getChunkPos()).mul(Chunk.SIZE);
		pos.zero();
		Debug.log("On chunk {}, {}, {}", chunk.getChunkPos().x, chunk.getChunkPos().y, chunk.getChunkPos().z);
		for (pos.z = 0; pos.z < Chunk.SIZE; pos.z ++) {
			for (pos.y = 0; pos.y < Chunk.SIZE; pos.y ++) {
				for (pos.x = 0; pos.x < Chunk.SIZE; pos.x ++) {
//					Debug.log(pos.add(cp, new Vector3i()));
					if (GeneratorHelper.getNoise(seed, new Vector3i(pos.x + cp.x, pos.y + cp.y, pos.z + cp.z)) >= cutoff) {
						chunk.setBlock(pos, Blocks.blockStone);
					}
				}
			}
		}
	}
	
}