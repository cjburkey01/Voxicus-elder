package com.cjburkey.voxicus.core;

public class ValueTypePair<T> {
	
	public final DataType<T> type;
	public final T value;
	
	public ValueTypePair(DataType<T> type, T value) {
		this.type = type;
		this.value = value;
	}
	
}