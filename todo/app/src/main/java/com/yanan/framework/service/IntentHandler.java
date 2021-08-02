package com.yanan.framework.service;

import android.app.Activity;
import android.content.Intent;

import com.yanan.framework.fieldhandler.ServiceHandler;

public class IntentHandler implements InstanceHandler<Intent>{
    static {
        ServiceHandler.register(Intent.class,new IntentHandler());
    }
    @Override
    public Intent instance(Activity activity, Class<Intent> instanceType) {
        return activity.getIntent();
    }
}
