package com.uas.utils;


public class KeyObject{

	private Object name, value;
	
	public KeyObject(){}

	public KeyObject(Object name, Object value){
		super();
		this.name = name;
		this.value = value;
	}

	public Object getName() {
		return name;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setName(Object name) {
		this.name = name;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
}
