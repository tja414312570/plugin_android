package com.yanan.framework.dto.entry;


import com.yanan.util.xml.Attribute;

public class IF extends TagSupport{
	@Attribute
	private String test;
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	@Override
	public String toString() {
		return "IF [test=" + test + ", value=" + value + "]";
	}
}