package com.yanan.framework.fieldhandler;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.yanan.framework.FieldHandler;
import com.yanan.framework.Plugin;
import com.yanan.util.ExtReflectUtils;
import com.yanan.util.ParameterUtils;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BindApaterHandler implements FieldHandler<BindAdapter> {
    static {
        Plugin.register(BindAdapter.class,new BindApaterHandler());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void process(Activity activity, Object instance, Field field,BindAdapter contextView) {
        Object fragment = null;
        try {
            fragment = ReflectUtils.getFieldValue(field,instance);
            View view = ViewsHandler.getView(activity,contextView.value());
            Method setAdapther = getMethod(view.getClass(),"setAdapter",fragment.getClass());
            if(setAdapther != null){
                ReflectUtils.invokeMethod(view,setAdapther,fragment);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Method getMethod(Class clzz, String setAdapter, Class<?> aClass) {
        Method[] methods = clzz.getMethods();
        for(Method method : methods){
            if(method.getName().equals(setAdapter)){

                if(ExtReflectUtils.isEffectiveType(method.getParameterTypes()[0],aClass)){
                    return method;
                }
            }
        }
        return null;
    }
}
