package com.cjburkey.voxicus;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.glfw.GLFW;

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
	public final World world = new World();
	private boolean locked = true;
	
	public void init() {
		Debug.log("Initializing game");
		Time.init();
		
		window = new Window();
		window.setSize(window.getScreenSize().x / 2, window.getScreenSize().y / 2, true);
		window.setTitle("Voxicus " + VERSION);
		window.show();
		GLFW.glfwSetInputMode(window.getId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}
	
	public void start() {
		Debug.log("Starting game");
		running = true;
		
		glEnable(GL_DEPTH_TEST);
		glClearColor(0.0f, 1.0f, 1.0f, 1.0f);
		
		shader = new ShaderProgram();
		if (!shader.addShader(GL_VERTEX_SHADER, Util.getTextFromResource("/res/voxicus/shader/terrainVertex.glsl"))) {
			Debug.error("Failed to add terrain vertex shader");
		}
		if (!shader.addShader(GL_FRAGMENT_SHADER, Util.getTextFromResource("/res/voxicus/shader/terrainFragment.glsl"))) {
			Debug.error("Failed to add terrain fragment shader");
		}
		if (!shader.link()) {
			Debug.error("Failed to link terrain shader program");
		}
		shader.addUniform("projectionMatrix");
		shader.addUniform("modelViewMatrix");
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
		
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			locked = !locked;
			GLFW.glfwSetInputMode(window.getId(), GLFW.GLFW_CURSOR, (locked) ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
		}
		
		INST.update();
		world.update();
		
		window.preRender();
		
		INST.render();
		world.render(shader);
		
		Input.update();
		window.postRender();
		if (window.getShouldClose()) {
			running = false;
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
			if (Time.getDeltaTime() < 1000000000l / 60l) {	// If we're going at more than 60fps, add a little slow-down (makes a 1000ish fps limit)
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}