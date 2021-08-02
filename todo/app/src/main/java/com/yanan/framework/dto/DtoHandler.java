package com.yanan.framework.dto;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yanan.framework.dto.annotations.Table;
import com.yanan.framework.dto.annotations.Xml;
import com.yanan.framework.dto.fragment.SqlFragment;
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
        SQLite sqLite = instanceType.getAnnotation(SQLite.class);
        Log.d("DTO_HANDLER",instanceType.getName()+sqLite.toString());
        SQLiteDatabase sqLiteDatabase = activity.openOrCreateDatabase(sqLite.value(),sqLite.mode(),null);
        Xml xml = instanceType.getAnnotation(Xml.class);
        if(xml != null){
            SqlFragmentManager sqlFragment = DtoContext.getSqlFragmentManager(xml.value());
        }
        return (T) sqLiteDatabase;
    }
}
