package com.cjburkey.voxicus.core;

public interface IInstance {
	
	String getName();
	SemVer getVersion();
	void init();
	void update();
	void render();
	void exit();
	
}