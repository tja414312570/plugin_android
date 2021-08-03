package com.yanan.todo;

import android.content.Intent;
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
import com.yanan.framework.dto.DtoContext;
import com.yanan.framework.dto.SqlFragmentManager;
import com.yanan.framework.dto.entry.WrapperMapping;
import com.yanan.framework.dto.fragment.SqlFragment;
import com.yanan.framework.dto.mapper.PreparedSql;
import com.yanan.framework.event.BindEvent;
import com.yanan.framework.event.Synchronized;
import com.yanan.framework.fieldhandler.BindFragment;
import com.yanan.framework.fieldhandler.MainFragment;
import com.yanan.framework.fieldhandler.Values;
import com.yanan.framework.message.MessageBus;
import com.yanan.framework.methodhandler.AfterInjected;
import com.yanan.framework.classhandler.ContextView;
import com.yanan.framework.classhandler.NoActionBar;
import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.Service;
import com.yanan.framework.fieldhandler.Value;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.event.Click;
import com.yanan.todo.ui.TestFragment;
import com.yanan.util.ReflectUtils;
import com.yanan.util.xml.XMLHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;


@ContextView(R.layout.activity_main)
@NoActionBar
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";
    @Views(R.id.main_bottom_bar) //通过Plugin获取试图
    private BottomBarLayout mBottomBarLayout;
    @Service //通过Plugin获取Fragment对象
    private FragmentManager fragmentManager;
    @Service
    private FragmentTransaction fragmentTransaction;
    @Service //使用plugin生成一个fragment对象
    @BindFragment(R.id.fragment) //绑定到fragment
    private TestFragment testFragment;
    @MainFragment//表明当前Fragment为主Fragment
    @Service
    @BindFragment(R.id.fragment)
    private com.yanan.todo.ui.MainFragment homeFragment;
    @Value(R.string.app_name) //获取资源数据
    private String app_name;
    @Values("hello \\{{app_name}\\}") //获取资源数据
    private String app_names;
    private String stringHolderTest;
    @Service
    private Intent intent;
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Plugin.inject(this);
        SqlFragmentManager sqlFragment = DtoContext.getSqlFragmentManager(R.xml.test);
        System.err.println(sqlFragment);

//
//        SqlFragment sqlFragment1 = sqlFragment.getSqlFragment("TestSql.insert");
//        PreparedSql preparedSql = sqlFragment1.getPreparedSql(objectList);
//        System.err.println(preparedSql);
        Toast.makeText(getApplication(),app_names,Toast.LENGTH_LONG);
        Log.d(TAG,"软件名称:"+app_name+","+app_names);
    }
    @AfterInjected
    public void onInited(){
        Log.d("YA_NAN_PLUGIN","test method");
        System.err.println("YA_NAN");
    }
    @Synchronized("请勿重复点击")
    @Click(R.id.fab)
    public void onFloatClick(View view){
        if(toast != null)
            toast.cancel();
        toast = Toast.makeText(getApplicationContext(),"浮动按钮点击",Toast.LENGTH_SHORT);
        toast.show();
//        EventContext.completedEvent();
        startActivity(new Intent(this,FormActivity.class));
    }
    @Click(R.id.fragment)
    public void onItemSelected(FrameLayout view){
        toast = Toast.makeText(getApplicationContext(),"fragment 被点击",Toast.LENGTH_SHORT);
        toast.show();
    }
    @BindEvent(view = R.id.main_bottom_bar,event = "OnItemSelectedListener")
    public void onItemSelected(BottomBarItem bottomBarItem, int previousPosition, int currentPosition) {
        try {
            if(toast != null)
                toast.cancel();String title = (String) ReflectUtils.getDeclaredFieldValue("title",bottomBarItem);
            toast = Toast.makeText(getApplicationContext(),title,Toast.LENGTH_SHORT);
            toast.show();
            if(currentPosition == 0){
                fragmentManager.beginTransaction().hide(testFragment)
                        .show(homeFragment)
                        .commit();
            }else{
                MessageBus.publish("TITLE",title);
                fragmentManager.beginTransaction().hide(homeFragment).show(testFragment).commit();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

//
}