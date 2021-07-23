package com.yanan.framework.service;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;

import com.yanan.framework.fieldhandler.ServiceHandler;

public class ContextHandler implements InstanceHandler<Context>{
    static {
        ServiceHandler.register(Context.class,new ContextHandler());
    }
    @Override
    public Context instance(Activity activity, Class<Context> instanceType) {
        return activity.getApplicationContext();
    }
}
