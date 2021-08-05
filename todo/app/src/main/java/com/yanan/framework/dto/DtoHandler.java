package com.yanan.framework.dto;

import android.app.Activity;

import com.yanan.framework.fieldhandler.SQLite;
import com.yanan.framework.fieldhandler.ServiceHandler;
import com.yanan.framework.service.InstanceHandler;

import java.util.HashMap;
import java.util.Map;

public class DtoHandler<T> implements InstanceHandler<T> {
    private Map<Class<?>,SQLite> sqLiteMap = new HashMap<>();
    static {
        ServiceHandler.registerAnnotations(SQLite.class,new DtoHandler());
    }
    @Override
    public T instance(Activity activity, Class<T> instanceType) {
        return DtoContext.createDtoProxyInstance(activity, instanceType);
    }
}
