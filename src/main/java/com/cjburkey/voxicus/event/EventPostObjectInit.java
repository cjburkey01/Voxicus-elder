package com.cjburkey.voxicus.event;

public class EventPostObjectInit implements IEvent {
	
	public String getName() {
		return getClass().getSimpleName();
	}
	
	public boolean getCanCancel() {
		return false;
	}
	
	public boolean getIsCancelled() {
		return false;
	}
	
	public void cancel() {
	}
	
}