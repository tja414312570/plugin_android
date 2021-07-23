package com.yanan.framework.classhandler;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.yanan.framework.ClassHandler;
import com.yanan.framework.Plugin;

public class NoActionBarHandler implements ClassHandler<NoActionBar> {
    static {
        Plugin.register(NoActionBar.class,new NoActionBarHandler());
    }
    @Override
    public void process(Activity activity,Object instance, NoActionBar annotation) {
        if(activity instanceof AppCompatActivity){
            ActionBar actionBar = ((AppCompatActivity)activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
//        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
