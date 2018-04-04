package com.cjburkey.voxicus.generation;

import java.util.Random;
import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.chunk.IChunkHandler;

public interface IChunkGenerator {
	
	/**
	 * Generates the chunk from scratch
	 * @param seed The seed of the world
	 * @param chunkHandler The world responsible for the generation
	 * @param random A pre-seeded random value
	 * @param chunk The chunk to generate
	 */
	void generate(long seed, Random random, IChunkHandler chunkHandler, Chunk chunk);
	
}