package com.yanan.todo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.yanan.todo.R;
import com.yanan.framework.message.Message;
import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.fieldhandler.ViewsHandler;
import com.yanan.framework.message.UseHandler;

public class TestFragment extends Fragment {

    @Views(R.id.text_home)
    private  TextView textView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        //fragment中如果要使用fragment里的view 需要先设置当前fragment的view上下文
        ViewsHandler.setViewContext(view);
        //使用Plugin注入服务
        Plugin.inject(this);
        return view;
    }
    //因为要更新UI，所以使用Handler
    @UseHandler
    @Message("TITLE")
    public void onTitleChange(String title){
        textView.setText("来自消息:"+title);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}