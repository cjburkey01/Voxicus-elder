package com.cjburkey.voxicus.component;

import com.cjburkey.voxicus.GameObject;

public abstract class ObjectComponent extends Component {
	
	public final GameObject getParentObj() {
		if (!(GameObject.class.isAssignableFrom(parent.getClass()))) {
			return null;
		}
		return (GameObject) parent;
	}
	
}