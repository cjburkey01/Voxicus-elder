package com.cjburkey.voxicus;

import static org.lwjgl.system.MemoryUtil.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

public class Util {
	
	public static float DEG_RAD = (float) Math.PI / 180.0f;
	public static float RAD_DEG = 180.0f / (float) Math.PI;
	
	public static String getTextFromResource(String resPath) {
		InputStream is = Util.class.getResourceAsStream(resPath);
		if (is == null) {
			return null;
		}
		StringBuilder stringOut = new StringBuilder();
		BufferedReader reader = null;
		String string = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			while ((string = reader.readLine()) != null) {
				stringOut.append(string);
				stringOut.append('\n');
			}
		} catch (Exception e) {
			Debug.error(e);
			return null;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (Exception e1) {
				Debug.error(e1);
				return null;
			}
		}
		return stringOut.toString();
	}
	
	public static float[] vector3fToArray(Vector3f[] vecs) {
		float[] data = new float[vecs.length * 3];
		for (int i = 0; i < vecs.length; i ++) {
			data[i * 3] = vecs[i].x;
			data[i * 3 + 1] = vecs[i].y;
			data[i * 3 + 2] = vecs[i].z;
		}
		return data;
	}
	
	/**
	 * <b><u><i>MAKE SURE TO DELTE THIS BUFFER WHEN IT IS NO LONGER USED</i></u></b>
	 */
	public static FloatBuffer vec3fBuffer(Vector3f[] vecs) {
		FloatBuffer buffer = memAllocFloat(vecs.length * 3);
		buffer.put(vector3fToArray(vecs)).flip();
		return buffer;
	}
	
	public static IntBuffer intBuffer(int[] ints) {
		IntBuffer buffer = memAllocInt(ints.length);
		buffer.put(ints).flip();
		return buffer;
	}
	
	public static ShortBuffer shortBuffer(short[] shorts) {
		ShortBuffer buffer = memAllocShort(shorts.length);
		buffer.put(shorts).flip();
		return buffer;
	}
	
	public static int[] intListToArray(List<Integer> ints) {
		int[] out = new int[ints.size()];
		for (int i = 0; i < ints.size(); i ++) {
			out[i] = ints.get(i);
		}
		return out;
	}
	
	public static short[] shortListToArray(List<Short> shorts) {
		short[] out = new short[shorts.size()];
		for (int i = 0; i < shorts.size(); i ++) {
			out[i] = shorts.get(i);
		}
		return out;
	}
	
	public static <T> List<T> arrayToList(T[] array) {
		List<T> out = new ArrayList<>();
		for (T t : array) {
			out.add(t);
		}
		return out;
	}
	
	public static float clamp(float in, float min, float max) {
		if (in < min) {
			return min;
		}
		if (in > max) {
			return max;
		}
		return in;
	}
	
}