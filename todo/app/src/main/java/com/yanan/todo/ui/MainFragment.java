package com.yanan.todo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yanan.framework.fieldhandler.BindAdapter;
import com.yanan.framework.fieldhandler.Service;
import com.yanan.framework.methodhandler.AfterInjected;
import com.yanan.todo.R;
import com.yanan.framework.Plugin;
import com.yanan.framework.event.BindEvent;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.fieldhandler.ViewsHandler;
import com.yanan.todo.ui.adapter.MainRecycleViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFragment extends Fragment {
    private static final String TAG = "MAIN_FRAGMENT";
    @Views(R.id.map)
    MapView mMapView;
    private View view;
    @Views(R.id.refreshLayout)
    private RefreshLayout refreshLayout;
    @Views(R.id.recyclerView)
    private RecyclerView recyclerView;
    @BindAdapter(R.id.recyclerView)
    @Service
    private MainRecycleViewAdapter mainRecycleViewAdapter;
    private AMap aMap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationClientOption;
    private boolean isFirstLoc = true;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    @AfterInjected
    public void initMap(){
        aMap = mMapView.getMap();
        UiSettings settings = aMap.getUiSettings();
        mLocationClient = new AMapLocationClient(getContext());
        //设置定位回调监听
//        mLocationClient.setLocationListener(this);
        //初始化参数定位
        mLocationClientOption = new AMapLocationClientOption();
        //设置定位模式
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息，默认返回
        mLocationClientOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认强制
        mLocationClientOption.setWifiScan(true);
        //设置定位间隔  ms
        mLocationClientOption.setInterval(2000);


        //给定客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationClientOption);
        //启动定位
        mLocationClient.startLocation();

        //设置定位监听
//        aMap.setLocationSource(this);
        //是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        //是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        //设置定位点的图标  默认为蓝色小点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        aMap.setMyLocationStyle(myLocationStyle);
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
    @BindEvent(view = 0, field = "mLocationClient", event = "LocationListener")
    public void onLocationChanged(AMapLocation aMapLocation) {
        Toast.makeText(getContext(),"定位完成:",Toast.LENGTH_LONG);
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功 回调信息 设置相关信息
                aMapLocation.getLocationType();//获取定位结果来源
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精确信息

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);

                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

//                Toast.makeText(getContext(), "定位成功"+aMapLocation.getAddress(), Toast.LENGTH_LONG).show();
                if (isFirstLoc) {
                    //缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                            new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心点移动到定位点
//                    mListener.onLocationChanged(aMapLocation);

                    isFirstLoc = false;

                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

                Toast.makeText(getContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
}