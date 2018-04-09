package com.cjburkey.voxicus.game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.voxicus.Voxicus;
import com.cjburkey.voxicus.core.DataHandler;
import com.cjburkey.voxicus.core.DataType;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.IInstance;
import com.cjburkey.voxicus.core.Input;
import com.cjburkey.voxicus.core.SemVer;
import com.cjburkey.voxicus.core.Time;
import com.cjburkey.voxicus.core.Transformations;
import com.cjburkey.voxicus.core.Window;
import com.cjburkey.voxicus.event.EventSystem;
import com.cjburkey.voxicus.shader.ShaderColor;
import com.cjburkey.voxicus.shader.ShaderProgram;
import com.cjburkey.voxicus.shader.ShaderTexture;
import com.cjburkey.voxicus.shader.ShaderTextureUI;
import com.cjburkey.voxicus.shader.ShaderVoxel;
import com.cjburkey.voxicus.texture.AtlasHandler;
import com.cjburkey.voxicus.world.Scene;

public final class Game {
	
	public static final SemVer VERSION = new SemVer(0, 0, 1, "alpha");
	public static final IInstance INST = new InstanceVoxicus();
	
	public static int maxTextureSize;
	
	private static boolean running;
	private static int fps;
	
	private float lastFpsCheck = 0.0f;
	private int frames = 0;

	public boolean resized;
	private Window window;
	public ShaderProgram shaderColored;
	public ShaderProgram shaderTextured;
	public ShaderProgram shaderTexturedUI;
	public ShaderProgram shaderVoxel;
	private final Scene world = new Scene();
	private final EventSystem globalEventHandler = new EventSystem();
	
	public void init() {
		Debug.log("Initializing game");
		Time.init();
		
		window = new Window(false, 0);
		window.setSize(window.getScreenSize().x / 2, window.getScreenSize().y / 2, true);
		window.setTitle(INST.getName() + ' ' + INST.getVersion());
		window.show();
		
		Debug.log("OpenGL: {}", glGetString(GL_VERSION));
		Debug.log("GLFW: {}", GLFW.glfwGetVersionString());
		Debug.log("Maximum Texture Size: {}^2", maxTextureSize = glGetInteger(GL_MAX_TEXTURE_SIZE));
		Debug.log("Maximum Texture Array Size: {}", glGetInteger(GL_MAX_ARRAY_TEXTURE_LAYERS));
		
		Debug.log("Loading data types");
		DataHandler.scanTypes(DataType.class);
	}
	
	public void start() {
		Debug.log("Starting game");
		running = true;
		
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glFrontFace(GL_CCW);
		glCullFace(GL_BACK);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shaderColored = new ShaderColor("res/voxicus/shader", "colorVertex", "colorFragment");
		if (shaderColored.getHasError()) {
			Debug.error("Failed to create color shader");
			stop();
			return;
		}
		shaderColored.bind();
		Debug.log("Created color shader program");
		
		shaderTextured = new ShaderTexture("res/voxicus/shader", "textureVertex", "textureFragment");
		if (shaderTextured.getHasError()) {
			Debug.error("Failed to create texture shader");
			stop();
			return;
		}
		shaderTextured.bind();
		Debug.log("Created texture shader program");
		
		shaderTexturedUI = new ShaderTextureUI("res/voxicus/shader", "textureUIVertex", "textureUIFragment");
		if (shaderTexturedUI.getHasError()) {
			Debug.error("Failed to create textured ui shader");
			stop();
			return;
		}
		shaderTexturedUI.bind();
		Debug.log("Created textured ui shader program");

		shaderVoxel = new ShaderVoxel("res/voxicus/shader", "voxelVertex", "voxelFragment");
		if (shaderVoxel.getHasError()) {
			Debug.error("Failed to create voxel shader");
			stop();
			return;
		}
		shaderVoxel.bind();
		Debug.log("Created voxel shader program");
		
		Input.init(window);
		
		INST.preinit();
		AtlasHandler.registerTextures();
		INST.init();
		
		startGameLoop();
		
		Debug.log("Closing game");
		INST.exit();
		window.destroy();
		Window.terminate();
		Debug.log("Closed game");
	}
	
	public void stop() {
		Debug.log("Stopping game");
		running = false;
	}
	
	private void update() {
		frames ++;
		if (lastFpsCheck < Time.getTimeF() - 1.0f) {
			fps = frames;
			Debug.log("Frames last second: {}", fps);
			lastFpsCheck = Time.getTimeF();
			frames = 0;
		}
		
		INST.update();
		world.update();
		
		window.preRender();
		INST.render();
		world.render();
		Input.update();
		window.postRender();
		
		if (window.getShouldClose()) {
			stop();
		}
		if (resized) {
			Debug.log("Window size changed to " + window.getWindowSize().x + "x" + window.getWindowSize().y);
			glViewport(0, 0, window.getWindowSize().x, window.getWindowSize().y);
			Transformations.updateProjection(90.0f, window.getWindowSize().x, window.getWindowSize().y, 0.01f, 1000.0f);
			resized = false;
		}
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
			if (Time.getDeltaTime() < 1000000000l / 60l) {	// If we're going at more than 60fps, add a ~1ms pause (makes a 1000ish fps limit)
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Scene getWorld() {
		return Voxicus.getGame().world;
	}
	
	public static Window getWindow() {
		return Voxicus.getGame().window;
	}
	
	/**
	 * Carried across scenes
	 */
	public static EventSystem getGlobalEventHandler() {
		return Voxicus.getGame().globalEventHandler;
	}
	
}