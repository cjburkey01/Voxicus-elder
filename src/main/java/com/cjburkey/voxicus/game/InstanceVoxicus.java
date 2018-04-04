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
	
	private World world;
	
	private float timeChangeSpeed = 2.0f;
	
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
			e.addTexture(new Resource("voxicus", "texture/gui/box.png"));
		});
		
		Blocks.registerBlocks();
	}
	
	@SuppressWarnings("deprecation")
	public void init() {
		world = new World(0, new ChunkGeneratorOverworld());
		world.spawnAround(new Vector3i().zero(), 5);
//		Chunk z = world.getAndGenerateChunk(new Vector3i().zero());
//		Scene.getActive().addObject("ChunkZ").addComponent(new ComponentChunk(z));
		
//		MeshTextureArray mesh = new MeshTextureArray();
//		
//		List<Vector3f> verts = new ArrayList<>();
//		List<Short> inds = new ArrayList<>();
//		List<Vector2f> uvs = new ArrayList<>();
//		List<Integer> tex = new ArrayList<>();
//		
//		Util.addCube(
//				verts,
//				inds,
//				uvs,
//				tex,
//				Util.UV_DEF,
//				Util.arrayToList(new Integer[] { 0, 0, 0, 0, 0, 0 }),
//				new Vector3f(0.0f, 0.0f, 0.0f),
//				1.0f);
//		
//		Util.addCube(
//				verts,
//				inds,
//				uvs,
//				tex,
//				Util.UV_DEF,
//				Util.arrayToList(new Integer[] { 1, 1, 1, 1, 1, 1 }),
//				new Vector3f(5.0f, 0.0f, 0.0f),
//				1.0f);
//		
//		Util.addCube(
//				verts,
//				inds,
//				uvs,
//				tex,
//				Util.UV_DEF,
//				Util.arrayToList(new Integer[] { 2, 2, 2, 2, 2, 2 }),
//				new Vector3f(10.0f, 0.0f, 0.0f),
//				1.0f);
//		
//		mesh.setMesh(verts, inds, uvs, tex, AtlasHandler.instance.getTexture());
//		GameObject chunkObj = Game.getWorld().addObject("Chunk");
//		chunkObj.transform.position.zero();
//		chunkObj.addComponent(new ComponentMesh(mesh));
		
		GameObject camObj = Game.getWorld().addObject("Camera");
		camObj.addComponent(new ComponentCamera(Game.getWindow().getWindowSize())).setClearColor(new Vector3f(0.1f, 0.1f, 0.1f));
		camObj.addComponent(new ComponentMouseLook()).setMouseLock(true).setPauseTimeOnFreeCursor(true).setPausedTimeScale(0.0d).setSmoothing(0.13f);
		camObj.addComponent(new ComponentFreeMove()).doManualMove(new Vector3f(0.0f, 0.0f, 10.0f), false);
		
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
	
}