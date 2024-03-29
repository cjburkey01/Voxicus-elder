package com.cjburkey.voxicus.chunk;

import org.joml.Vector3i;
import com.cjburkey.voxicus.block.Block;
import com.cjburkey.voxicus.block.BlockState;
import com.cjburkey.voxicus.core.Util;

public class Chunk {
	
	public static final int SIZE = 16;
	
	private final IChunkHandler parent;
	private final Vector3i chunkPos = new Vector3i();
	private BlockState[] blocks;
	
	public Chunk(IChunkHandler parent, Vector3i chunkPos) {
		this.parent = parent;
		this.chunkPos.set(chunkPos);
		blocks = new BlockState[SIZE * SIZE * SIZE];
	}
	
	public boolean getIsAirBlock(Vector3i pos) {
		BlockState at = getBlock(pos);
		return at == null;
	}
	
	public void setBlock(Vector3i pos, Block block) {
		if (!posInChunk(pos)) {
			return;
		}
		blocks[getIndex(pos)] = (block == null) ? null : new BlockState(block, this, pos);
	}
	
	public BlockState getBlock(Vector3i pos) {
		if (!posInChunk(pos)) {
			return null;
		}
		return blocks[getIndex(pos)];
	}
	
	public BlockState getBlock(int i) {
		if (i < 0 || i > blocks.length) {
			return null;
		}
		return blocks[i];
	}
	
	public boolean posInChunk(Vector3i pos) {
		return !(pos.x < 0 || pos.x >= SIZE || pos.y < 0 || pos.y >= SIZE || pos.z < 0 || pos.z >= SIZE);
	}
	
	public Vector3i getChunkPos() {
		return new Vector3i(chunkPos);
	}
	
	public IChunkHandler getWorld() {
		return parent;
	}
	
	public static int getIndex(Vector3i pos) {
		return pos.z * SIZE * SIZE + pos.y * SIZE + pos.x;
	}
	
	public static Vector3i getPosition(int i) {
		int z = Util.floorDiv(i, SIZE * SIZE);
		i -= z * SIZE * SIZE;
		int y = Util.floorDiv(i, SIZE);
		return new Vector3i(i - y * SIZE, y, z);
	}
	
}