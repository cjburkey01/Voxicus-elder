package com.cjburkey.voxicus.game;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.voxicus.block.Blocks;
import com.cjburkey.voxicus.chunk.World;
import com.cjburkey.voxicus.component.ComponentCamera;
import com.cjburkey.voxicus.component.ComponentFreeMove;
import com.cjburkey.voxicus.component.ComponentMouseLook;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.IInstance;
import com.cjburkey.voxicus.core.Input;
import com.cjburkey.voxicus.core.SemVer;
import com.cjburkey.voxicus.core.Time;
import com.cjburkey.voxicus.event.EventRegistryTexture;
import com.cjburkey.voxicus.generation.ChunkGeneratorOverworld;
import com.cjburkey.voxicus.resource.Resource;
import com.cjburkey.voxicus.world.GameObject;

public class InstanceVoxicus implements IInstance {

	private float timeChangeSpeed = 2.0f;
	private World world;
	
	public String getName() {
		return "Voxicus";
	}
	
	public SemVer getVersion() {
		return new SemVer(0, 0, 1, "alpha");
	}
	
	public void preinit() {
		Game.getGlobalEventHandler().addListener(EventRegistryTexture.class, e -> {
			e.addTexture(new Resource("voxicus", "texture/terrain/blockStone.png"));
			e.addTexture(new Resource("voxicus", "texture/terrain/blockGrass.png"));
			e.addTexture(new Resource("voxicus", "texture/terrain/blockDirt.png"));
			e.addTexture(new Resource("voxicus", "texture/terrain/blockGrass2.png"));
		});
		
		Blocks.registerBlocks();
	}
	
	public void init() {
		world = new World(0, new ChunkGeneratorOverworld());
		world.spawnAround(new Vector3i().zero(), 2);
		
		addCamera();
	}
	
	private void addCamera() {
		GameObject camObj = Game.getWorld().addObject("Camera");
		camObj.addComponent(new ComponentCamera(Game.getWindow().getWindowSize())).setClearColor(new Vector3f(0.1f, 0.1f, 0.1f));
		camObj.addComponent(new ComponentMouseLook()).setMouseLock(true).setPauseTimeOnFreeCursor(true).setPausedTimeScale(0.0d).setSmoothing(0.13f);
		camObj.addComponent(new ComponentFreeMove()).doManualMove(new Vector3f(0.0f, 0.0f, 0.75f), false);
	}
	
	public void update() {
		world.tick();
		
		boolean p = false;
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_DOWN)) {
			Time.setTimeScale(Time.getTimeScale() - ((Input.getIsKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) ? (timeChangeSpeed * 10) :  (timeChangeSpeed)) * Time.getPureDeltaTime());
			p = true;
		}
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_UP)) {
			Time.setTimeScale(Time.getTimeScale() + ((Input.getIsKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) ? (timeChangeSpeed * 10) :  (timeChangeSpeed)) * Time.getPureDeltaTime());
			p = true;
		}
		if (Input.getIsKeyPressed(GLFW.GLFW_KEY_P)) {
			Time.setTimeScale(1.0d);
			p = true;
		}
		if (p) {
			Debug.log("Time scale: {}", Time.getTimeScale());
		}
	}
	
	public void render() {
	}
	
	public void exit() {
		world.exit();
	}
	
	public World getWorld() {
		return world;
	}
	
}