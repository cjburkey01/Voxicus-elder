package com.cjburkey.voxicus.chunk;

import javax.annotation.Nonnull;
import org.joml.Vector3i;

public interface IChunkHandler {
	
	/**
	 * Retrieves the chunk at the position. <b>This should never return {@code null}</b>
	 * @param pos The position of the chunk in chunk coordinates
	 */
	@Nonnull
	Chunk getChunk(Vector3i pos);
	
	/**
	 * Retrieves the chunk at the position and generates it if it has not yet been generated. <b>This should never return {@code null}</b>
	 * @param pos The position of the chunk in chunk coordinates
	 */
	@Nonnull
	Chunk getAndGenerateChunk(Vector3i pos);
	
	/**
	 * Retrieves the position of the chunk that contains the specified block
	 * @param pos The position of the block
	 */
	Vector3i getChunkPosFromBlock(Vector3i pos);
	
}