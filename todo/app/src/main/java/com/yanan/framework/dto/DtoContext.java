package com.yanan.framework.dto;

import android.util.Log;

import com.yanan.framework.Plugin;
import com.yanan.framework.dto.annotations.Xml;
import com.yanan.framework.dto.entry.BaseMapping;
import com.yanan.framework.dto.entry.TagSupport;
import com.yanan.framework.dto.entry.WrapperMapping;
import com.yanan.framework.dto.fragment.FragmentBuilder;
import com.yanan.framework.dto.fragment.FragmentSet;
import com.yanan.framework.dto.fragment.SqlFragment;
import com.yanan.framework.dto.fragment.WrapMapping;
import com.yanan.framework.service.InstanceHandler;
import com.yanan.todo.R;
import com.yanan.util.xml.XMLHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoContext {
    private static Map<Integer, SqlFragmentManager> fragmentManagerMap = new HashMap<>();

    private static Map<Class<? extends TagSupport>,Class<? extends FragmentBuilder>> fragmentBuilderMapping = new HashMap<>();

    public static SqlFragmentManager getSqlFragmentManager(Integer resourceId) {
        SqlFragmentManager sqlFragmentManager = fragmentManagerMap.get(resourceId);
        if(sqlFragmentManager == null && !fragmentManagerMap.containsKey(resourceId)){
            sqlFragmentManager = createFragmentManager(resourceId);
            fragmentManagerMap.put(resourceId,sqlFragmentManager);
        }
        return sqlFragmentManager;
    }

    private static SqlFragmentManager createFragmentManager(Integer resourceId) {
        XMLHelper xmlHelper = new XMLHelper(R.xml.test, WrapperMapping.class);
        WrapperMapping wrapperMapping = xmlHelper.read();
        SqlFragmentManager sqlFragmentManager = new SqlFragmentManager();
        sqlFragmentManager.setWrapperMapping(wrapperMapping);
        String namespace = wrapperMapping.getNamespace();
        List<BaseMapping> baseMappingList = wrapperMapping.getBaseMappings();
        for(BaseMapping mapping : baseMappingList){
            mapping.setWrapperMapping(wrapperMapping);
            String sqlId = namespace + "." + mapping.getId();
            sqlFragmentManager.wrapMap.put(sqlId, mapping);
            Log.d("DTO_CTX", "found wrap id " + sqlId + " ; content : " + mapping.getContent().trim());
        }
        for(BaseMapping baseMapping : baseMappingList){
            buildFragment(baseMapping,sqlFragmentManager);
        }
        return sqlFragmentManager;
    }
    public static SqlFragment buildFragment(BaseMapping mapping,SqlFragmentManager sqlFragmentManager) {
        SqlFragment sqlFragment = null;
//        PlugsHandler handler = PlugsFactory.getPluginsHandler(mapping);
//        FragmentBuilder fragmentBuilder = PlugsFactory.getPluginsInstanceByAttributeStrict(FragmentBuilder.class,
//                handler.getProxyClass().getName() + ".root");
        FragmentBuilder fragmentBuilder = Plugin.createInstance(fragmentBuilderMapping.get(mapping.getClass()),false);

       Log.d("DTO_CTX","build " + mapping.getNode().toUpperCase() + " wrapper fragment , wrapper id : \""
                + mapping.getWrapperMapping().getNamespace() + "." + mapping.getId() + "\" ,ref : "+mapping.getWrapperMapping().isRef());
        sqlFragment = (SqlFragment) fragmentBuilder;
        if(!mapping.getWrapperMapping().isRef() || mapping.getParentMapping() != null) {
            fragmentBuilder.build(mapping);
            if(mapping.getParentMapping() == null)
                sqlFragmentManager.addWarp(sqlFragment);
        }
        return sqlFragment;
    }

    public static <T extends FragmentBuilder> Class<T> getFragmentSet(Class<?> aClass) {
        return (Class<T>) fragmentBuilderMapping.get(aClass);
    }
    public static void registerFragmentSet(Class<? extends TagSupport> tagClass,Class<? extends FragmentBuilder> fragmentBuilderClass) {
        fragmentBuilderMapping.put(tagClass,fragmentBuilderClass);
    }
}
