package com.yanan.todo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.yanan.framework.Plugin;
import com.yanan.framework.classhandler.ContextView;
import com.yanan.framework.classhandler.NoActionBar;
import com.yanan.framework.event.BindEvent;
import com.yanan.framework.event.Click;
import com.yanan.framework.event.EventContext;
import com.yanan.framework.fieldhandler.BindFragment;
import com.yanan.framework.fieldhandler.MainFragment;
import com.yanan.framework.fieldhandler.Service;
import com.yanan.framework.fieldhandler.SqlLite;
import com.yanan.framework.fieldhandler.Value;
import com.yanan.framework.fieldhandler.Values;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.message.MessageBus;
import com.yanan.framework.methodhandler.AfterInjected;
import com.yanan.todo.ui.TestFragment;
import com.yanan.util.ReflectUtils;

@NoActionBar
@ContextView(R.layout.activity_form)
public class FormActivity extends AppCompatActivity {

    @Values("hello \\{{app_name}\\}") //获取资源数据
    private String app_names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Plugin.inject(this);
        EventContext.completedEvent();
        Toast.makeText(getApplication(),app_names,Toast.LENGTH_SHORT).show();

    }
    @Click(R.id.button)
    public void onButtonClick(View view){
        Toast.makeText(getApplication(),app_names+" 按钮点击",Toast.LENGTH_SHORT).show();
    }

}