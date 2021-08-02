package com.yanan.framework.dto.entry;

import com.yanan.util.xml.AsXml;
import com.yanan.util.xml.Mapping;
import com.yanan.util.xml.MappingGroup;
import com.yanan.util.xml.Value;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TagSupport{
	@AsXml
	protected String xml;
	@Value
	protected String value = "";
	@MappingGroup(support = TagSupport.class,
			value = {
			@Mapping(node = "var", target = Var.class),
			@Mapping(node = "val", target = Val.class),
			@Mapping(node = "trim", target = Trim.class),
			@Mapping(node = "if", target = IF.class),
			@Mapping(node = "foreach", target = ForEach.class),
			@Mapping(node = "include", target = Include.class),
			@Mapping(node = "case", target = Case.class),
			@Mapping(node = "when", target = When.class),
			@Mapping(node = "default", target = Default.class)
			}
	)
	protected List<TagSupport> tags = Collections.EMPTY_LIST;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<TagSupport> getTags() {
		return tags;
	}
	public void setTags(List<TagSupport> tags) {
		this.tags = tags;
	}
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
	@Override
	public String toString() {
		return "TagSupport [value=" + value + ", tags=" + tags + "]";
	}
}