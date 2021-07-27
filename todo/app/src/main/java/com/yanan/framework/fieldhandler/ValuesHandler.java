package com.yanan.framework.fieldhandler;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.yanan.framework.FieldHandler;
import com.yanan.framework.Plugin;
import com.yanan.framework.StringHolder;
import com.yanan.util.ParameterUtils;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.Field;

public class ValuesHandler implements FieldHandler<Values> {
    static {
        Plugin.register(Values.class,new ValuesHandler());
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void process(Activity activity, Object instance,Field field, Values value) {
        Resources resource = activity.getResources();
        Class type = field.getType();
        Object result = StringHolder.decodeString(value.value());
        result = ParameterUtils.castType(result,type);
        Log.d(getClass().getSimpleName(),field+"==>"+result);
        if(result == null && value.required())
            throw new NullPointerException("resource "+value.value()+" not found");
        try {
            ReflectUtils.setFieldValue(field,instance,result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
