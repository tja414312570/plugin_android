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