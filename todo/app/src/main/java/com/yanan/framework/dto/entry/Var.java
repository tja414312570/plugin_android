package com.yanan.framework.dto.entry;


import com.yanan.util.xml.Attribute;

public class Var extends TagSupport{
	@Attribute
	private String id;
	@Attribute
	private String value;
	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Var [id=" + id + ", value=" + value + "]";
	}
}