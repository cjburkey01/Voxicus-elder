package com.cjburkey.voxicus.mesh;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.voxicus.block.BlockState;
import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.texture.AtlasHandler;

public class ChunkMesher {
	
	public static MeshVoxel generateMeshForChunk(Chunk chunk) {
		return generateMeshForChunk(new MeshVoxel(), chunk);
	}
	
	public static MeshVoxel generateMeshForChunk(MeshVoxel mesh, Chunk chunk) {
		List<Vector3f> verts = new ArrayList<>();
		List<Short> inds = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Vector2f> tiles = new ArrayList<>();
		
		Vector3i pos = new Vector3i().zero();
		for (pos.x = 0; pos.x < Chunk.SIZE; pos.x ++) {
			for (pos.y = 0; pos.y < Chunk.SIZE; pos.y ++) {
				for (pos.z = 0; pos.z < Chunk.SIZE; pos.z ++) {
					if (chunk.getIsAirBlock(pos)) {
						continue;
					}
					BlockState at = chunk.getWorld().getBlock(chunk.getChunkPos(), pos);
					if (at == null) {
						Debug.error("A block was not null in the chunk but was null when rendering in chunk: {}, {}, {} at block: {}, {}, {}", chunk.getChunkPos().x, chunk.getChunkPos().y, chunk.getChunkPos().z, pos.x, pos.y, pos.z);
						System.exit(-1);
					}
 					if (chunk.getWorld().getIsTransparentBlockAt(chunk.getChunkPos(), Util.add(pos, Util.iUP))) {
						addSimple(verts, inds, normals, tiles, at, Util.add(pos, Util.iUP), Util.RIGHT, Util.FORWARD);
					}
					if (chunk.getWorld().getIsTransparentBlockAt(chunk.getChunkPos(), Util.add(pos, Util.iDOWN))) {
						addSimple(verts, inds, normals, tiles, at, Util.add(pos, Util.iFORWARD), Util.RIGHT, Util.BACK);
					}
					if (chunk.getWorld().getIsTransparentBlockAt(chunk.getChunkPos(), Util.add(pos, Util.iRIGHT))) {
						addSimple(verts, inds, normals, tiles, at, Util.add(pos, Util.iRIGHT), Util.FORWARD, Util.UP);
					}
					if (chunk.getWorld().getIsTransparentBlockAt(chunk.getChunkPos(), Util.add(pos, Util.iLEFT))) {
						addSimple(verts, inds, normals, tiles, at, Util.add(pos, Util.iFORWARD), Util.BACK, Util.UP);
					}
					if (chunk.getWorld().getIsTransparentBlockAt(chunk.getChunkPos(), Util.add(pos, Util.iFORWARD))) {
						addSimple(verts, inds, normals, tiles, at, Util.add(pos, Util.add(Util.iFORWARD, Util.iRIGHT)), Util.LEFT, Util.UP);
					}
					if (chunk.getWorld().getIsTransparentBlockAt(chunk.getChunkPos(), Util.add(pos, Util.iBACK))) {
						addSimple(verts, inds, normals, tiles, at, pos, Util.RIGHT, Util.UP);
					}
				}
			}
		}
		
		mesh.setMesh(verts, inds, normals, tiles, AtlasHandler.getTexture());
		return mesh;
	}
	
	private static void addSimple(List<Vector3f> verts, List<Short> inds, List<Vector3f> normals, List<Vector2f> tiles, BlockState block, Vector3i corner, Vector3f right, Vector3f up) {
		if (block == null) {
			throw new RuntimeException("Block managed to get through a null-check, someone do something, this shouldn't be possible");
		}
		MeshUtil.addVoxelQuad(verts, inds, normals, tiles, AtlasHandler.getTexture(block.getParent().texture), new Vector3f(corner), right, up, new Vector2f(1.0f, 1.0f));
	}
	
}