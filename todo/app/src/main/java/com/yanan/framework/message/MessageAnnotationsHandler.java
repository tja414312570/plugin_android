package com.yanan.framework.message;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.yanan.framework.MethodHandler;
import com.yanan.framework.Plugin;
import com.yanan.util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MessageAnnotationsHandler implements MethodHandler<Message> {
    static {
        Plugin.register(Message.class,new MessageAnnotationsHandler());
    }
    @Override
    public void process(Activity activity, Object instance, Method method,Message anno) {
        if(method.getAnnotation(UseHandler.class) != null){
            Handler handler = new Handler(){
                @Override
                public void handleMessage(@NonNull android.os.Message msg) {
                    try {
                        ReflectUtils.invokeMethod(instance,method,msg.obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            };
            MessageBus.bindMessage(anno.value(), new MessageHandler<Object>() {
                @Override
                public void onMessage(Object message) {
                    android.os.Message msg = new android.os.Message();
                    msg.obj = message;
                    handler.sendMessage(msg);
                }
            });
        }else{
            MessageBus.bindMessage(anno.value(), new MessageHandler<Object>() {
                @Override
                public void onMessage(Object message) {
                    try {
                        ReflectUtils.invokeMethod(instance,method,message);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
}
