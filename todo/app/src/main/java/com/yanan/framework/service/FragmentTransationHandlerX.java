package com.yanan.framework.service;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.yanan.framework.fieldhandler.ServiceHandler;


public class FragmentTransationHandlerX implements InstanceHandler<FragmentTransaction>{
    static {
        ServiceHandler.register(FragmentTransaction.class,new FragmentTransationHandlerX());
    }
    @Override
    public FragmentTransaction instance(Activity activity, Class<FragmentTransaction> instanceType) {
        return ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction();
    }
}
