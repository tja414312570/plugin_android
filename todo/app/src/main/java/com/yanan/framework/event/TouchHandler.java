package com.yanan.framework.event;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.yanan.framework.MethodHandler;
import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.ViewsHandler;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TouchHandler implements MethodHandler<Touch> {
    static {
        Plugin.register(Touch.class,new TouchHandler());
    }
    @Override
    public void process(Activity activity, Object instance, Method method,Touch touch) {
        View view = ViewsHandler.getView(activity,touch.value());
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        if(EventContext.require(activity,instance,method,touch,v)){
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
