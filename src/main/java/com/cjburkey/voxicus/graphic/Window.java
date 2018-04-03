package com.cjburkey.voxicus.graphic;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import com.cjburkey.voxicus.Voxicus;

public class Window {
	
	private static boolean glfwInit;
	private static GLCapabilities caps;
	private String title = "";
	private final long window;
	private final Vector2i size = new Vector2i(0, 0);
	
	public Window(boolean vsync, int samples) {
		if (!glfwInit) {
			glfwInit = glfwInit();
			if (!glfwInit) {
				throw new RuntimeException("Failed to initialize GLFW");
			}
			GLFWErrorCallback.createPrint(System.err).set();
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_SAMPLES, samples);
		
		window = glfwCreateWindow(300, 300, "", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create GLFW window");
		}
		
		glfwMakeContextCurrent(window);
		if (caps == null) {
			caps = GL.createCapabilities();
		}
		glfwSetFramebufferSizeCallback(window, (win, w, h) -> onResize(new Vector2i(w, h)));
		glfwSwapInterval((vsync) ? 1 : 0);
		
		if (samples > 0) {
			glEnable(GL_MULTISAMPLE);
		}
	}
	
	public void setTitle(String title) {
		this.title = title;
		glfwSetWindowTitle(window, title);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void show() {
		glfwShowWindow(window);
	}
	
	public void hide() {
		glfwHideWindow(window);
	}
	
	public void preRender() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void center() {
		Vector2i screen = getScreenSize();
		Vector2i pos = new Vector2i((screen.x - size.x) / 2, (screen.y - size.y) / 2);
		glfwSetWindowPos(window, pos.x, pos.y);
	}
	
	public void setSize(int width, int height) {
		setSize(width, height, false);
	}
	
	public void setSize(int width, int height, boolean center) {
		glfwSetWindowSize(window, width, height);
		if (center) {
			center();
		}
	}
	
	public void postRender() {
		glfwPollEvents();
		glfwSwapBuffers(window);
	}
	
	public boolean getShouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public void destroy() {
		glfwDestroyWindow(window);
	}
	
	public Vector2i getWindowSize() {
		return new Vector2i(size);
	}
	
	public Vector2i getScreenSize() {
		GLFWVidMode mon = glfwGetVideoMode(glfwGetPrimaryMonitor());
		return new Vector2i(mon.width(), mon.height());
	}
	
	public long getId() {
		return window;
	}
	
	public static void terminate() {
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	protected void onResize(Vector2i size) {
		this.size.set(size);
		Voxicus.getGame().resized = true;
	}
	
}