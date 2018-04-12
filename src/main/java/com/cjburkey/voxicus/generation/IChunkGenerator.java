package com.cjburkey.voxicus.generation;

import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.chunk.IChunkHandler;

public interface IChunkGenerator {
	
	/**
	 * Generates the chunk from scratch
	 * @param chunkHandler The world responsible for the generation
	 * @param chunk The chunk to generate
	 */
	void generate(IChunkHandler chunkHandler, Chunk chunk);
	
}