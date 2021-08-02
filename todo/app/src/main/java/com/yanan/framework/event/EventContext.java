package com.yanan.framework.event;

import android.app.Activity;
import android.widget.Toast;

import com.yanan.util.HashMaps;
import com.yanan.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Stack;

public class EventContext {
    final static Stack<Integer> invokeEventStack = new Stack<>();
    final static Map<Method,Synchronized> eventSynchronizedMap = new HashMaps<>();
    static Integer hash(Class eventClass, Object eventSource){
        return eventClass.hashCode()+eventSource.hashCode()<<1;
    }
    static synchronized boolean require(Activity activity, Object instance, Method method, Annotation event, Object eventSource){
        Synchronized synchronised = getSynchronized(method);
        if(synchronised == null)
            return true;
        Integer hash = hash(event.annotationType(),eventSource);
        synchronized (hash){
            if(invokeEventStack.contains(hash)){
                if(!synchronised.value().isEmpty())
                    Toast.makeText(activity.getApplicationContext(),synchronised.value(),Toast.LENGTH_SHORT).show();
                if(!synchronised.callback().isEmpty()) {
                    try {
                        ReflectUtils.invokeMethod(instance,synchronised.callback());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
            invokeEventStack.push(hash);
            return true;
        }
    }

    private static Synchronized getSynchronized(Method method) {
        synchronized (method){
            Synchronized sync = eventSynchronizedMap.get(method);
            if(sync == null && eventSynchronizedMap.containsKey(method))
                return null;
            sync = method.getAnnotation(Synchronized.class);
            eventSynchronizedMap.put(method,sync);
            return sync;
        }
    }

    ;

    public static void completedEvent(){
        invokeEventStack.pop();
    }
    public static void completedEvent(Class eventClass, Object eventSource){
        Integer hash = hash(eventClass,eventSource);
        invokeEventStack.remove(hash);
    }
}
