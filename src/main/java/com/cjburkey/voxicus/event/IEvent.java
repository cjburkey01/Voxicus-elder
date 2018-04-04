package com.cjburkey.voxicus.event;

public interface IEvent {
	
	String getName();
	boolean getCanCancel();
	boolean getIsCancelled();
	void cancel();
	
}