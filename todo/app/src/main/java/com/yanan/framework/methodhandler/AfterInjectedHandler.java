package com.yanan.framework.methodhandler;

import android.app.Activity;

import com.yanan.framework.MethodHandler;
import com.yanan.framework.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AfterInjectedHandler implements MethodHandler<AfterInjected> {
    static {
        Plugin.register(AfterInjected.class,new AfterInjectedHandler());
    }
    @Override
    public void process(Activity activity, Object instance, Method method,AfterInjected contextView) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
