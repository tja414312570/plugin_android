package com.yanan.framework.dto;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yanan.framework.dto.annotations.SQL;
import com.yanan.framework.dto.annotations.SQLs;
import com.yanan.framework.dto.annotations.Xml;
import com.yanan.framework.dto.mapper.DefaultSqlSessionExecuter;
import com.yanan.framework.dto.proxy.DtoProxy;
import com.yanan.framework.fieldhandler.SQLite;
import com.yanan.framework.fieldhandler.ServiceHandler;
import com.yanan.framework.service.InstanceHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
