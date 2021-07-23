package com.yanan.framework.service;

import android.app.Activity;
import android.app.FragmentManager;

import com.yanan.framework.fieldhandler.ServiceHandler;

public class FragmentManagerHandler implements InstanceHandler<FragmentManager>{
    static {
        ServiceHandler.register(FragmentManager.class,new FragmentManagerHandler());
    }
    @Override
    public FragmentManager instance(Activity activity, Class<FragmentManager> instanceType) {
        return activity.getFragmentManager();
    }
}
