package com.yanan.framework.dto;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yanan.framework.Plugin;
import com.yanan.framework.StringHolder;
import com.yanan.framework.dto.annotations.SQL;
import com.yanan.framework.dto.annotations.SQLs;
import com.yanan.framework.dto.annotations.Xml;
import com.yanan.framework.dto.entry.BaseMapping;
import com.yanan.framework.dto.entry.SelectorMapping;
import com.yanan.framework.dto.entry.TagSupport;
import com.yanan.framework.dto.entry.WrapperMapping;
import com.yanan.framework.dto.fragment.FragmentBuilder;
import com.yanan.framework.dto.fragment.SqlFragment;
import com.yanan.framework.dto.mapper.DefaultSqlSessionExecuter;
import com.yanan.framework.dto.proxy.DtoProxy;
import com.yanan.framework.fieldhandler.SQLite;
import com.yanan.todo.R;
import com.yanan.util.StringUtil;
import com.yanan.util.xml.XMLHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DtoContext {
    private static final Map<Integer, SqlFragmentManager> fragmentManagerMap = new HashMap<>();
    private static final Map<String, SqlFragmentManager> fragmentNameManagerMap = new HashMap<>();
    private static final Map<Class<? extends TagSupport>,Class<? extends FragmentBuilder>> fragmentBuilderMapping = new HashMap<>();

    public static SqlFragmentManager getSqlFragmentManager(Integer resourceId) {
        SqlFragmentManager sqlFragmentManager = fragmentManagerMap.get(resourceId);
        if(sqlFragmentManager == null && !fragmentManagerMap.containsKey(resourceId)){
            sqlFragmentManager = createFragmentManager(resourceId);
            String namespace = sqlFragmentManager.getNamespace();
            fragmentManagerMap.put(resourceId,sqlFragmentManager);
            fragmentNameManagerMap.put(sqlFragmentManager.getNamespace(),sqlFragmentManager);
            try{
                Class<?> dtoClass = Class.forName(namespace);
                createFragmentManager(dtoClass,namespace);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sqlFragmentManager;
    }
    public static SqlFragmentManager getSqlFragmentManager(String namespace) {
        return fragmentNameManagerMap.get(namespace);
    }
    public static SqlFragmentManager getSqlFragmentManager(Class<?> clazz) {
        String namespace = clazz.getName();
        SqlFragmentManager sqlFragmentManager = fragmentNameManagerMap.get(namespace);
        if(sqlFragmentManager == null && !fragmentNameManagerMap.containsKey(namespace)){
            sqlFragmentManager = createFragmentManager(clazz,namespace);
            fragmentNameManagerMap.put(namespace,sqlFragmentManager);
            Xml xml = clazz.getAnnotation(Xml.class);
            if(xml != null){
                createFragmentManager(xml.value());
                fragmentManagerMap.put(xml.value(),sqlFragmentManager);
            }
        }
        return sqlFragmentManager;
    }

    private static SqlFragmentManager createFragmentManager(Class<?> clazz, String namespace) {
        SqlFragmentManager sqlFragmentManager = fragmentNameManagerMap.get(namespace);
        if(sqlFragmentManager == null)
            sqlFragmentManager = new SqlFragmentManager(namespace);
        WrapperMapping wrapperMapping = new WrapperMapping();
        wrapperMapping.setNamespace(namespace);
        SQLs sqlArray = clazz.getAnnotation(SQLs.class);
        List<BaseMapping> baseMappingList = new ArrayList<>();
        wrapperMapping.setBaseMappings(baseMappingList);
        if(sqlArray != null){
            SQL[] sols = sqlArray.value();
            for(SQL sql : sols){
                if(sql.id().isEmpty())
                    throw new RuntimeException("Sql id is null");
                BaseMapping baseMapping = new SelectorMapping();
                baseMapping.setNode("sql");
                baseMapping.setValue(sql.value());
                baseMapping.setContent(sql.value());
                baseMapping.setXml(sql.value());
                baseMapping.setId(sql.id());
                bind(sqlFragmentManager,baseMapping,wrapperMapping);
            }
        }
        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            SQL sql = method.getAnnotation(SQL.class);
            if(sql != null){
                BaseMapping baseMapping = new SelectorMapping();
                baseMapping.setId(sql.id());
                if(StringUtil.isEmpty(sql.id()))
                    baseMapping.setId(method.getName());
                if(sql.value().toLowerCase(Locale.ROOT).trim().startsWith("insert"))
                    baseMapping.setNode("insert");
                else if(sql.value().toLowerCase(Locale.ROOT).trim().startsWith("update"))
                    baseMapping.setNode("update");
                else if(sql.value().toLowerCase(Locale.ROOT).trim().startsWith("query"))
                    baseMapping.setNode("query");
                else if(sql.value().toLowerCase(Locale.ROOT).trim().startsWith("delete"))
                    baseMapping.setNode("delete");
                else
                    baseMapping.setNode("sql");
                String xml = sql.value();
                int index = -1;
                StringBuilder stringBuilder = new StringBuilder();
                while((index = xml.indexOf("{{"))!= -1){
                    stringBuilder.setLength(0);
                    String prefix = xml.substring(0,index);
                    int endex = xml.indexOf("}}",index);
                    if(endex == -1)
                        throw new RuntimeException("not string holder end symbol");
                    String holder = namespace+"."+xml.substring(index+2,endex);
                    String suffix = xml.substring(endex+2);
                    holder = sqlFragmentManager.getWrapper(holder).getXml();
                    if(holder == null)
                        holder = StringHolder.decodeString(xml.substring(index+1,endex+1));
                    stringBuilder.append(xml.substring(0,index)).append(holder).append(suffix);
                    xml = stringBuilder.toString();
                }
                baseMapping.setValue(xml);
                baseMapping.setXml(xml);
                baseMapping.setContent(xml);
                bind(sqlFragmentManager,baseMapping,wrapperMapping);
            }
        }
        return sqlFragmentManager;
    }
    private static void bind(SqlFragmentManager sqlFragmentManager,BaseMapping baseMapping, WrapperMapping wrapperMapping){
        wrapperMapping.getBaseMappings().add(baseMapping);
        baseMapping.setWrapperMapping(wrapperMapping);
        String sqlId = sqlFragmentManager.getNamespace() + "." + baseMapping.getId();
        sqlFragmentManager.wrapMap.put(sqlId, baseMapping);
        Log.d("DTO_CTX", "found wrap id " + sqlId + " ; content : " + baseMapping.getContent().trim());
        buildFragment(baseMapping,sqlFragmentManager);
    }
    private static SqlFragmentManager createFragmentManager(Integer resourceId) {
        XMLHelper xmlHelper = new XMLHelper(R.xml.test, WrapperMapping.class);
        WrapperMapping wrapperMapping = xmlHelper.read();
        SqlFragmentManager sqlFragmentManager = fragmentNameManagerMap.get(wrapperMapping.getNamespace());
        if(sqlFragmentManager == null)
            sqlFragmentManager = new SqlFragmentManager(wrapperMapping.getNamespace());
        sqlFragmentManager.setWrapperMapping(wrapperMapping);
        String namespace = wrapperMapping.getNamespace();
        List<BaseMapping> baseMappingList = wrapperMapping.getBaseMappings();
        for(BaseMapping baseMapping : baseMappingList){
            bind(sqlFragmentManager,baseMapping,wrapperMapping);
        }
        return sqlFragmentManager;
    }
    public static SqlFragment buildFragment(BaseMapping mapping,SqlFragmentManager sqlFragmentManager) {
        SqlFragment sqlFragment = null;
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

    public static <T> T createDtoProxyInstance(Activity activity,Class<T> instanceType){
        SQLite sqLite = instanceType.getAnnotation(SQLite.class);
        Log.d("DTO_HANDLER",instanceType.getName()+sqLite.toString());
        SQLiteDatabase sqLiteDatabase = activity.openOrCreateDatabase(sqLite.value(),sqLite.mode(),null);
        SqlFragmentManager sqlFragmentManager = DtoContext.getSqlFragmentManager(instanceType);
        SqlSession sqlSession = new DefaultSqlSessionExecuter(sqLiteDatabase,sqlFragmentManager);
        InvocationHandler proxy = new DtoProxy(activity,instanceType,sqlFragmentManager,sqlSession);
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{instanceType},proxy);
    }
}
