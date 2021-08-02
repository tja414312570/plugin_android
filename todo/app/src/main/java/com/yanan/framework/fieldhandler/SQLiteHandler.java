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

public class SQLiteHandler implements FieldHandler<SQLite> {
    private static Map<Class<?>, InstanceHandler> handlerMap = new HashMap<>();
    static {
        Plugin.register(SQLite.class,new SQLiteHandler());
    }
    public static <T> void register(Class<T> type, InstanceHandler instanceHandler){
        handlerMap.put(type,instanceHandler);
    }
    @Override
    public void process(Activity activity, Object obj,Field field, SQLite service) {
        Object sqlite = activity.openOrCreateDatabase(service.value(),service.mode(),null);
        Log.d(getClass().getSimpleName(),field+"==>"+sqlite);
        try {
            ReflectUtils.setFieldValue(field,obj,sqlite);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
