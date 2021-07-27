package com.yanan.framework.stringholders;

import android.content.res.Resources;
import android.view.ContextThemeWrapper;

import com.yanan.framework.Plugin;
import com.yanan.framework.RResourceHelper;
import com.yanan.framework.StringHolder;
import com.yanan.framework.StringHolderProvider;
import com.yanan.util.StringUtil;

public class ResourceStringHolder implements StringHolderProvider {
    static {
        StringHolder.register("resource",new ResourceStringHolder());
    }
    @Override
    public String getValue(String key, String attr,String args, String token) {
        ContextThemeWrapper contextThemeWrapper = (ContextThemeWrapper) Plugin.currentContext();
        Resources resource = contextThemeWrapper.getResources();
        if(StringUtil.isEmpty(args))
            args = "string";
        return resource.getString(RResourceHelper.getResourceId(args,key));
    }

}
