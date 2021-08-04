package com.yanan.todo;

import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yanan.framework.Plugin;
import com.yanan.framework.classhandler.ContextView;
import com.yanan.framework.classhandler.NoActionBar;
import com.yanan.framework.event.BindEvent;
import com.yanan.framework.event.Click;
import com.yanan.framework.event.EventContext;
import com.yanan.framework.fieldhandler.BindAdapter;
import com.yanan.framework.fieldhandler.SQLite;
import com.yanan.framework.fieldhandler.Service;
import com.yanan.framework.fieldhandler.Values;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.form.FormContext;
import com.yanan.todo.dto.DemoDto;
import com.yanan.todo.ui.adapter.MainRecycleViewAdapter;
import com.yanan.util.xml.XMLHelper;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoActionBar
@ContextView(R.layout.activity_form)
public class FormActivity extends AppCompatActivity {

    private static final String TAG = "FORM_ACTIVITY";
    @Values("hello \\{{app_name}\\}") //获取资源数据
    private String app_names;
    @Views(R.id.text_form)
    private ViewGroup viewGroup;
    @Service
    private DemoDto demoDto;
    @Views(R.id.refreshLayout)
    private RefreshLayout refreshLayout;
    @Views(R.id.refreshLayout)
    private ViewGroup scrollView;
    @Views(R.id.recyclerView)
    private RecyclerView recyclerView;
    @BindAdapter(R.id.recyclerView)
    @Service
    private MainRecycleViewAdapter mainRecycleViewAdapter;
    private List<Map> resultMap ;
    @BindEvent(view = R.id.refreshLayout,event="OnRefreshListener")
    public void onRefresh(RefreshLayout refreshlayout){
        Log.d(TAG,"下拉刷新:"+refreshlayout);
        resultMap.clear();
        resultMap.addAll(demoDto.query());
        Log.d(TAG,"加载后的数据"+resultMap);
        mainRecycleViewAdapter.notifyDataSetChanged();
        refreshlayout.finishRefresh(true/*,false*/);//传入false表示刷新失败
    }
    @BindEvent(view = R.id.refreshLayout,event="OnLoadMoreListener")
    public void onLoadMore(RefreshLayout refreshlayout){
        Log.d(TAG,"上拉加载:"+refreshlayout);
        resultMap.clear();
        resultMap.addAll(demoDto.query());
        mainRecycleViewAdapter.notifyDataSetChanged();
        refreshlayout.finishLoadMore(true/*,false*/);//传入false表示刷新失败
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Plugin.inject(this);
        Toast.makeText(getApplication(),app_names,Toast.LENGTH_SHORT).show();
        refreshLayout.setRefreshHeader(new ClassicsHeader(getApplication()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getApplication()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        resultMap = demoDto.query();
        mainRecycleViewAdapter.setDemoList(resultMap);

    }
    @Click(R.id.button_delete)
    public void onDeleteClick(View view){
        demoDto.delete();
        refreshLayout.autoRefresh();
    }
    @Click(R.id.button)
    public void onButtonClick(View view) throws IOException, XmlPullParserException {

        FormContext formContext = FormContext.getFormContext(viewGroup);
        formContext = FormContext.getFormContext(R.id.text_form);
        Toast.makeText(getApplication()," 表单内容"+formContext.toString(),Toast.LENGTH_SHORT).show();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id", "test username");
        params.put("name", "test usex");

        List<Object> objectList = new ArrayList<>();
        objectList.add(params);
        params = new HashMap<String,Object>();
//        params.put("id", (int)System.currentTimeMillis());
        params.put("name", formContext.get(R.id.name));
        params.put("mobile", formContext.get(R.id.phone));
        params.put("action", formContext.get(R.id.action));
        params.put("note", formContext.get(R.id.note));
        objectList.add(params);
        demoDto.insert3(params);
        System.err.println(demoDto.query());
        refreshLayout.autoRefresh();
//        textView.setText(demoDto.query().toString());
    }
    @Override
    protected void onDestroy() {
        EventContext.completedEvent();
        super.onDestroy();
    }
}