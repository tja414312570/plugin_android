package com.yanan.framework.dto.entry;

import com.yanan.util.xml.Attribute;
import com.yanan.util.xml.Ignore;
import com.yanan.util.xml.LineNumber;
import com.yanan.util.xml.Tag;
import com.yanan.util.xml.Value;
import com.yanan.util.xml.XmlResource;

import java.util.List;


public abstract class BaseMapping extends TagSupport{
	@Tag
	protected String node;
	@Attribute
	protected String id;
	@Attribute
	protected String resultType;
	@Attribute
	protected String parameterType;
	@Value
	protected String content;
	@LineNumber
	protected int lineNum;
	protected WrapperMapping wrapperMapping;
	@Ignore
	protected BaseMapping parentMapping;
	public List<TagSupport> getTags() {
		return tags;
	}
	public void setTags(List<TagSupport> tags) {
		this.tags = tags;
	}
	@Override
	public String toString() {
		return "BaseMapping{node='" + node + '\'' +
				", id='" + id + '\'' +
				", resultType='" + resultType + '\'' +
				", parameterType='" + parameterType + '\'' +
				", content='" + content + '\'' +
				", lineNum=" + lineNum +
				", wrapperMapping=" + wrapperMapping +
				", parentMapping=" + parentMapping +
				", value='" + value + '\'' +
				", tags=" + tags +
				'}';
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public WrapperMapping getWrapperMapping() {
		return wrapperMapping;
	}
	public void setWrapperMapping(WrapperMapping wrapperMapping) {
		this.wrapperMapping = wrapperMapping;
	}
	public BaseMapping getParentMapping() {
		return parentMapping;
	}
	public void setParentMapping(BaseMapping parentMapping) {
		this.parentMapping = parentMapping;
	}
}