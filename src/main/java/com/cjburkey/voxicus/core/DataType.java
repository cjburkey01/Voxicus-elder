package com.cjburkey.voxicus.core;

public abstract class DataType<T> {
	
	public static final DataType<String> TYPE_STRING = new DataType<String>("string") {
		public String cast(Object in) {
			return (String) in;
		}
		public String fromData(String data) {
			return data;
		}
		public String toData(String data) {
			return data;
		}
		public boolean isType(Object data) {
			return data instanceof String;
		}
	};
	public static final DataType<Float> TYPE_FLOAT = new DataType<Float>("float") {
		public Float cast(Object in) {
			return (Float) in;
		}
		public Float fromData(String data) {
			return Float.parseFloat(data);
		}
		public String toData(Float data) {
			return data.toString();
		}
		public boolean isType(Object data) {
			return data instanceof Float;
		}
	};
	public static final DataType<Integer> TYPE_INT = new DataType<Integer>("int") {
		public Integer cast(Object in) {
			return (Integer) in;
		}
		public Integer fromData(String data) {
			return Integer.parseInt(data);
		}
		public String toData(Integer data) {
			return data.toString();
		}
		public boolean isType(Object data) {
			return data instanceof Integer;
		}
	};
	public static final DataType<Double> TYPE_DOUBLE = new DataType<Double>("double") {
		public Double cast(Object in) {
			return (Double) in;
		}
		public Double fromData(String data) {
			return Double.parseDouble(data);
		}
		public String toData(Double data) {
			return data.toString();
		}
		public boolean isType(Object data) {
			return data instanceof Double;
		}
	};
	public static final DataType<Long> TYPE_LONG = new DataType<Long>("long") {
		public Long cast(Object in) {
			return (Long) in;
		}
		public Long fromData(String data) {
			return Long.parseLong(data);
		}
		public String toData(Long data) {
			return data.toString();
		}
		public boolean isType(Object data) {
			return data instanceof Long;
		}
	};
	
	public final String name;
	
	protected DataType(String name) {
		this.name = name;
	}
	
	public abstract T cast(Object in);
	public abstract T fromData(String data);
	public abstract String toData(T data);
	public abstract boolean isType(Object data);
	
}