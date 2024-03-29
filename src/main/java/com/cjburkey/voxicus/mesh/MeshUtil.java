package com.cjburkey.voxicus.mesh;

import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.voxicus.core.Util;

public class MeshUtil {
	
	public static void addQuad(List<Vector3f> verts, List<Short> inds, List<Vector2f> uvs, Vector3f center, Vector3f right, Vector3f up, float size) {
		addQuad(verts, inds, uvs, center, right, up, new Vector2f(size, size));
	}
	
	public static void addQuad(List<Vector3f> verts, List<Short> inds, List<Vector2f> uvs, Vector3f center, Vector3f right, Vector3f up, Vector2f size) {
		short si = (short) verts.size();
		Vector3f r = new Vector3f(right).normalize().mul(size.x / 2.0f);
		Vector3f l = Util.neg(r);
		Vector3f u = new Vector3f(up).normalize().mul(size.y / 2.0f);
		Vector3f d = Util.neg(u);
		center = new Vector3f(center);
		
		verts.add(Util.add(Util.add(d, l), center));
		verts.add(Util.add(Util.add(d, r), center));
		verts.add(Util.add(Util.add(u, r), center));
		verts.add(Util.add(Util.add(u, l), center));
		
		inds.add(si);
		inds.add((short) (si + 1));
		inds.add((short) (si + 2));
		inds.add(si);
		inds.add((short) (si + 2));
		inds.add((short) (si + 3));
		
		uvs.add(new Vector2f(0.0f, 1.0f));
		uvs.add(new Vector2f(1.0f, 1.0f));
		uvs.add(new Vector2f(1.0f, 0.0f));
		uvs.add(new Vector2f(0.0f, 0.0f));
	}
	
	public static void addQuadFromCorner(List<Vector3f> verts, List<Short> inds, List<Vector2f> uvs, Vector3f corner, Vector3f right, Vector3f up, float size) {
		addQuadFromCorner(verts, inds, uvs, corner, right, up, new Vector2f(size, size));
	}
	
	public static void addQuadFromCorner(List<Vector3f> verts, List<Short> inds, List<Vector2f> uvs, Vector3f corner, Vector3f right, Vector3f up, Vector2f size) {
		short si = (short) verts.size();
		Vector3f r = new Vector3f(right).normalize().mul(size.x);
		Vector3f u = new Vector3f(up).normalize().mul(size.y);
		corner = new Vector3f(corner);
		
		verts.add(corner);
		verts.add(Util.add(r, corner));
		verts.add(Util.add(Util.add(u, r), corner));
		verts.add(Util.add(u, corner));
		
		inds.add(si);
		inds.add((short) (si + 1));
		inds.add((short) (si + 2));
		inds.add(si);
		inds.add((short) (si + 2));
		inds.add((short) (si + 3));
		
		uvs.add(new Vector2f(0.0f, 1.0f));
		uvs.add(new Vector2f(1.0f, 1.0f));
		uvs.add(new Vector2f(1.0f, 0.0f));
		uvs.add(new Vector2f(0.0f, 0.0f));
	}
	
	public static void addVoxelQuad(List<Vector3f> verts, List<Short> inds, List<Vector3f> normals, List<Vector2f> tiles, Vector2i tilePos, Vector3f corner, Vector3f right, Vector3f up, Vector2f size) {
		short si = (short) verts.size();
		Vector3f r = new Vector3f(right).normalize().mul(size.x);
		Vector3f u = new Vector3f(up).normalize().mul(size.y);
		corner = new Vector3f(corner);
		Vector2f otilePos = new Vector2f(tilePos);
		
		verts.add(corner);
		verts.add(Util.add(r, corner));
		verts.add(Util.add(Util.add(u, r), corner));
		verts.add(Util.add(u, corner));
		
		inds.add(si);
		inds.add((short) (si + 1));
		inds.add((short) (si + 2));
		inds.add(si);
		inds.add((short) (si + 2));
		inds.add((short) (si + 3));
		
		Vector3f normal = up.cross(right, new Vector3f());
		normals.add(normal);
		normals.add(normal);
		normals.add(normal);
		normals.add(normal);
		
		tiles.add(otilePos);
		tiles.add(otilePos);
		tiles.add(otilePos);
		tiles.add(otilePos);
	}
	
}