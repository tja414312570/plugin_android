package com.yanan.framework.fieldhandler;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.yanan.framework.FieldHandler;
import com.yanan.framework.Plugin;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.Field;

public class ValueHandler implements FieldHandler<Value> {
    static {
        Plugin.register(Value.class,new ValueHandler());
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void process(Activity activity, Object instance,Field field, Value value) {
        Resources resource = activity.getResources();
        Class type = field.getType();
        Object result = getResource(type,resource,value.value(),field);
        Log.d(getClass().getSimpleName(),field+"==>"+result);
        if(result == null && value.required())
            throw new NullPointerException("resource "+value.value()+" not found");
        try {
            ReflectUtils.setFieldValue(field,instance,result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Object getResource(Class<?> clzz, Resources resources, int value,Field field) {
        if (clzz.equals(String.class))
            return resources.getString(value);
        if (clzz.equals(boolean.class) || clzz.equals(Boolean.class))
            return resources.getBoolean(value);
        if (clzz.equals(int.class) || clzz.equals(Integer.class))
            return resources.getInteger(value);
        if (clzz.equals(float.class) || clzz.equals(Float.class))
            return resources.getFloat(value);
        // 以上所有类型的数组类型
        if (clzz.equals(String[].class))
            return resources.getStringArray(value);
        if (clzz.equals(int[].class))
            return resources.getIntArray(value);
       return null;
    }
}
