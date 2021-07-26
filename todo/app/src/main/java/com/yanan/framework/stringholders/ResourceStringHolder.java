package com.yanan.framework.stringholders;

import android.content.res.Resources;
import android.view.ContextThemeWrapper;

import com.yanan.framework.Plugin;
import com.yanan.framework.StringHolder;
import com.yanan.framework.StringHolderProvider;
import com.yanan.todo.R;
import com.yanan.util.ReflectUtils;

public class ResourceStringHolder implements StringHolderProvider {
    static {
        StringHolder.register("resource",new ResourceStringHolder());
    }
    @Override
    public String getValue(String key, String attr,String args, String token) {
        ContextThemeWrapper contextThemeWrapper = (ContextThemeWrapper) Plugin.currentContext();
        Resources resource = contextThemeWrapper.getResources();
        return resource.getString(getResourceInt("id",key));
    }
    public int getResourceInt(String attr,String id){
        try {
            Object type = ReflectUtils.getFieldValue(attr,R.class);
            return ReflectUtils.getFieldValue(id,type);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("could get R field value ",e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("could get R field value ",e);
        }
    }

}
