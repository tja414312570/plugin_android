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
        SQLite sqLite = instanceType.getAnnotation(SQLite.class);
        Log.d("DTO_HANDLER",instanceType.getName()+sqLite.toString());
        Object instance = null;
        SQLiteDatabase sqLiteDatabase = activity.openOrCreateDatabase(sqLite.value(),sqLite.mode(),null);
        Xml xml = instanceType.getAnnotation(Xml.class);
        if(xml != null) {
            SqlFragmentManager sqlFragmentManager = DtoContext.getSqlFragmentManager(xml.value());
        }

        SqlFragmentManager sqlFragmentManager = DtoContext.getSqlFragmentManager(instanceType);
        SqlSession sqlSession = new DefaultSqlSessionExecuter(sqLiteDatabase,sqlFragmentManager);
        InvocationHandler proxy = new DtoProxy(activity,instanceType,sqlFragmentManager,sqlSession);
        instance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{instanceType},proxy);
        return (T) instance;
    }
}
