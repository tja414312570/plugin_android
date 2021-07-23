package com.yanan.framework.fieldhandler;

import android.app.Activity;
import android.util.Log;

import com.yanan.framework.FieldHandler;
import com.yanan.framework.Plugin;
import com.yanan.framework.service.InstanceHandler;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ServiceHandler implements FieldHandler<Service> {
    private static Map<Class<?>, InstanceHandler> handlerMap = new HashMap<>();
    static {
        Plugin.register(Service.class,new ServiceHandler());
    }
    public static <T> void register(Class<T> type, InstanceHandler instanceHandler){
        handlerMap.put(type,instanceHandler);
    }
    @Override
    public void process(Activity activity, Object obj,Field field, Service service) {
        Class<?> fieldType = field.getType();
        Object instance = null;
        InstanceHandler instanceHandler = handlerMap.get(fieldType);
        if(instanceHandler!= null){
            instance = instanceHandler.instance(activity,fieldType);
        }else{
            instance = Plugin.getInstance(fieldType);
            if(instance == null){
                instance = Plugin.createInstance(fieldType,fieldType.getAnnotation(AutoInject.class) != null);
            }
        }
        Log.d(getClass().getSimpleName(),field+"==>"+instance);
        try {
            ReflectUtils.setFieldValue(field,obj,instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
