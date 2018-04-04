package com.cjburkey.voxicus.event;

import java.util.ArrayList;
import java.util.List;

public class EventHandler<T extends IEvent> {
	
	private final List<IEventListener<T>> listeners = new ArrayList<>();
	
	public void addListener(IEventListener<T> listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IEventListener<T> listener) {
		listeners.remove(listener);
	}
	
	public void clearListeners() {
		listeners.clear();
	}
	
	@SuppressWarnings("unchecked")
	public void triggerEvent(IEvent e) {
		for (IEventListener<T> listener : listeners) {
			if (e.getCanCancel() && e.getIsCancelled()) {
				return;
			}
			listener.onCall((T) e);
		}
	}
	
}