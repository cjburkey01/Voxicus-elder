package com.cjburkey.voxicus.chunk;

import java.util.Random;
import javax.annotation.Nonnull;
import org.joml.Vector3i;
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
	
}