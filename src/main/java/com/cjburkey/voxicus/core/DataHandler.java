package com.cjburkey.voxicus.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DataHandler {
	
	private static final List<DataType<?>> TYPES = new ArrayList<>();
	private final Map<String, ValueTypePair<?>> data = new HashMap<>();
	
	public DataHandler() {
	}
	
	public DataHandler(DataHandler copy) {
		for (Entry<String, ValueTypePair<?>> entry : copy.data.entrySet()) {
			data.put(entry.getKey(), entry.getValue());
		}
	}
	
	public <T> void set(String key, T value, DataType<T> type) {
		data.put(key, new ValueTypePair<T>(type, value));
	}
	
	public <T> void set(String key, T value) {
		DataType<T> type = getType(value);
		if (type == null) {
			Debug.error("Failed to determine type for value: {}", value);
			return;
		}
		set(key, value, type);
	}
	
	@SuppressWarnings("unchecked")
	private <T> DataType<T> getType(T value) {
		if (TYPES.isEmpty()) {
			scanTypes(DataType.class);
		}
		for (DataType<?> type : TYPES) {
			if (type.isType(value)) {
				return (DataType<T>) type;
			}
		}
		return null;
	}
	
	public static void scanTypes(Class<?> cls) {
		try {
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				if (field.getType().equals(DataType.class)) {
					TYPES.add((DataType<?>) field.get(null));
				}
			}
			Debug.log("Added Data Types:");
			TYPES.forEach(type -> Debug.log("  " + type.name));
		} catch (Exception e) {
			Debug.error(e, true);
		}
	}
	
}