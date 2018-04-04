package com.cjburkey.voxicus.core;

public interface IInstance {
	
	String getName();
	SemVer getVersion();
	void preinit();
	void init();
	void update();
	void render();
	void exit();
	
}