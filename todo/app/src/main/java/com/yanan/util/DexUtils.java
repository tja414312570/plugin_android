package com.yanan.util;

import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;

public class DexUtils {
    private static final String TAG = "DEX_UTILS";

    private static List<DexFile> getMultiDex() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        BaseDexClassLoader dexLoader = (BaseDexClassLoader) Thread.currentThread().getContextClassLoader();
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        Object pathList = ReflectUtils.getFieldValue(pathListField,dexLoader);
//        Field f = getField("pathList", ClassHelper.getClassHelper("dalvik.system.BaseDexClassLoader").getClass());
//        Object pathList = getObjectFromField(f, dexLoader);
//        Field f2 = getField("dexElements", getClassByAddressName("dalvik.system.DexPathList"));
//        Object[] list = getObjectFromField(f2, pathList);
        Object[] list = ReflectUtils.getDeclaredFieldValue("dexElements",pathList);
//        Field f3 = getField("dexFile", getClassByAddressName("dalvik.system.DexPathList$Element"));
//        Field f3 = ClassHelper.getClassHelper("dalvik.system.DexPathList$Element").getDeclaredField("dexFile");
        List<DexFile> res = new ArrayList<>();

        for (int i = 0; i < list.length; i++) {
            DexFile d = ReflectUtils.getDeclaredFieldValue("dexFile", list[i]);
            res.add(d);
        }
        return res;
    }
    /**
     *
     * @Description: 根据包名获得该包以及子包下的所有类
     * @param path 包名
     * @return List<Class>    包下所有类
     */
    public static List<Class> getClasses(String path) throws ClassNotFoundException, IOException, NoSuchFieldException, IllegalAccessException {
        List<DexFile> dexFiles = getMultiDex();
        List<Class> classes = new ArrayList<>();
        for(DexFile dexFile : dexFiles){
            Enumeration<String> enumeration = dexFile.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while(enumeration.hasMoreElements()){
                String  className = enumeration.nextElement();
                if (className.startsWith(path)) {
                    Log.d(TAG,"find init class path :"+className);
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }
}
