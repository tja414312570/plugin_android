package com.yanan.framework.dto.entry;


import com.yanan.util.xml.Attribute;

public class Val extends TagSupport{
	@Attribute
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Val [id=" + id + "]";
	}
}