package com.yanan.framework.fieldhandler;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.yanan.framework.FieldHandler;
import com.yanan.framework.Plugin;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.Field;

public class ViewsHandler implements FieldHandler<Views> {
    static ThreadLocal<View> viewThreadLocal = new ThreadLocal<>();
    static {
        Plugin.register(Views.class,new ViewsHandler());
    }
    public static void setViewContext(View view) {
        viewThreadLocal.set(view);
    }
    @Override
    public void process(Activity activity,Object instance, Field field, Views views) {
        View view = null;
        if(viewThreadLocal.get() != null){
            view = viewThreadLocal.get().findViewById(views.value());
        }
        if(view == null){
            view = activity.findViewById(views.value());
        }
        Log.d(getClass().getSimpleName(),field+"==>"+view);
        if(view == null && views.required())
            throw new NullPointerException("view "+views.value()+" not found");
        try {
            ReflectUtils.setFieldValue(field,instance,view);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static <T extends View> T getView(Activity activity,int resourceId){
        T view = null;
        if(resourceId == 0){
            view = (T) viewThreadLocal.get();
        }else{
            if(viewThreadLocal.get() != null){
                view = viewThreadLocal.get().findViewById(resourceId);
            }
            if(view == null){
                view = activity.findViewById(resourceId);
            }
        }
        return view;
    }
}
