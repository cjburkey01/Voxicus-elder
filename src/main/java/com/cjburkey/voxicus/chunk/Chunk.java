package com.cjburkey.voxicus.chunk;

import org.joml.Vector3i;
import com.cjburkey.voxicus.block.Block;
import com.cjburkey.voxicus.block.BlockState;
import com.cjburkey.voxicus.core.Util;

public class Chunk {
	
	public static final int SIZE = 16;
	
	private Vector3i chunkPos;
	private BlockState[] blocks;
	
	public Chunk(Vector3i chunkPos) {
		this.chunkPos = new Vector3i(chunkPos);
		blocks = new BlockState[SIZE * SIZE * SIZE];
	}
	
	public boolean getIsTransparentBlockAt(Vector3i pos) {
		BlockState state = getBlock(pos);
		return state == null || !state.getParent().isFullBlock(state);
	}
	
	public void setBlock(Vector3i pos, Block block) {
		if (!posInChunk(pos)) {
			return;
		}
		blocks[getIndex(pos)] = new BlockState(block, this, pos);
	}
	
	public BlockState getBlock(Vector3i pos) {
		if (!posInChunk(pos)) {
			return null;
		}
		return blocks[getIndex(pos)];
	}
	
	public boolean posInChunk(Vector3i pos) {
		return !(pos.x < 0 || pos.x >= 16 || pos.y < 0 || pos.y >= 16 || pos.z < 0 || pos.z >= 16);
	}
	
	// Z major major
	// Y major
	// X minor
	public int getIndex(Vector3i pos) {
		return pos.z * SIZE * SIZE + pos.y * SIZE + pos.x;
	}
	
	public Vector3i getPos(int index) {
		Vector3i out = new Vector3i();
		out.z = Util.floorDiv(index, SIZE * SIZE);
		index -= out.z * SIZE * SIZE;
		out.y = Util.floorDiv(index, SIZE);
		out.x = index - out.y * SIZE;
		return out;
	}
	
	public Vector3i getChunkPos() {
		return new Vector3i(chunkPos);
	}
	
}