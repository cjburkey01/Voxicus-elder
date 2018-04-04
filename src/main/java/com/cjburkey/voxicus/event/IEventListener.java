package com.cjburkey.voxicus.event;

@FunctionalInterface
public interface IEventListener<T extends IEvent> {
	
	void onCall(T e);
	
}