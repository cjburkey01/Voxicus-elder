package com.cjburkey.voxicus.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.joml.Vector3i;
import com.cjburkey.voxicus.component.ComponentChunk;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.generation.IChunkGenerator;
import com.cjburkey.voxicus.world.Scene;

public class World implements IChunkHandler {
	
	public final long seed;
	public final Random random;
	private final Map<Integer, ChunkGenPair> chunks = new HashMap<>();
	private final Map<Integer, ComponentChunk> instantiated = new HashMap<>();
	private final IChunkGenerator chunkGenerator;
	
	public World(IChunkGenerator chunkGenerator) {
		this(new Random().nextInt() / 200, chunkGenerator);
	}
	
	public World(long seed, IChunkGenerator chunkGenerator) {
		this.seed = seed;
		this.random = new Random(seed);
		this.chunkGenerator = chunkGenerator;
	}
	
	public Chunk getChunk(Vector3i pos) {
		return getChunkGenPair(pos).chunk;
	}
	
	public Chunk getAndGenerateChunk(Vector3i pos) {
		ChunkGenPair cp = getChunkGenPair(pos);
		if (!cp.generated) {
			chunkGenerator.generate(seed, random, this, cp.chunk);
			cp.generated = true;
		}
		return cp.chunk;
	}
	
	private ChunkGenPair getChunkGenPair(Vector3i pos) {
		int i = getIndexOfPos(pos);
		ChunkGenPair cp = chunks.get(i);
		if (cp == null) {
			cp = new ChunkGenPair(new Chunk(pos));
			chunks.put(i, cp);
		}
		return cp;
	}
	
	public Vector3i getChunkPosFromBlock(Vector3i pos) {
		return Util.floorDiv(pos, Chunk.SIZE);
	}
	
	@Deprecated
	public void spawnAround(Vector3i pos, int size) {
		Vector3i p = new Vector3i().zero();
		for (p.z = -size; p.z <= size; p.z ++) {
			for (p.y = -size; p.y <= size; p.y ++) {
				for (p.x = -size; p.x <= size; p.x ++) {
					spawnChunk(p.add(pos, new Vector3i()));
				}
			}
		}
	}
	
	public void spawnChunk(Vector3i chunkPos) {
		Chunk c = getAndGenerateChunk(chunkPos);
		ComponentChunk cc = Scene.getActive().addObject("Chunk " + chunkPos).addComponent(new ComponentChunk(c));
		if (cc == null) {
			Debug.warn("Failed to spawn chunk: {}", chunkPos);
			return;
		}
		instantiated.put(getIndexOfPos(chunkPos), cc);
	}
	
	public void despawnChunk(Vector3i chunkPos) {
		int i = getIndexOfPos(chunkPos);
		if (!instantiated.containsKey(i)) {
			Debug.warn("Chunk already despawned: " + chunkPos);
			return;
		}
		instantiated.get(i).getParentObj().destroy();
		instantiated.remove(i);
	}
	
	private int getIndexOfPos(Vector3i pos) {
		return pos.z * Chunk.SIZE * Chunk.SIZE + pos.y * Chunk.SIZE + pos.x;
	}
	
	private static class ChunkGenPair {
		
		public final Chunk chunk;
		public boolean generated;
		
		private ChunkGenPair(Chunk chunk) {
			this(chunk, false);
		}
		
		private ChunkGenPair(Chunk chunk, boolean generated) {
			this.chunk = chunk;
			this.generated = generated;
		}
		
	}
	
}