package com.cjburkey.voxicus.component;

import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.mesh.ChunkMesher;

public class ComponentChunk extends ObjectComponent {
	
	private Chunk chunk;
	private ComponentMesh mesh;
	
	public ComponentChunk(Chunk chunk) {
		this.chunk = chunk;
	}
	
	protected void onInit() {
		if (chunk == null) {
			Debug.error("Chunk was not set for instantiated chunk");
		}
		Debug.log("Instantiate chunk: {}, {}, {}", chunk.getChunkPos().x, chunk.getChunkPos().y, chunk.getChunkPos().z);
		mesh = getParentObj().addComponent(new ComponentMesh());
		mesh.setMesh(ChunkMesher.generateMeshForChunk(chunk));
		getParentObj().transform.position.set(chunk.getChunkPos()).mul(Chunk.SIZE);
	}
	
}