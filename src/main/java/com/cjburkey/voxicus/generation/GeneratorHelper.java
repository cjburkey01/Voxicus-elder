package com.cjburkey.voxicus.generation;

import org.joml.Vector3i;

public class GeneratorHelper {
	
	private static final int ARB1 = 52;
	private static final int ARB2 = 18;
	private static final int ARB3 = 26;
	
	public static final double SCALE_DEF = 20.0d;
	public static final double BASE_DEF = 5.0d;
	public static final double AMPLITUDE_DEF = 1.0d;
	
	public static double getNoise(long seed, Vector3i block) {
		return getNoise(seed, block, SCALE_DEF, BASE_DEF, AMPLITUDE_DEF);
	}
	
	public static double getNoise(long seed, Vector3i block, double scale, double base, double amplitude) {
		double val = SimplexNoise.noise(ARB1 * seed + block.x / scale, ARB2 * seed + block.y / scale, ARB3 * seed + block.z / scale);
		val += (base - block.y) / amplitude;
		return val;
	}
	
}