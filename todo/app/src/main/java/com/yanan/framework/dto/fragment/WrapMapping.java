package com.yanan.framework.dto.fragment;

import com.yanan.framework.fieldhandler.Singleton;

import java.util.HashMap;
import java.util.Map;
@Singleton(false)
public class WrapMapping {
	private String namespace;
	private Map<String,SqlFragment> wrapps =new HashMap<String,SqlFragment>();
	public Map<String, SqlFragment> getWrapps() {
		return wrapps;
	}
	public void setWrapps(Map<String, SqlFragment> wrapps) {
		this.wrapps = wrapps;
	}
	public String getNamespace() {
		return namespace;
	}
	public WrapMapping(String namespace) {
		super();
		this.namespace = namespace;
	}
	public void addSqlFragemnt(SqlFragment sqlFragment){
		String id = sqlFragment.getId();
		SqlFragment frag = wrapps.get(id);
		if(frag!=null)
			throw new RuntimeException("wrapper \""+namespace+"."+id+"\" is exists!");
		wrapps.put(id, sqlFragment);
	}
	public SqlFragment getSqlFragment(String id){
		SqlFragment frag = wrapps.get(id);
		if(frag==null)
			throw new RuntimeException("wrapper \""+namespace+"."+id+"\" is not exists!");
		return frag;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
}