package com.yanan.framework.form;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.yanan.framework.Plugin;
import com.yanan.framework.RResourceHelper;
import com.yanan.framework.fieldhandler.ViewsHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormContext {
    private static Map<Class<?>,FormHolder> formHolderMap = new HashMap<>();
    public static <T> void register(Class<? extends T> formClass,FormHolder<T,?> formHolder){
        formHolderMap.put(formClass,formHolder);
    }
    public Map<Integer,FormItem> formItemMap = new LinkedHashMap<>();

    public static FormContext getFormContext(Activity activity,int id) {
        ViewGroup viewGroup = (ViewGroup) ViewsHandler.getView(activity,id);
        return getFormContext(viewGroup);
    }
    public static FormContext getFormContext(int id) {
        return getFormContext((Activity) Plugin.currentContext(),id);
    }
    public static FormContext getFormContext(ViewGroup viewGroup){
        FormContext formContext = new FormContext();
        findForm(formContext,viewGroup);
        return formContext;
    }
    public <T> T get(Integer id){
        FormItem formItem = formItemMap.get(id);
        return (T) formItem.getFormHolder().get(formItem.getView());
    }
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        Iterator<FormItem> iterator = formItemMap.values().iterator();
        while(iterator.hasNext()){
            FormItem formItem = iterator.next();
            map.put(RResourceHelper.getResourceName("id",formItem.getView().getId()),formItem.getFormHolder().get(formItem.getView()));
        }
        return map;
    }
    public String toString(){
        return new Gson().toJson(toMap());
    }
    private static void findForm(FormContext formContext, ViewGroup viewGroup) {
        for(int i = 0;i<viewGroup.getChildCount();i++){
            View view = viewGroup.getChildAt(i);
            if(view instanceof ViewGroup){
                findForm(formContext, (ViewGroup) view);
                continue;
            }
            Class<?> viewClass = view.getClass();
            FormHolder formHolder = formHolderMap.get(viewClass);
            if(formHolder != null){
                FormItem formItem = new FormItem();
                formItem.setView(view);
                formItem.setFormHolder(formHolder);
                formContext.formItemMap.put(view.getId(),formItem);
            }
        }
    }
}
