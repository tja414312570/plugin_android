package com.yanan.framework.event;

import android.app.Activity;
import android.view.DragEvent;
import android.view.View;

import com.yanan.framework.MethodHandler;
import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.ViewsHandler;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DragHandler implements MethodHandler<Drag> {
    static {
        Plugin.register(Drag.class,new DragHandler());
    }
    @Override
    public void process(Activity activity, Object instance, Method method,Drag click) {
            View view = ViewsHandler.getView(activity,click.value());
            final Synchronized synchronised = method.getAnnotation(Synchronized.class);
            view.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    boolean required = synchronised != null ?  EventContext.require(Click.class,view) : false;
                    try {
                        if(required){
                            Object result =  ReflectUtils.invokeMethod(instance,method,view,event);
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
