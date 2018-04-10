package com.cjburkey.voxicus.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import org.joml.Vector3i;
import com.cjburkey.voxicus.component.ComponentChunk;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.generation.IChunkGenerator;
import com.cjburkey.voxicus.world.Scene;

public class World implements IChunkHandler {
	
	public final long seed;
	public final Random random;
	private final Map<Vector3i, ChunkGenPair> chunks = new HashMap<>();
	private final Map<Vector3i, ComponentChunk> instantiated = new HashMap<>();
	private final IChunkGenerator chunkGenerator;
	private final Stack<Runnable> onDoneQueue = new Stack<>();
	
	public World(IChunkGenerator chunkGenerator) {
		this(new Random().nextInt() / 200, chunkGenerator);
	}
	
	public World(long seed, IChunkGenerator chunkGenerator) {
		this.seed = seed;
		this.random = new Random(seed);
		this.chunkGenerator = chunkGenerator;
		
		Debug.log("World seed: {}", seed);
	}
	
	public void addSynchronousAction(Runnable r) {
		onDoneQueue.push(r);
	}
	
	public void tick() {
		int i = 0;
		while (!onDoneQueue.isEmpty() && i < 20) {
			Runnable r = onDoneQueue.pop();
			if (r != null) {
				r.run();
			}
			i ++;
		}
	}
	
	public void exit() {
		ThreadedChunkGeneration.stopSystem();
	}
	
	public Chunk getChunk(Vector3i pos) {
		return getChunkGenPair(pos).chunk;
	}
	
	private ChunkGenPair getChunkGenPair(Vector3i pos) {
		ChunkGenPair cp = chunks.get(pos);
		if (cp == null) {
			cp = new ChunkGenPair(new Chunk(this, pos));
			chunks.put(pos, cp);
		}
		return cp;
	}
	
	public Vector3i getChunkPosFromBlock(Vector3i pos) {
		return Util.floorDiv(pos, Chunk.SIZE);
	}
	
	public void spawnAround(Vector3i pos, int size) {
		Vector3i p = new Vector3i().zero();
		for (p.z = -size; p.z <= size; p.z ++) {
			for (p.y = -size; p.y <= size; p.y ++) {
				for (p.x = -size; p.x <= size; p.x ++) {
					Vector3i ps = Util.add(p, pos);
					tryGenerateChunk(Util.add(p, pos), () -> spawnChunk(ps));
				}
			}
		}
	}
	
	public void tryGenerateChunk(Vector3i chunkPos, Runnable onDone) {
		if (!ThreadedChunkGeneration.getHasStarted()) {
			ThreadedChunkGeneration.startSystem();
		}
		ThreadedChunkGeneration.addChunk(this, chunkPos, onDone);
	}
	
	public void spawnChunk(Vector3i chunkPos) {
		if (instantiated.containsKey(chunkPos)) {
			return;
		}
		Chunk c = getChunk(chunkPos);
		ComponentChunk cc = Scene.getActive().addObject("Chunk " + chunkPos).addComponent(new ComponentChunk(c));
		if (cc == null) {
			Debug.warn("Failed to spawn chunk: {}", chunkPos);
			return;
		}
		instantiated.put(chunkPos, cc);
	}
	
	public void despawnChunk(Vector3i chunkPos) {
		if (!instantiated.containsKey(chunkPos)) {
			Debug.warn("Chunk already despawned: " + chunkPos);
			return;
		}
		instantiated.get(chunkPos).getParentObj().destroy();
		instantiated.remove(chunkPos);
	}
	
	public static class ChunkGenPair {
		
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
	
	public IChunkGenerator getGenerator() {
		return chunkGenerator;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public Random getRandom() {
		return random;
	}
	
}