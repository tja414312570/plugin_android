package com.yanan.framework.message;

import android.util.Log;

import com.yanan.util.LockSupports;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.LockSupport;

/**
 * 消息总线
 */
public class MessageBus implements Runnable{
    static boolean isAsyncRun;
    static ThreadLocal<Object> rejectMessageHandler = new ThreadLocal<>();
    static ThreadLocal<MessageHandler> currentMessageHandler = new ThreadLocal<>();
    static Map<Object, List<MessageHandler<Object>>> messageHandlerMap = new ConcurrentHashMap<>();
    static List<AsyncMessage> asyncMessagesList = new CopyOnWriteArrayList<>();
    static Thread thread;
    public static <T> void bindMessage(Object top,MessageHandler<T> messageHandler){
        List<MessageHandler<Object>> messageHandlerList = messageHandlerMap.get(top);
        if(messageHandlerList == null){
            synchronized (top){
                if(messageHandlerList == null){
                    messageHandlerList = new CopyOnWriteArrayList<>();
                    messageHandlerMap.put(top,messageHandlerList);
                }
            }
        }
        if(messageHandlerList.indexOf(messageHandler) == -1){
            messageHandlerList.add((MessageHandler<Object>) messageHandler);
            LockSupports.unLock(top);
        }
    }
    static synchronized void checkThread(){
        if(thread == null){
            isAsyncRun = true;
            thread = new Thread(new MessageBus());
            thread.setDaemon(true);
            thread.setName("Message Bus daemon thread");
            thread.start();
        }
        LockSupport.unpark(thread);
    }
    public synchronized static <T> void publishAsync(String top,T message) {
        publishAsync(top,message,true);
    }
    public synchronized static <T> void publishAsync(String top,T message,boolean ignore){
        checkThread();
        asyncMessagesList.add(new AsyncMessage(top,message,ignore));
    }

    public static <T> void publish(String top,T message){
        publish(top,message,true);
    }
    public static <T> void publish(String top,T message,boolean ignore){
        List<MessageHandler<Object>> messageHandlerList = messageHandlerMap.get(top);
        while(!ignore && (messageHandlerList == null || messageHandlerList.size() == 0)){
            LockSupports.lock(top);
        }
       for(MessageHandler messageHandler : messageHandlerList){
           try{
               currentMessageHandler.set(messageHandler);
               messageHandler.onMessage(message);
               while(messageHandler.equals(rejectMessageHandler.get())){
                   messageHandler.onMessage(message);
               }
           }finally {
               rejectMessageHandler.remove();
               currentMessageHandler.remove();
           }
       }
    }
    public static void reject() {
        rejectMessageHandler.set(currentMessageHandler.get());
    }

    @Override
    public void run() {
        while(isAsyncRun){
            if(asyncMessagesList.size() == 0) {
                LockSupports.lock(thread);
            }
            Log.d("MESSAGE_BUS",asyncMessagesList.toString());
           Iterator<AsyncMessage> iterator = asyncMessagesList.iterator();
           while(iterator.hasNext()){
               AsyncMessage asyncMessage = iterator.next();
               boolean ignore = asyncMessage.isIgnore();
               Object top = asyncMessage.getTop();
               Object message = asyncMessage.getMessage();
               List<MessageHandler<Object>> messageHandlerList = messageHandlerMap.get(top);
               if(!ignore && (messageHandlerList == null || messageHandlerList.size() == 0))continue;
               boolean removable = true;
               for(MessageHandler messageHandler : messageHandlerList)
                   try{
                       messageHandler.onMessage(message);
                       if(messageHandler.equals(rejectMessageHandler.get())){
                           removable = false;
                           continue;
                       }
                   }finally {
                       rejectMessageHandler.remove();
                   }
               if(removable){
                   asyncMessagesList.remove(asyncMessage);
               }
           }
        }
    }
}
