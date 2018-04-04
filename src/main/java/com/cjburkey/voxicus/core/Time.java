package com.cjburkey.voxicus.core;

public class Time {
	
	private static double startTimeSeconds;
	
	// In seconds
	private static double previousTime = getTimeSeconds();
	private static double currentTime = getTimeSeconds();
	private static double deltaTime = 0.0d;
	
	// Smooth timescale movement
	private static double timeScaleSmoothing = 0.1d;
	private static double goalTimeScale = 1.0d;
	private static double timeScale = 1.0d;
	private static double[] timeScaleV = new double[1];
	
	public static void init() {
		startTimeSeconds = getTimeSeconds();
	}
	
	public static void update(long updateStartTimeNanos) {
		previousTime = currentTime;
		currentTime = Math.abs(updateStartTimeNanos) / 1000000000.0d;
		deltaTime = currentTime - previousTime;
		
		timeScale = Util.smoothDamp(timeScale, goalTimeScale, timeScaleV, timeScaleSmoothing, getPureDeltaTime());
	}
	
	public static void setTimeScale(double time) {
		goalTimeScale = time;
	}
	
	public static double getStartTime() {
		return startTimeSeconds;
	}
	
	/**
	 * Does not abide by the time scale
	 */
	public static float getTimeF() {
		return (float) getTime();
	}
	
	/**
	 * Does not abide by the time scale
	 */
	public static double getTime() {
		return currentTime - startTimeSeconds;
	}
	
	public static float getDeltaTimeF() {
		return (float) getDeltaTime();
	}
	
	public static double getDeltaTime() {
		return deltaTime * timeScale;
	}
	
	/**
	 * Does not abide by the time scale
	 */
	public static float getPureDeltaTimeF() {
		return (float) getPureDeltaTime();
	}
	
	/**
	 * Does not abide by the time scale
	 */
	public static double getPureDeltaTime() {
		return deltaTime;
	}
	
	private static double getTimeSeconds() {
		return Math.abs(System.nanoTime()) / 1000000000.0d;
	}
	
	public static double getTimeScale() {
		return timeScale;
	}
	
}