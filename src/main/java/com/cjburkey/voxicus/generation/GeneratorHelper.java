package com.cjburkey.voxicus.generation;

import org.joml.Vector3i;
import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.core.Util;

public class GeneratorHelper {
	
	private static final int ARB1 = 52;
	private static final int ARB2 = 18;
	private static final int ARB3 = 26;
	
	public static final double SCALE_DEF = 250.0d;
	public static final double BASE_DEF = 5.0d;
	public static final double AMPLITUDE_DEF = 50.0d;
	
	public static double getNoise(long seed, Vector3i chunkPos, Vector3i posInChunk) {
		return getNoise(seed, Util.add(Util.mul(chunkPos, Chunk.SIZE), posInChunk), SCALE_DEF, BASE_DEF, AMPLITUDE_DEF);
	}
	
	public static double getNoise(long seed, Vector3i block, double scale, double base, double amplitude) {
		return SimplexNoise.noise(ARB1 * seed + block.x / scale, ARB2 * seed + block.y / scale, ARB3 * seed + block.z / scale)
			+ (base - block.y) / amplitude;
	}
	
}