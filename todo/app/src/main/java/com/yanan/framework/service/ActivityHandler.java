package com.yanan.framework.service;

import android.app.Activity;

import com.yanan.framework.fieldhandler.ServiceHandler;

public class ActivityHandler implements InstanceHandler<Activity>{
    static {
        ServiceHandler.register(Activity.class,new ActivityHandler());
    }
    @Override
    public Activity instance(Activity activity, Class<Activity> instanceType) {
        return activity;
    }
}
