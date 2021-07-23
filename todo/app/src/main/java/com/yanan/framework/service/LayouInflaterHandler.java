package com.yanan.framework.service;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import com.yanan.framework.fieldhandler.ServiceHandler;

public class LayouInflaterHandler implements InstanceHandler<LayoutInflater>{
    static {
        ServiceHandler.register(LayoutInflater.class,new LayouInflaterHandler());
    }
    @Override
    public LayoutInflater instance(Activity activity, Class<LayoutInflater> instanceType) {
        return activity.getLayoutInflater();
    }
}
