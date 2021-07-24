package com.yanan.framework.event;

import android.app.Activity;
import android.view.View;

import com.yanan.framework.MethodHandler;
import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.ViewsHandler;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BindEventHandler implements MethodHandler<BindEvent> {
    static {
        Plugin.register(BindEvent.class,new BindEventHandler());
    }
    @Override
    public void process(Activity activity, Object instance, Method method,BindEvent annotation) {
        Object bindObject = null;
        if(annotation.view() != 0){
            bindObject = ViewsHandler.getView(activity,annotation.view());
        }else{
            try {
                bindObject = ReflectUtils.getFieldValue(annotation.field(),instance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        Method setListenerMethod = null;
            if(Object.class.equals(annotation.listener())){
                setListenerMethod = foundMethod(bindObject.getClass(),annotation.event());
            }else{
                try {
                    setListenerMethod = bindObject.getClass().getMethod(annotation.event(),annotation.listener());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    if(setListenerMethod == null){
                        setListenerMethod = bindObject.getClass().getMethod("set"+annotation.event(),annotation.listener());
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            if(setListenerMethod == null){
                throw new RuntimeException("could not found listener method for "+annotation);
            }
            Object listener = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    setListenerMethod.getParameterTypes(),
                    new BindEventProxy(activity,instance,bindObject,setListenerMethod,method,annotation));
            try {
                setListenerMethod.invoke(bindObject,listener);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

//
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        method.invoke(instance,view);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
    }

    private Method foundMethod(Class<?> aClass, String event) {
        String setMethod = "set"+event.substring(0,1).toUpperCase()+event.substring(1);
        Method[] methods = aClass.getMethods();
        for(Method method : methods){
            if(event.equals(method.getName()) || setMethod.equals(method.getName()))
                return method;
        }
        return null;
    }
}
