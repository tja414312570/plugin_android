package com.yanan.framework.dto.entry;


import com.yanan.util.xml.Attribute;

public class When extends TagSupport{

	@Attribute
	private String test;
	
	@Attribute(name="break")
	private boolean breaks;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	public boolean isBreaks() {
		return breaks;
	}

	public void setBreaks(boolean breaks) {
		this.breaks = breaks;
	}
	@Override
	public String toString() {
		return "When [test=" + test + ", breaks=" + breaks + ", value=" + value + ", tags=" + tags
				+ "]";
	}
	
}