# plugin_android
为android的plugin框架提供注解开发支持

# 开发与测试阶段 
## 特性
# 高度扩展
# 注解开发
# 轻量化
## 案例
```java
package com.yanan.todo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.yanan.framework.event.BindEvent;
import com.yanan.framework.fieldhandler.BindFragment;
import com.yanan.framework.message.MessageBus;
import com.yanan.framework.methodhandler.AfterInjected;
import com.yanan.framework.classhandler.ContextView;
import com.yanan.framework.classhandler.NoActionBar;
import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.Service;
import com.yanan.framework.fieldhandler.Value;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.event.Click;
import com.yanan.todo.ui.MainFragment;
import com.yanan.todo.ui.TestFragment;
import com.yanan.util.ReflectUtils;

@ContextView(R.layout.activity_main)
@NoActionBar
public class MainActivity extends AppCompatActivity implements BottomBarLayout.OnItemSelectedListener {
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
    @Service
    @BindFragment(R.id.fragment)
    private MainFragment homeFragment;
    @Value(R.string.app_name) //获取资源数据
    private String app_name;
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Plugin.inject(this);
        Log.d(TAG,"软件名称:"+app_name);
    }
    @AfterInjected
    public void onInited(){
        Log.d("YA_NAN_PLUGIN","test method");
        System.err.println("YA_NAN");
    }
    @SuppressLint("WrongConstant")
    @Click(R.id.fragment)
    public void onItemSelected(FrameLayout view){
        toast = Toast.makeText(getApplicationContext(),"fragment 被点击",1000);
        toast.show();
    }
    @BindEvent(view = R.id.main_bottom_bar,event = "OnItemSelectedListener")
    @SuppressLint("WrongConstant")
    @Override
    public void onItemSelected(BottomBarItem bottomBarItem, int previousPosition, int currentPosition) {
        try {
            if(toast != null)
                toast.cancel();String title = (String) ReflectUtils.getDeclaredFieldValue("title",bottomBarItem);
           final int timeout = 1000;
            toast = Toast.makeText(getApplicationContext(),title,timeout);

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
```

```java
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
        textView.setText("来自于消息:"+title);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
```

```java
package com.yanan.todo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.MapView;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yanan.todo.R;
import com.yanan.framework.Plugin;
import com.yanan.framework.event.BindEvent;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.fieldhandler.ViewsHandler;

public class MainFragment extends Fragment {
    private static final String TAG = "MAIN_FRAGMENT";
    @Views(R.id.map)
    MapView mMapView;
    private View view;
    @Views(R.id.refreshLayout)
    private RefreshLayout refreshLayout;

    @BindEvent(view = R.id.refreshLayout,event="OnRefreshListener")
    public void onRefresh(RefreshLayout refreshlayout){
        Log.d(TAG,"下拉刷新:"+refreshlayout);
        refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
    }
    @BindEvent(view = R.id.refreshLayout,event="OnLoadMoreListener")
    public void onLoadMore(RefreshLayout refreshlayout){
        Log.d(TAG,"上拉加载:"+refreshlayout);
        refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示刷新失败
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main,container,false);
        ViewsHandler.setViewContext(view);
        Plugin.inject(this);
        mMapView.onCreate(savedInstanceState);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//            }
//        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
//            }
//        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
```
