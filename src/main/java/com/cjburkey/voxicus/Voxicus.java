package com.cjburkey.voxicus;

import org.apache.logging.log4j.LogManager;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.game.Game;

public final class Voxicus {
	
	private static Game game;
	
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