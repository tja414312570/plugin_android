package com.yanan.framework.dto.stringsholder;

import com.yanan.framework.StringHolderProvider;
import com.yanan.framework.StringHolder;
import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.SqlFragmentManager;
import com.yanan.framework.dto.entry.BaseMapping;
import com.yanan.framework.dto.fragment.SqlFragment;

public class SqlStringHolder implements StringHolderProvider {
    static {
        StringHolder.register("sql",new SqlStringHolder());
    }
    @Override
    public String getValue(String key, String attr,String args, String token) {
        if(!"sql".equals(attr))
            return null;
        String namespace = key.substring(0,key.lastIndexOf("."));
        SqlFragmentManager sqlFragmentManager = DtoContext.getSqlFragmentManager(namespace);
        if(sqlFragmentManager != null){
            SqlFragment baseMapping = sqlFragmentManager.getSqlFragment(key);
            if(baseMapping != null)
                return baseMapping.getBaseMapping().getXml();
        }
        throw new RuntimeException("could not found sql fragment "+key);
    }
}
