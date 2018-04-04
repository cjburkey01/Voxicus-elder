package com.cjburkey.voxicus.game;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.voxicus.block.Block;
import com.cjburkey.voxicus.chunk.Chunk;
import com.cjburkey.voxicus.component.ComponentCamera;
import com.cjburkey.voxicus.component.ComponentFreeMove;
import com.cjburkey.voxicus.component.ComponentMouseLook;
import com.cjburkey.voxicus.core.Bounds;
import com.cjburkey.voxicus.core.Debug;
import com.cjburkey.voxicus.core.IInstance;
import com.cjburkey.voxicus.core.Input;
import com.cjburkey.voxicus.core.SemVer;
import com.cjburkey.voxicus.core.Time;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.graphic.MeshTexture;
import com.cjburkey.voxicus.graphic.Texture;
import com.cjburkey.voxicus.gui.GuiBox;
import com.cjburkey.voxicus.world.GameObject;
import com.cjburkey.voxicus.world.Scene;

public class InstanceVoxicus implements IInstance {
	
	private Chunk chunk = new Chunk(new Vector3i());
	private Block block = new Block();
	
	// Test values
	private float delta = 2.0f;
	
	public String getName() {
		return "Voxicus";
	}
	
	public SemVer getVersion() {
		return new SemVer(0, 0, 1, "alpha");
	}
	
	public void init() {
		Texture texture = new Texture("/res/voxicus/texture/terrain/main.png");
		MeshTexture mesh = new MeshTexture();
		
		for (int z = 0; z < Chunk.SIZE; z ++) {
			for (int y = 0; y < Chunk.SIZE; y ++) {
				for (int x = 0; x < Chunk.SIZE; x ++) {
					chunk.setBlock(new Vector3i(x, y, z), block);
				}
			}
		}
		
		List<Vector3f> verts = new ArrayList<>();
		List<Short> inds = new ArrayList<>();
		List<Vector2f> uvs = new ArrayList<>();
		for (int z = 0; z < Chunk.SIZE; z ++) {
			for (int y = 0; y < Chunk.SIZE; y ++) {
				for (int x = 0; x < Chunk.SIZE; x ++) {
					boolean drawAtXp1 = chunk.getIsTransparentBlockAt(new Vector3i(x + 1, y, z));
					boolean drawAtXm1 = chunk.getIsTransparentBlockAt(new Vector3i(x - 1, y, z));
					boolean drawAtZp1 = chunk.getIsTransparentBlockAt(new Vector3i(x, y, z + 1));
					boolean drawAtZm1 = chunk.getIsTransparentBlockAt(new Vector3i(x, y, z - 1));
					boolean drawAtYp1 = chunk.getIsTransparentBlockAt(new Vector3i(x, y + 1, z));
					boolean drawAtYm1 = chunk.getIsTransparentBlockAt(new Vector3i(x, y - 1, z));
					Util.addCube(verts, inds, uvs, new Vector3f(x, y, z), 1.0f, new Boolean[] {
						drawAtZp1, drawAtZm1, drawAtXp1, drawAtXm1, drawAtYp1, drawAtYm1
					});
				}
			}
		}
		
//		mesh.setMesh(verts, inds, uvs, texture);
//		GameObject chunkObj = Game.getWorld().addObject();
//		chunkObj.transform.position.zero();
//		chunkObj.addComponent(new ComponentMesh(mesh));
		
		GameObject camObj = Game.getWorld().addObject();
		camObj.addComponent(new ComponentCamera(Game.getWindow().getWindowSize())).setClearColor(new Vector3f(0.1f, 0.1f, 0.1f));
		camObj.addComponent(new ComponentMouseLook()).setMouseLock(true).setPauseTimeOnFreeCursor(true).setPausedTimeScale(0.0d).setSmoothing(0.13f);
		camObj.addComponent(new ComponentFreeMove()).doManualMove(new Vector3f(0.0f, 0.0f, 10.0f), false);
		
		Scene.getActive().getGuiHandler().addElement(new GuiBox(new Bounds(10.0f, 10.0f, 100.0f, 100.0f), new Texture("/res/voxicus/texture/gui/box.png")));
	}
	
	public void update() {
		boolean p = false;
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_DOWN)) {
			Time.setTimeScale(Time.getTimeScale() - ((Input.getIsKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) ? (delta * 10) :  (delta)) * Time.getPureDeltaTime());
			p = true;
		}
		if (Input.getIsKeyDown(GLFW.GLFW_KEY_UP)) {
			Time.setTimeScale(Time.getTimeScale() + ((Input.getIsKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) ? (delta * 10) :  (delta)) * Time.getPureDeltaTime());
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