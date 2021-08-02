package com.yanan.framework.dto.entry;

import com.yanan.util.xml.Attribute;
import com.yanan.util.xml.ENCODEING;
import com.yanan.util.xml.Element;
import com.yanan.util.xml.Encode;
import com.yanan.util.xml.FieldType;
import com.yanan.util.xml.FieldTypes;
import com.yanan.util.xml.Mapping;

import java.util.List;


@Encode(ENCODEING.UTF16)
@Element(name="wrapper")
@FieldType(FieldTypes.ALL)
public class WrapperMapping{
	@Attribute
	private String namespace;
	@Attribute
	private String database;
	@Attribute
	private boolean ref;
	@Mapping(node = "select", target = SelectorMapping.class)
	@Mapping(node = "insert", target = SelectorMapping.class)
	@Mapping(node = "update", target = SelectorMapping.class)
	@Mapping(node = "delete", target = SelectorMapping.class)
	@Mapping(node = "sql", target = SelectorMapping.class)
	private List<BaseMapping> baseMappings;
	public boolean isRef() {
		return ref;
	}
	public void setRef(boolean ref) {
		this.ref = ref;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	@Override
	public String toString() {
		return "WrapperMapping [namespace=" + namespace + ", database=" + database + ", baseMappings=" + baseMappings .toString().replace("],","\r\n")+ "]";
	}
	public List<BaseMapping> getBaseMappings() {
		return baseMappings;
	}
	public void setBaseMappings(List<BaseMapping> baseMappings) {
		this.baseMappings = baseMappings;
	}
}