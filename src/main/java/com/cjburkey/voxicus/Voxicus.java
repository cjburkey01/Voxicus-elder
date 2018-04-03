package com.cjburkey.voxicus;

import org.apache.logging.log4j.LogManager;

public final class Voxicus {
	
	private static Game game;
	
	// TODO: Controls:
	//	- WASD movement
	//	- Mouse look
	//	- Escape to free cursor
	public static void main(String[] args) {
		Debug.init(LogManager.getLogger("voxicus"));
		game = new Game();
		game.init();
		game.start();
	}
	
	public static Game getGame() {
		return game;
	}
	
}