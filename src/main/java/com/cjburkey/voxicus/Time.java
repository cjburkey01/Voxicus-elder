package com.cjburkey.voxicus;

public class Time {
	
	private static double startTimeSeconds;
	
	// In seconds
	private static double previousTime = getTimeSeconds();
	private static double currentTime = getTimeSeconds();
	private static double deltaTime = 0.0d;
	
	public static void init() {
		startTimeSeconds = getTimeSeconds();
	}
	
	public static void update(long updateStartTimeNanos) {
		previousTime = currentTime;
		currentTime = Math.abs(updateStartTimeNanos) / 1000000000.0d;
		deltaTime = currentTime - previousTime;
	}
	
	public static double getStartTime() {
		return startTimeSeconds;
	}
	
	public static double getTime() {
		return currentTime - startTimeSeconds;
	}
	
	public static double getDeltaTime() {
		return deltaTime;
	}
	
	public static float getDeltaTimeF() {
		return (float) deltaTime;
	}
	
	private static double getTimeSeconds() {
		return Math.abs(System.nanoTime()) / 1000000000.0d;
	}
	
}