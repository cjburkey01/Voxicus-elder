package com.cjburkey.voxicus.event;

import java.util.HashMap;
import java.util.Map;

public class EventSystem {
	
	private final Map<Class<? extends IEvent>, EventHandler<? extends IEvent>> eventHandlers = new HashMap<>();
	
	public <T extends IEvent> void addListener(Class<T> event, IEventListener<T> listener) {
		getOrCreateHandler(event).addListener(listener);
	}
	
	public <T extends IEvent> void removeListener(Class<T> event, IEventListener<T> listener) {
		getOrCreateHandler(event).removeListener(listener);
	}
	
	public <T extends IEvent> void clearListeners(Class<T> event) {
		getOrCreateHandler(event).clearListeners();
	}
	
	public void triggerEvent(IEvent e) {
		getOrCreateHandler(e.getClass()).triggerEvent(e);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends IEvent> EventHandler<T> getOrCreateHandler(Class<T> event) {
		EventHandler<? extends IEvent> at = eventHandlers.get(event);
		if (at != null) {
			return (EventHandler<T>) at;
		}
		EventHandler<T> e = new EventHandler<>();
		eventHandlers.put(event, e);
		return e;
	}
	
}