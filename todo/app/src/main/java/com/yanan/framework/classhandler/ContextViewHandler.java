package com.yanan.framework.classhandler;

import android.app.Activity;

import com.yanan.framework.ClassHandler;
import com.yanan.framework.Plugin;

public class ContextViewHandler implements ClassHandler<ContextView> {
    static {
        Plugin.register(ContextView.class,new ContextViewHandler());
    }
    @Override
    public void process(Activity activity, Object instance,ContextView contextView) {
//        if(instance instanceof Activity){
            activity.setContentView(contextView.value());
//        }else if(instance instanceof Fragment){
//            Fragment fragment = (Fragment) instance;
//            fragment.getLayoutInflater().inflate(contextView.value(),fragment.getContext(),false);
//            View view = inflater.inflate(R.layout.fragment_main,container,false);
//            ViewsHandler.setViewContext(view);
//        }

    }
}
