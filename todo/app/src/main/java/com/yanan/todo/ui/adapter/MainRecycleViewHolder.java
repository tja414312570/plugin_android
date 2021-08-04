package com.yanan.todo.ui.adapter;

import android.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.yanan.framework.Plugin;
import com.yanan.framework.classhandler.ContextView;
import com.yanan.framework.event.Click;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.fieldhandler.ViewsHandler;
import com.yanan.todo.R;
import com.yanan.util.StringUtil;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Map;

public class MainRecycleViewHolder extends RecyclerView.ViewHolder {
    @Views(R.id.item_activity_from_name)
    private TextView nameView;
    @Views(R.id.item_activity_from_phone)
    private TextView phoneView;
    @Views(R.id.item_activity_from_action)
    private TextView actionView;
    @Views(R.id.item_activity_from_note)
    private TextView noteView;
    @Views(R.id.data_view)
    private View dataView;
    @Views(R.id.empty_view)
    private View emptyView;
    @Views(R.id.item_activity_from_id)
    private TextView idView;
    @Views(0)
    private ConstraintLayout contextView;
    public MainRecycleViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        ViewsHandler.setViewContext(itemView);
        Plugin.inject(this);
    }
    @Click(0)
    public void onContextClick(View textView){
        Log.d("MAIN_VIEW_HOLDER",textView.toString());
        Toast.makeText(Plugin.currentContext(),"上下文View点击",Toast.LENGTH_SHORT).show();
    }
    @Click(R.id.item_activity_from_name)
    public void onItemClick(TextView textView){
        Log.d("MAIN_VIEW_HOLDER",textView.getText()+"==>"+textView.toString());
        Toast.makeText(Plugin.currentContext(),"text view 点击",Toast.LENGTH_SHORT).show();
    }

    public void draw(Map map) {
        idView.setText(get(map.get("id")));
        nameView.setText(get(map.get("name")));
        phoneView.setText(get(map.get("mobile")));
        actionView.setText(get(map.get("action")));
        noteView.setText(get(map.get("note")));
        emptyView.setVisibility(View.GONE);
        dataView.setVisibility(View.VISIBLE);
        contextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT) );
    }

    private String get(Object value) {
        if(value == null || StringUtil.isEmpty(value+"" ))
            return "-";
        return  value+"";
    }

    public void empty() {
        emptyView.setVisibility(View.VISIBLE);
        dataView.setVisibility(View.GONE);
        contextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT) );
    }

    public void title() {
        idView.setText("ID");
        nameView.setText("姓名");
        phoneView.setText("电话");
        actionView.setText("动作");
        noteView.setText("备注");
    }
}
