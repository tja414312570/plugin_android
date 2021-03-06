package com.yanan.framework;

import com.yanan.util.DexUtils;
import com.yanan.util.HashMaps;
import com.yanan.util.ReflectUtils;
import com.yanan.util.asserts.Assert;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RResourceHelper {
    final static Map<String,Class<?>> resourceClassCache = new HashMaps<>();
    final static Map<Integer,String> resourceIntegerMap = new HashMap<>();
    public static int getResourceId(String attr, String name) {
        check();
        Class<?> clazz = resourceClassCache.get(attr);
        Assert.isNotNull(clazz,"could not get attr "+attr+" at R class");
        try {
            return ReflectUtils.getFieldValue(name,clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static String getResourceName(String attr, Integer id) {
        check();
        Class<?> clazz = resourceClassCache.get(attr);
        Assert.isNotNull(clazz,"could not get attr "+attr+" at R class");
        try {
            if(resourceIntegerMap.isEmpty()){
                Field[] fields = clazz.getFields();
                for(Field field : fields)
                    resourceIntegerMap.put(ReflectUtils.getFieldValue(field,clazz),field.getName());
            }
            return resourceIntegerMap.get(id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    private synchronized static void check() {
        if(resourceClassCache == null || resourceClassCache.isEmpty()){
            try {
                final String rPackageName = Plugin.currentContext().getPackageName()+".R";
                List<Class> classList = DexUtils.getClasses(rPackageName);
                String typeName;
                for(Class clazz : classList){
                    typeName= clazz.getSimpleName();
                    if(typeName.equals("R"))
                        continue;
                    resourceClassCache.put(typeName,clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
