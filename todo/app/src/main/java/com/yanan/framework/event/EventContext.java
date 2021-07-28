package com.yanan.framework.event;

import java.util.Stack;

public class EventContext {
    final static Stack<Integer> invokeEventStack = new Stack<>();
    static Integer hash(Class eventClass, Object eventSource){
        return eventClass.hashCode()+eventSource.hashCode()<<1;
    }
    static synchronized boolean require(Class eventClass, Object eventSource){
        Integer hash = hash(eventClass,eventSource);
        if(invokeEventStack.contains(hash))
            return false;
        invokeEventStack.push(hash);
        return true;
    };

    public static void completedEvent(){
        invokeEventStack.pop();
    }
    public static void completedEvent(Class eventClass, Object eventSource){
        Integer hash = hash(eventClass,eventSource);
        invokeEventStack.remove(hash);
    }
}
