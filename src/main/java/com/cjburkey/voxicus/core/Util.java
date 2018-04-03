package com.cjburkey.voxicus.core;

import static org.lwjgl.system.MemoryUtil.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Util {
	
	// -- VARS -- //
	
	public static float DEG_RAD = (float) Math.PI / 180.0f;
	public static float RAD_DEG = 180.0f / (float) Math.PI;
	
	// -- IO -- //
	
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
	
	// -- BUFFERS -- //
	
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
	
	// -- MATHS -- //
	
	public static float meanf(List<Float> input) {
		return meanf(input.toArray(new Float[input.size()]));
	}
	
	public static float meanf(Float... input) {
		float sum = 0.0f;
		for (float i : input) {
			sum += i;
		}
		return sum / input.length;
	}
	
	public static double meand(List<Double> input) {
		return meand(input.toArray(new Double[input.size()]));
	}
	
	public static double meand(Double... input) {
		double sum = 0.0f;
		for (double i : input) {
			sum += i;
		}
		return sum / input.length;
	}
	
	public static float sin(float min, float max, float fullTime, float time) {
		float d = max - min;
		return min + (d / 2.0f) + (d * ((float) Math.sin(((time % fullTime) / fullTime) * 360.0f * DEG_RAD) / 2.0f));
	}
	
	public static float cos(float min, float max, float fullTime, float time) {
		float d = max - min;
		return min + (d / 2.0f) + (d * ((float) Math.cos(((time % fullTime) / fullTime) * 360.0f * DEG_RAD) / 2.0f));
	}
	
	/**
	 * Unsafe, only use with parental supervision.
	 * Is ~25x slower than just using the function itself, use <i>Math.sin(...)</i> rather than <i>Util.getRangeTrigr("sin", ...)</i>, etc;
	 * there is no reason to use this function.
	 */
	public static float getRangeTrigr(String name, float min, float max, float fullTime, float time) {
		float d = max - min;
		return min + (d / 2.0f) + (d * ((float) getUnsafeTrigFunction(name, double.class, ((time % fullTime) / fullTime) * 360.0f * DEG_RAD) / 2.0f));
	}
	
	/**
	 * This is pretty unsafe, I wouldn't trust it with my child
	 */
	private static double getUnsafeTrigFunction(String name, Class<?> param, float val) {
		try {
			Method m = Math.class.getMethod(name, param);
			Object obj = m.invoke(null, val);
			if (obj == null || !(obj instanceof Double || obj instanceof Float)) {
				Debug.error("Failed to execute trig function in Math class: {}. The method did not return a double or float", name);
				return Double.MIN_VALUE / 2.0f;
			}
			return (Double) obj;
		} catch (Exception e) {
			Debug.error("Failed to located trig function in Math class: {}", name);
			Debug.error(e, false);
			return Double.MIN_VALUE / 2.0f;
		}
	}
	
	/**
	 * Inclusive random between the minimum and maximum
	 */
	public static int randomRange(int min, int max, Random random) {
		return randomRange(min, max, random, true);
	}
	
	public static int randomRange(int min, int max, Random random, boolean inclusive) {
		if (max < min) {
			int tmp = min;
			min = max;
			max = tmp;
		}
		return min + random.nextInt(max - min + ((inclusive) ? 1 : 0));
	}
	
	/**
	 * Inclusive random between the minimum and maximum
	 */
	public static float randomRange(float min, float max, Random random) {
		return randomRange(min, max, random, true);
	}
	
	public static float randomRange(float min, float max, Random random, boolean inclusive) {
		if (max < min) {
			float tmp = min;
			min = max;
			max = tmp;
		}
		return (min + (random.nextFloat() * (max - min))) - ((inclusive) ? 0.0f : 0.000001f);
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
	
	public static float smoothDamp(float current, float target, float[] velocity, float smoothTime, float deltaTime) {
		return smoothDamp(current, target, velocity, smoothTime, Float.MAX_VALUE, deltaTime);
	}
	
	public static float smoothDamp(float current, float target, float[] velocity, float smoothTime, float maxSpeed, float deltaTime) {
		smoothTime = Math.max(0.0001f, smoothTime);
		float num = 2.0f / smoothTime;
		float num2 = num * deltaTime;
		float num3 = 1.0f / (1.0f + num2 + 0.48f * num2 * num2 + 0.235f * num2 * num2 * num2);
		float num4 = current - target;
		float num5 = target;
		float num6 = maxSpeed * smoothTime;
		num4 = clamp(num4, -num6, num6);
		target = current - num4;
		float num7 = (velocity[0] - num * num4) * deltaTime;
		velocity[0] = (velocity[0] - num * num7) * num3;
		float num8 = target + (num4 + num7) * num3;
		if (num5 - current > 0.0f == num8 > num5) {
			velocity[0] = 0;
		}
		return num8;
	}
	
	public static Vector2f smoothDamp(Vector2f current, Vector2f target, Vector2f velocity, float smoothTime, float deltaTime) {
		return smoothDamp(current, target, velocity, smoothTime, Float.MAX_VALUE, deltaTime);
	}
	
	public static Vector2f smoothDamp(Vector2f current, Vector2f target, Vector2f velocity, float smoothTime, float maxSpeed, float deltaTime) {
		Vector2f out = new Vector2f(current);
		float[] xVel = new float[] { velocity.x };
		float[] yVel = new float[] { velocity.y };
		out.x = smoothDamp(out.x, target.x, xVel, smoothTime, maxSpeed, deltaTime);
		out.y = smoothDamp(out.y, target.y, yVel, smoothTime, maxSpeed, deltaTime);
		velocity.set(xVel[0], yVel[0]);
		return out;
	}
	
	public static Vector3f smoothDamp(Vector3f current, Vector3f target, Vector3f velocity, float smoothTime, float deltaTime) {
		return smoothDamp(current, target, velocity, smoothTime, Float.MAX_VALUE, deltaTime);
	}
	
	public static Vector3f smoothDamp(Vector3f current, Vector3f target, Vector3f velocity, float smoothTime, float maxSpeed, float deltaTime) {
		Vector3f out = new Vector3f(current);
		float[] xVel = new float[] { velocity.x };
		float[] yVel = new float[] { velocity.y };
		float[] zVel = new float[] { velocity.z };
		out.x = smoothDamp(out.x, target.x, xVel, smoothTime, maxSpeed, deltaTime);
		out.y = smoothDamp(out.y, target.y, yVel, smoothTime, maxSpeed, deltaTime);
		out.z = smoothDamp(out.z, target.z, zVel, smoothTime, maxSpeed, deltaTime);
		velocity.set(xVel[0], yVel[0], zVel[0]);
		return out;
	}
	
	// -- JAVA REFLECTION -- //
	
	public static List<Field> getInheritedFields(Class<?> type) throws Exception {
		try {
			List<Field> result = new ArrayList<Field>();
			Class<?> i = type;
			while (i != null && !i.equals(Object.class)) {
				Collections.addAll(result, i.getDeclaredFields());
				i = i.getSuperclass();
			}
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static Field getFieldFromNameInClassOrSupers(Class<?> type, String name) throws Exception {
		for (Field field : getInheritedFields(type)) {
			if (field.getName().equals(name)) {
				return field;
			}
		}
		return null;
	}
	
}