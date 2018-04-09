package com.cjburkey.voxicus.game;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.voxicus.block.Blocks;
import com.cjburkey.voxicus.chunk.World;
import com.cjburkey.voxicus.component.ComponentCamera;
import com.cjburkey.voxicus.component.ComponentFreeMove;
import com.cjburkey.voxicus.component.ComponentMesh;
import com.cjburkey.voxicus.component.ComponentMouseLook;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.IInstance;
import com.cjburkey.voxicus.core.Input;
import com.cjburkey.voxicus.core.SemVer;
import com.cjburkey.voxicus.core.Time;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.event.EventRegistryTexture;
import com.cjburkey.voxicus.generation.ChunkGeneratorOverworld;
import com.cjburkey.voxicus.mesh.MeshUtil;
import com.cjburkey.voxicus.mesh.MeshVoxel;
import com.cjburkey.voxicus.resource.Resource;
import com.cjburkey.voxicus.texture.AtlasHandler;
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
//		world.spawnAround(new Vector3i().zero(), 5);
//		Chunk z = world.getAndGenerateChunk(new Vector3i().zero());
//		Scene.getActive().addObject("ChunkZ").addComponent(new ComponentChunk(z));
		
//		for (int x = 0; x < 2; x ++) {
//			for (int y = 0; y < 2; y ++) {
//				Debug.log("{}, {} is {}", x, y, AtlasHandler.getTexture(new Vector2i(x, y)));
//			}
//		}
		
		GameObject camObj = Game.getWorld().addObject("Camera");
		camObj.addComponent(new ComponentCamera(Game.getWindow().getWindowSize())).setClearColor(new Vector3f(0.1f, 0.1f, 0.1f));
		camObj.addComponent(new ComponentMouseLook()).setMouseLock(true).setPauseTimeOnFreeCursor(true).setPausedTimeScale(0.0d).setSmoothing(0.13f);
		camObj.addComponent(new ComponentFreeMove()).doManualMove(new Vector3f(0.0f, 0.0f, 0.75f), false);
		
		GameObject quadTest = Game.getWorld().addObject("MeshTest");
		List<Vector3f> verts = new ArrayList<>();
		List<Short> inds = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Vector2f> tiles = new ArrayList<>();
		
		Vector2i pos1 = AtlasHandler.getTexture(new Resource("voxicus", "texture/terrain/blockStone.png"));
		Vector2i pos2 = AtlasHandler.getTexture(new Resource("voxicus", "texture/terrain/blockGrass2.png"));
		MeshUtil.addVoxelQuad(verts, inds, normals, tiles, pos1, new Vector3f(0.0f, 0.0f, 0.0f), Util.RIGHT, Util.FORWARD, new Vector2f(5.0f, 1.0f));
		MeshUtil.addVoxelQuad(verts, inds, normals, tiles, pos2, new Vector3f(0.0f, -2.0f, 0.0f), Util.RIGHT, Util.UP, new Vector2f(3.0f, 2.0f));
		
		MeshVoxel quadMesh = new MeshVoxel();
		quadMesh.setMesh(verts, inds, normals, tiles, AtlasHandler.getTexture());
		quadTest.addComponent(new ComponentMesh(quadMesh));
		
//		Scene.getActive().getGuiHandler().addElement(new GuiBox(new Bounds(10.0f, 10.0f, 100.0f, 100.0f), AtlasHandler.instance.getTexture()));
	}
	
	public void update() {
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
	}
	
	public World getWorld() {
		return world;
	}
	
}