package com.cjburkey.voxicus.block;

import org.joml.Vector3i;
import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.core.DataHandler;

public class BlockState {
	
	private Block parent;
	private Vector3i chunkPos;
	private Vector3i posInChunk;
	private DataHandler dataHandler;
	
	public BlockState(Block parent, Chunk chunk, Vector3i posInChunk) {
		this(parent, chunk.getChunkPos(), posInChunk);
	}
	
	public BlockState(Block parent, Vector3i chunkPos, Vector3i posInChunk) {
		this(parent, chunkPos, posInChunk, new DataHandler());
	}
	
	public BlockState(Block parent, Vector3i chunkPos, Vector3i posInChunk, DataHandler dataHandler) {
		this.parent = parent;
		this.chunkPos = new Vector3i(chunkPos);
		this.posInChunk = new Vector3i(posInChunk);
		this.dataHandler = dataHandler;
	}
	
	public void onUpdate() {
		parent.onUpdate(this);
	}
	
	public Block getParent() {
		return parent;
	}
	
	public Vector3i getWorldPos() {
		return posInChunk.add(chunkPos.mul(Chunk.SIZE, new Vector3i()), new Vector3i());
	}
	
	public Vector3i getChunkPos() {
		return new Vector3i(chunkPos);
	}
	
	public Vector3i getPosInChunk() {
		return new Vector3i(posInChunk);
	}
	
	public DataHandler getDataHandler() {
		return dataHandler;
	}
	
	public boolean getIsTransparent() {
		return !parent.getIsFullBlock(this);
	}
	
}