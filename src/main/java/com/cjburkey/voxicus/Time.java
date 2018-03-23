package com.cjburkey.voxicus;

public class Time {
	
	private static double startTimeSeconds;
	
	// In seconds
	private static double previousTime = 0.0d;
	private static double currentTime = 0.0d;
	private static double deltaTime = 0.0d;
	
	public static void init() {
		startTimeSeconds = Math.abs(System.nanoTime()) / 1000000000.0d;
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
	
}