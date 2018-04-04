package com.cjburkey.voxicus.mesh;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.voxicus.block.BlockState;
import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.texture.AtlasHandler;

public class ChunkMesher {
	
	public static MeshTextureArray generateMeshForChunk(Chunk chunk) {
		MeshTextureArray mesh = new MeshTextureArray();
		List<Vector3f> verts = new ArrayList<>();
		List<Short> inds = new ArrayList<>();
		List<Vector2f> uvs = new ArrayList<>();
		List<Integer> tex = new ArrayList<>();
		
		// So we don't have to re-instantiate per block, we pre-define the variables
		Vector3i p = new Vector3i().zero();
		Vector3f pf = new Vector3f().zero();
		Vector3i scan = new Vector3i().zero();
		BlockState block = null;
		int tid = 0;
		boolean north = false;
		boolean south = false;
		boolean east = false;
		boolean west = false;
		boolean up = false;
		boolean down = false;
		
		for (p.z = 0; p.z < Chunk.SIZE; p.z ++) {
			for (p.y = 0; p.y < Chunk.SIZE; p.y ++) {
				for (p.x = 0; p.x < Chunk.SIZE; p.x ++) {
					block = chunk.getBlock(p);
					if (block == null || block.getParent() == null) {
						continue;	// Empty block (air), don't render
					}
					//Debug.log(p);
					
					pf.set(p.x, p.y, p.z);
					scan.set(p).add(Util.iFORWARD);
					south = chunk.getIsTransparentBlockAt(scan);
					scan.set(p).add(Util.iBACK);
					north = chunk.getIsTransparentBlockAt(scan);
					scan.set(p).add(Util.iRIGHT);
					east = chunk.getIsTransparentBlockAt(scan);
					scan.set(p).add(Util.iLEFT);
					west = chunk.getIsTransparentBlockAt(scan);
					scan.set(p).add(Util.iUP);
					up = chunk.getIsTransparentBlockAt(scan);
					scan.set(p).add(Util.iDOWN);
					down = chunk.getIsTransparentBlockAt(scan);
					
					tid = AtlasHandler.instance.getTextureId(block.getParent().texture);
					//tid = 0;
					
					Util.addCube(verts, inds, uvs, tex, Util.UV_DEF, Util.arrayToList(new Integer[] { tid, tid, tid, tid, tid, tid }), pf, 1.0f,
						new Boolean[] { north, south, east, west, up, down });
				}
			}
		}
		
		mesh.setMesh(verts, inds, uvs, tex, AtlasHandler.instance.getTexture());
		
		return mesh;
	}
	
}