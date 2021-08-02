package com.yanan.framework.dto.entry;

public class SelectorMapping extends BaseMapping{
	@Override
	public String toString() {
		return "SelectorMapping [node=" + node + ", id=" + id + ", resultType=" + resultType
				+ ", parameterType=" + parameterType + ", content=" + content +", line="+lineNum
				+ ",tags=" + tags + "]";
	}
}