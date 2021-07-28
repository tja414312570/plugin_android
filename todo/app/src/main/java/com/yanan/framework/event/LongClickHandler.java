package com.yanan.framework.event;

import android.app.Activity;
import android.view.View;

import com.yanan.framework.MethodHandler;
import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.ViewsHandler;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LongClickHandler implements MethodHandler<LongClick> {
    static {
        Plugin.register(LongClick.class,new LongClickHandler());
    }
    @Override
    public void process(Activity activity, Object instance, Method method,LongClick click) {
        View view = ViewsHandler.getView(activity,click.value());
        final Synchronized synchronised = method.getAnnotation(Synchronized.class);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    boolean required = synchronised != null ?  EventContext.require(Click.class,view) : false;
                    try {
                        if(required){
                            Object result =  ReflectUtils.invokeMethod(instance,method,view);
                            if(method.getReturnType().equals(boolean.class)||method.getReturnType().equals(Boolean.class))
                                return (boolean) result;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
    }
}
