package com.cjburkey.voxicus.game;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.voxicus.Voxicus;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.IInstance;
import com.cjburkey.voxicus.core.Input;
import com.cjburkey.voxicus.core.SemVer;
import com.cjburkey.voxicus.core.Time;
import com.cjburkey.voxicus.core.Transformations;
import com.cjburkey.voxicus.graphic.ShaderProgram;
import com.cjburkey.voxicus.graphic.ShaderTerrain;
import com.cjburkey.voxicus.graphic.Window;
import com.cjburkey.voxicus.world.Scene;

public final class Game {
	
	public static final SemVer VERSION = new SemVer(0, 0, 1, "alpha");
	public static final IInstance INST = new InstanceVoxicus();
	
	private static boolean running;
	private static int fps;
	
	private float fpsCheck = 0.0f;
	private int frames = 0;

	public boolean resized;
	private Window window;
	private ShaderProgram shader;
	private final Scene world = new Scene();
	
	public void init() {
		Debug.log("Initializing game");
		Time.init();
		
		window = new Window();
		window.setSize(window.getScreenSize().x / 2, window.getScreenSize().y / 2, true);
		window.setTitle("Voxicus " + VERSION);
		window.show();
		
		String glVersion = glGetString(GL_VERSION);
		String glfwVersion = GLFW.glfwGetVersionString();
		
		Debug.log("OpenGL: {}", glVersion);
		Debug.log("GLFW: {}", glfwVersion);
	}
	
	public void start() {
		Debug.log("Starting game");
		running = true;
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glFrontFace(GL_CCW);
		glCullFace(GL_BACK);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		shader = new ShaderTerrain();
		if (shader.getHasError()) {
			Debug.error("Failed to create terrain shader");
			stop();
			return;
		}
		shader.bind();
		Debug.log("Created terrain shader program");
		
		Input.init(window);
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
		fpsCheck += Time.getDeltaTime();
		frames ++;
		if (fpsCheck >= 1.0d) {
			fps = frames;
			Debug.log("Frames last second: {}", fps);
			fpsCheck = 0.0f;
			frames = 0;
		}
		
		INST.update();
		world.update();
		
		window.preRender();
		INST.render();
		world.render(shader);
		Input.update();
		window.postRender();
		
		if (window.getShouldClose()) {
			stop();
		}
		if (resized) {
			Debug.log("Window size changed to " + window.getWindowSize().x + "x" + window.getWindowSize().y);
			glViewport(0, 0, window.getWindowSize().x, window.getWindowSize().y);
			Transformations.updateProjection(90.0f, window.getWindowSize().x, window.getWindowSize().y, 0.01f, 1000.0f);
			shader.setUniform("projectionMatrix", Transformations.PROJECTION);
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
	
}