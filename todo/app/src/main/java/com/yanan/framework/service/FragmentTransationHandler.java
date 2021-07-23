package com.yanan.framework.service;

import android.app.Activity;
import android.app.FragmentTransaction;

import com.yanan.framework.fieldhandler.ServiceHandler;


public class FragmentTransationHandler implements InstanceHandler<FragmentTransaction>{
    static {
        ServiceHandler.register(FragmentTransaction.class,new FragmentTransationHandler());
    }
    @Override
    public FragmentTransaction instance(Activity activity, Class<FragmentTransaction> instanceType) {
        return activity.getFragmentManager().beginTransaction();
    }
}
