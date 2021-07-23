package com.yanan.framework.event;

import android.app.Activity;
import android.view.View;

import com.yanan.framework.MethodHandler;
import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.ViewsHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClickHandler implements MethodHandler<Click> {
    static {
        Plugin.register(Click.class,new ClickHandler());
    }
    @Override
    public void process(Activity activity, Object instance, Method method,Click click) {
            View view = ViewsHandler.getView(activity,click.value());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        method.invoke(instance,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
