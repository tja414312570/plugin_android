package com.yanan.framework.event;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BindEventProxy implements InvocationHandler {
    private final Method setListenerMethod;
    private Activity activity;
    private Method listenerMethod;
    private Method bindMethod;
    private BindEvent bindEvent;
    private Object instance;
    private View view;
    private Class<?> listenerClass[];

    public Activity getActivity() {
        return activity;
    }

    public Method getListenerMethod() {
        return listenerMethod;
    }

    public BindEventProxy(Activity activity,Object instance,View view,  Method setListenerMethod, Method bindMethod, BindEvent bindEvent) {
        this.activity = activity;
        this.setListenerMethod = setListenerMethod;
        this.listenerClass = this.setListenerMethod.getParameterTypes();
        this.bindMethod = bindMethod;
        this.listenerMethod = this.listenerClass[0].getMethods()[0];
        this.bindEvent = bindEvent;
        this.instance = instance;
        this.view = view;
    }

    public Method getBindMethod() {
        return bindMethod;
    }

    public BindEvent getBindEvent() {
        return bindEvent;
    }

    public Object getInstance() {
        return instance;
    }

    public View getView() {
        return view;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d("BIND_EVENT_PROXY",method+ Arrays.toString(args));
        if(method.getName().equals(listenerMethod.getName())){
            return bindMethod.invoke(instance,args);
        }
        return method.invoke(this);
    }
}
