package com.yanan.framework.dto;

import com.yanan.framework.dto.entry.BaseMapping;
import com.yanan.framework.dto.entry.WrapperMapping;
import com.yanan.framework.dto.fragment.SqlFragment;
import com.yanan.framework.dto.fragment.WrapMapping;

import java.util.HashMap;
import java.util.Map;

public class SqlFragmentManager {
    private Map<String, WrapMapping> wrapMapping = new HashMap<>();
    Map<String, BaseMapping> wrapMap = new HashMap<>();

    public String getNamespace() {
        return namespace;
    }

    private String namespace;

    public SqlFragmentManager(String namespace) {
        this.namespace = namespace;
    }

    public void addWarp(SqlFragment sqlFragment){
        String namespace = sqlFragment.getBaseMapping().getWrapperMapping().getNamespace();
        WrapMapping wrapperMapping = wrapMapping.get(namespace);
        if(wrapperMapping==null){
            wrapperMapping = new WrapMapping(namespace);
            wrapMapping.put(namespace,wrapperMapping);
        }
        wrapperMapping.addSqlFragment(sqlFragment);
    }
    public SqlFragment getSqlFragment(String id){
        int symIndex = id.lastIndexOf(".");
        if(symIndex==-1)
            throw new RuntimeException("id \"" +id+"\" does not container namespace symbol \".\"");
        String namespace = id.substring(0,symIndex);
        WrapMapping wrapperMapping = wrapMapping.get(namespace);
        if(wrapperMapping==null)
            throw new RuntimeException("could not found wrapper \""+id+"\" at namespace \"" +namespace+"\"!");
        String idl = id.substring(symIndex+1);
        SqlFragment sqlFragment = wrapperMapping.getSqlFragment(idl);
        if(sqlFragment==null)
            throw new RuntimeException("could not found wrapper \""+idl+"\" at namespace \"" +namespace+"\"!");
        return sqlFragment;
    }

    public void setWrapperMapping(WrapperMapping wrapperMapping) {
        this.wrapMapping = wrapMapping;
    }
    public BaseMapping getWrapper(String id) {
        return this.wrapMap.get(id);
    }
}
