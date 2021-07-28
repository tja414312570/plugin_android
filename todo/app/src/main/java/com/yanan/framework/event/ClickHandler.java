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
            final Synchronized synchronised = method.getAnnotation(Synchronized.class);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean required = synchronised != null ?  EventContext.require(Click.class,view) : false;
                    try {
                        if(required)
                            method.invoke(instance,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }finally {
//                        if(required)
//                            EventContext.completedFilter(Click.class,view);
                    }
                }
            });
    }
}
