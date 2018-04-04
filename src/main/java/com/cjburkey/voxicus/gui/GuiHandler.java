package com.cjburkey.voxicus.gui;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.voxicus.core.Transformations;
import com.cjburkey.voxicus.graphic.ShaderProgram;

public class GuiHandler {
	
	private final List<GuiElement> elements = new ArrayList<>();
	
	public void addElement(GuiElement e) {
		elements.add(e);
		e.onCreate(this);
	}
	
	public void removeElement(GuiElement e) {
		e.setDestroyed(this);
	}
	
	public void removeElements() {
		for (GuiElement element : elements) {
			removeElement(element);
		}
	}
	
	public void onRender(ShaderProgram shader) {
		for (int i = 0; i < elements.size(); i ++) {
			if (elements.get(i).getIsDestroyed()) {
				elements.get(i).onRemove(this);
				elements.remove(i);
				i --;
			}
		}
		shader.bind();
		shader.setUniform("projectionMatrix", Transformations.ORTHOGRAPHIC);
		elements.forEach(e -> {
			Transformations.updateModelOrthographics(e.getBounds().getMin(), e.getRotation(), e.getScale());
			shader.setUniform("guiMatrix", Transformations.MODEL_ORTHOGRAPHIC);
			e.onRender(this);
		});
	}
	
}