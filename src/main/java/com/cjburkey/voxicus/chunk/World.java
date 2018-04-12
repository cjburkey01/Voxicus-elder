package com.cjburkey.voxicus.chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import org.joml.Vector3i;
import com.cjburkey.voxicus.block.Block;
import com.cjburkey.voxicus.block.BlockState;
import com.cjburkey.voxicus.component.ComponentChunk;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.event.EventPostObjectInit;
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
		
		Scene.getActive().getEventHandler().addListener(EventPostObjectInit.class, (e) -> ThreadedChunkGeneration.startSystem());
		
		Debug.log("World seed: {}", seed);
	}
	
	public void exit() {
		ThreadedChunkGeneration.stopSystem();
	}
	
	public void addSynchronousAction(Runnable r) {
		onDoneQueue.push(r);
	}
	
	public void tick() {
		int i = 0;
		while (!onDoneQueue.isEmpty() && i < 20) {
			Runnable r = onDoneQueue.pop();
			if (r == null) {
				continue;
			}
			r.run();
			i ++;
		}
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
					tryGenerateChunk(ps, () -> {
						getChunkGenPair(ps).generated = true;
						createChunkObject(ps);
					});
				}
			}
		}
	}
	
	private void tryGenerateChunk(Vector3i chunkPos, Runnable onDone) {
		if (getChunkGenPair(chunkPos).generated) {
			return;
		}
		ThreadedChunkGeneration.addChunk(this, chunkPos, onDone);
	}
	
	public void despawnChunk(Vector3i chunkPos) {
		if (!instantiated.containsKey(chunkPos)) {
			Debug.warn("Chunk already despawned: " + chunkPos);
			return;
		}
		instantiated.get(chunkPos).getParentObj().destroy();
		instantiated.remove(chunkPos);
	}
	
	public void updateChunk(Vector3i pos, boolean updateNeighbors) {
		Vector3i p = new Vector3i(pos);
		if (instantiated.containsKey(p)) {
			instantiated.get(p).render();
		}
		if (!updateNeighbors) {
			return;
		}
		
		// Update existing neighboring chunks to prevent boundaries in the meshes
		p.add(1, 0, 0);
		updateSingleChunk(p);
		p.set(pos).add(-1, 0, 0);
		updateSingleChunk(p);
		p.set(pos).add(0, 1, 0);
		updateSingleChunk(p);
		p.set(pos).add(0, -1, 0);
		updateSingleChunk(p);
		p.set(pos).add(0, 0, 1);
		updateSingleChunk(p);
		p.set(pos).add(0, 0, -1);
		updateSingleChunk(p);
	}
	
	private void updateSingleChunk(Vector3i pos) {
		if (instantiated.containsKey(pos)) {
			ComponentChunk cc = instantiated.get(pos);
			if (!cc.getHasInit()) {
				return;
			}
			cc.render();
		}
	}
	
	private void createChunkObject(Vector3i chunkPos) {
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
	
	public void setBlock(Block block, Vector3i pos) {
		Vector3i chunk = getChunkWithBlock(pos);
		ChunkGenPair c = getChunkGenPair(chunk);
		if (!c.generated) {
			tryGenerateChunk(chunk, () -> c.chunk.setBlock(pos, block));
			return;
		}
		c.chunk.setBlock(pos, block);
		updateChunk(chunk, true);
	}
	
	public void deleteBlock(Vector3i pos) {
		setBlock(null, pos);
	}
	
	public BlockState getBlockFromPos(Vector3i pos) {
		return getChunk(getChunkWithBlock(pos)).getBlock(getBlockWithinChunk(pos));
	}
	
	public BlockState getBlock(Vector3i chunk, Vector3i pos) {
		Vector3i block = getBlockPos(chunk, pos);
		Vector3i chunkPos = getChunkWithBlock(block);
		Vector3i blockPos = getBlockWithinChunk(block);
//		Debug.log("block: {}", block);
//		Debug.log("chunkPos: {}", chunkPos);
//		Debug.log("blockPos: {}", blockPos);
		return getChunk(chunkPos).getBlock(blockPos);
	}
	
	public static Vector3i getBlockPos(Vector3i chunk, Vector3i posInChunk) {
		return Util.add(Util.mul(new Vector3i(chunk), Chunk.SIZE), posInChunk);
	}
	
	public static Vector3i getChunkWithBlock(Vector3i block) {
		(block = new Vector3i(block)).x = Util.floorDiv(block.x, Chunk.SIZE);
		block.y = Util.floorDiv(block.y, Chunk.SIZE);
		block.z = Util.floorDiv(block.z, Chunk.SIZE);
		return block;
	}
	
	public static Vector3i getBlockWithinChunk(Vector3i block) {
		Vector3i chunk = getChunkWithBlock(block).mul(Chunk.SIZE);
		block = new Vector3i(block);
		while (block.x >= Chunk.SIZE) {
			block.x -= Chunk.SIZE;
			chunk.x += 1;
		}
		while (block.x < 0) {
			block.x += Chunk.SIZE;
			chunk.x -= 1;
		}
		while (block.y >= Chunk.SIZE) {
			block.y -= Chunk.SIZE;
			chunk.y += 1;
		}
		while (block.y < 0) {
			block.y += Chunk.SIZE;
			chunk.y -= 1;
		}
		while (block.z >= Chunk.SIZE) {
			block.z -= Chunk.SIZE;
			chunk.z += 1;
		}
		while (block.z < 0) {
			block.z += Chunk.SIZE;
			chunk.z -= 1;
		}
		return block;
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
	
	public boolean getIsTransparentBlockAt(Vector3i block) {
		BlockState at = getBlockFromPos(block);
		return at == null || at.getIsTransparent();
	}
	
	public boolean getIsTransparentBlockAt(Vector3i chunk, Vector3i block) {
		BlockState at = getBlock(chunk, block);
		return at == null || at.getIsTransparent();
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
	
}