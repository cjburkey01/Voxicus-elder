package com.cjburkey.voxicus.component;

import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.mesh.ChunkMesher;
import com.cjburkey.voxicus.mesh.MeshVoxel;

public class ComponentChunk extends ObjectComponent {
	
	private Chunk chunk;
	private MeshVoxel mesh;
	private ComponentMesh cMesh;
	
	public ComponentChunk(Chunk chunk) {
		this.chunk = chunk;
	}
	
	protected void onInit() {
		if (chunk == null) {
			Debug.error("Chunk was not set for instantiated chunk");
		}
		Debug.log("Instantiate chunk: {}, {}, {}", chunk.getChunkPos().x, chunk.getChunkPos().y, chunk.getChunkPos().z);
		cMesh = getParentObj().addComponent(new ComponentMesh());
		getParentObj().transform.position.set(chunk.getChunkPos()).mul(Chunk.SIZE);
		chunk.getWorld().updateChunk(chunk.getChunkPos(), true);
	}
	
	public void render() {
		if (mesh != null) {
			mesh.onDestroy();
		}
		mesh = ChunkMesher.generateMeshForChunk(chunk);
		cMesh.setMesh(mesh);
	}
	
}