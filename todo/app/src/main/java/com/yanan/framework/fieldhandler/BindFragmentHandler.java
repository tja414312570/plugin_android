package com.yanan.framework.fieldhandler;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.yanan.framework.FieldHandler;
import com.yanan.framework.Plugin;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.Field;

public class BindFragmentHandler implements FieldHandler<BindFragment> {
    static {
        Plugin.register(BindFragment.class,new BindFragmentHandler());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void process(Activity activity, Object instance, Field field,BindFragment contextView) {
        Object fragment = null;
        try {
            fragment = ReflectUtils.getFieldValue(field,instance);
            if(fragment != null){
                Log.d("TEST",fragment.toString());
                if(activity instanceof FragmentActivity){
                    if(!((androidx.fragment.app.Fragment) fragment).isAdded()) {
                        androidx.fragment.app.FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                        androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(contextView.value(), (androidx.fragment.app.Fragment) fragment);
                        if(field.getAnnotation(MainFragment.class) == null){
                            fragmentTransaction.hide((androidx.fragment.app.Fragment) fragment);
                        }
                        fragmentTransaction.commit();
                    }
                }else{
                    if(!((Fragment) fragment).isAdded()){
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(contextView.value(), (Fragment) fragment);
                        if(field.getAnnotation(MainFragment.class) == null){
                            fragmentTransaction.hide((Fragment) fragment);
                        }
                        fragmentTransaction.commit();
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
