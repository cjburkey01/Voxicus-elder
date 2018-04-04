package com.cjburkey.voxicus.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.voxicus.core.Bounds;
import com.cjburkey.voxicus.core.Util;
import com.cjburkey.voxicus.mesh.MeshTextureUI;
import com.cjburkey.voxicus.texture.Texture;

public class GuiBox extends GuiElement {
	
	private MeshTextureUI mesh;
	private Texture texture;
	
	public GuiBox(Bounds bounds, Texture texture) {
		super(bounds);
		this.texture = texture;
	}
	
	public void onCreate(GuiHandler parent) {
		mesh = new MeshTextureUI();
		mesh.setMesh(Util.arrayToList(new Vector3f[] {
			new Vector3f(bounds.getMin(), 0.0f),
			new Vector3f(bounds.getMin().x, bounds.getMax().y, 0.0f),
			new Vector3f(bounds.getMax().x, bounds.getMax().y, 0.0f),
			new Vector3f(bounds.getMax().x, bounds.getMin().y, 0.0f)
		}), Util.arrayToList(new Short[] {
			0, 1, 2,
			0, 2, 3
		}), Util.arrayToList(new Vector2f[] {
			new Vector2f(0.0f, 0.0f),
			new Vector2f(0.0f, 1.0f),
			new Vector2f(1.0f, 1.0f),
			new Vector2f(1.0f, 0.0f)
		}), texture);
	}
	
	public void onRender(GuiHandler parent) {
		mesh.onRender();
	}
	
	public void onRemove(GuiHandler parent) {
		mesh.onDestroy();
	}
	
}