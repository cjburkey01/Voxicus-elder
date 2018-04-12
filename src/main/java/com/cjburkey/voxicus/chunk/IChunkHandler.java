package com.cjburkey.voxicus.chunk;

import java.util.Random;
import javax.annotation.Nonnull;
import org.joml.Vector3i;
import com.cjburkey.voxicus.block.Block;
import com.cjburkey.voxicus.block.BlockState;
import com.cjburkey.voxicus.generation.IChunkGenerator;

public interface IChunkHandler {
	
	/**
	 * Retrieves the chunk at the position. <b>This should never return {@code null}</b>
	 * @param pos The position of the chunk in chunk coordinates
	 */
	@Nonnull
	Chunk getChunk(Vector3i pos);
	
	/**
	 * Retrieves the position of the chunk that contains the specified block
	 * @param pos The position of the block
	 */
	@Nonnull
	Vector3i getChunkPosFromBlock(Vector3i pos);
	
	/**
	 * Retrieves the generator used for this world
	 */
	@Nonnull
	IChunkGenerator getGenerator();
	
	/**
	 * Get the generation seed for this handler
	 */
	long getSeed();
	
	/**
	 * Get a pre-seeded and used random value for this handler
	 */
	Random getRandom();
	
	/**
	 * Adds an action to the queue of actions to be executed on the world's thread
	 */
	void addSynchronousAction(Runnable r);
	
	/**
	 * Ticks the world and executes a synchronous action
	 */
	void tick();
	
	/**
	 * Replaces the block at the provided position with the provided block
	 */
	void setBlock(Block block, Vector3i pos);
	
	/**
	 * Removes the block at the specified position
	 */
	void deleteBlock(Vector3i pos);
	
	/**
	 * Re-renders the chunk at the position
	 */
	void updateChunk(Vector3i pos, boolean updateNeighbors);
	
	/**
	 * Retrieves a specific block from some chunk in the chunk handler
	 */
	BlockState getBlockFromPos(Vector3i pos);
	
	/**
	 * Retrieves a specific block relative to the specified chunk in the chunk handler
	 */
	BlockState getBlock(Vector3i chunk, Vector3i pos);
	
	/**
	 * Check whether the block at this position is not a full cube
	 */
	boolean getIsTransparentBlockAt(Vector3i block);
	
	/**
	 * Check whether the block at this position relative to the supplied chunk is not a full cube
	 */
	boolean getIsTransparentBlockAt(Vector3i chunk, Vector3i block);
	
}