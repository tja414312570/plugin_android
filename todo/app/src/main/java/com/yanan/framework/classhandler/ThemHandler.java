package com.yanan.framework.classhandler;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.yanan.framework.ClassHandler;
import com.yanan.framework.Plugin;

public class ThemHandler implements ClassHandler<Theme> {
    static {
        Plugin.register(Theme.class,new ThemHandler());
    }
    @Override
    public void process(Activity activity,Object instance, Theme annotation) {
        activity.setTheme(annotation.value());
    }
}
