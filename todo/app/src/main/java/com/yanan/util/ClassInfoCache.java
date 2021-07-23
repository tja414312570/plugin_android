package com.yanan.util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 类信息缓存类，提供ClassHelper的缓存。
 * 支持ClassLoader的部分方法缓存
 * @author yanan
 */
public class ClassInfoCache {
	private static Map<Class<?>,ClassHelper> classCache = new LinkedHashMap<Class<?>,ClassHelper>();
	private static Map<String,String> fieldAddMethodCache = new LinkedHashMap<String,String>();
	private static Map<String,String> fieldSetMethodCache = new LinkedHashMap<String,String>();
	private static Map<String,String> fieldGetMethodCache = new LinkedHashMap<String,String>();
	private static Map<String,Class<?>> classMapCache = new LinkedHashMap<String,Class<?>>();
	/**
	 * 获取一个类的ClassHelper
	 * @param clzz target class
	 * @return class helper
	 */
	public static ClassHelper getClassHelper(Class<?> clzz){
		if(classCache.get(clzz)!=null)
			return classCache.get(clzz);
		try{
			ClassHelper cache = new ClassHelper(clzz);
			classCache.put(clzz, cache);
			return cache;
		}catch (Throwable t){
			throw new RuntimeException("failed to create class helper for "+clzz.getName(),t);
		}
		
	}
	public static String getFieldAddMethod(String name) {
		String str = fieldAddMethodCache.get(name);
		if(str==null){
			str = ReflectUtils.createFieldAddMethod(name);
			fieldAddMethodCache.put(name, str);
		}
		return str;
	}
	public static String getFieldAddMethod(Field field) {
		return getFieldAddMethod(field);
	}
	public static String getFieldSetMethod(String name) {
		String str = fieldSetMethodCache.get(name);
		if(str == null){
			str = ReflectUtils.createFieldSetMethod(name);
			fieldSetMethodCache.put(name, str);
		}
		return str;
	}
	public static String getFieldSetMethod(Field field) {
		return getFieldSetMethod(field.getName());
	}
	public static String getFieldGetMethod(String name) {
		String str = fieldGetMethodCache.get(name);
		if(str == null){
			str = ReflectUtils.createFieldGetMethod(name);
			fieldGetMethodCache.put(name, str);
		}
		return str;
	}
	public static String getFieldGetMethod(Field field) {
		return getFieldGetMethod(field.getName());
	}
	public static Class<?> classForName(String className) throws ClassNotFoundException {
		Class<?> clzz = classMapCache.get(className);
		if(clzz == null){
			clzz = Class.forName(className);
			classMapCache.put(className, clzz);
		}
		return clzz;
	}
}
