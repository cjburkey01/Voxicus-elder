package com.cjburkey.voxicus;

public final class Game {
	
	private static boolean running;
	private static int fps;
	
	private float fpsCheck = 0.0f;
	private int frames = 0;
	
	public void init() {
		Debug.log("Initializing game");
		Time.init();
	}
	
	public void start() {
		Debug.log("Starting game");
		running = true;
		startGameLoop();
	}
	
	public void stop() {
		Debug.log("Stopping game");
		running = false;
	}
	
	public boolean getRunning() {
		return running;
	}
	
	public int getFps() {
		return fps;
	}
	
	private void startGameLoop() {
		long start = 0;
		while (true) {
			start = Math.abs(System.nanoTime());
			Time.update(start);
			update();
			if (!running) {
				break;
			}
			if (Time.getDeltaTime() < 1000000000l / 60l) {	// If we're going at more than 60fps, add a little slow-down (makes a 1000ish fps limit)
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void update() {
		fpsCheck += Time.getDeltaTime();
		frames ++;
		if (fpsCheck >= 1.0d) {
			fps = frames;
			Debug.log("FPS: {}", fps);
			fpsCheck = 0.0f;
			frames = 0;
		}
	}
	
}