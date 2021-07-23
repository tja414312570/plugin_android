package com.yanan.framework.service;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.yanan.framework.fieldhandler.ServiceHandler;

public class FragmentManagerHandlerX implements InstanceHandler<FragmentManager>{
    static {
        ServiceHandler.register(FragmentManager.class,new FragmentManagerHandlerX());
    }
    @Override
    public FragmentManager instance(Activity activity, Class<FragmentManager> instanceType) {
        return ((FragmentActivity)activity).getSupportFragmentManager();
    }
}
