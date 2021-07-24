package com.yanan.todo.ui.adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yanan.framework.Plugin;
import com.yanan.framework.classhandler.ContextView;
import com.yanan.framework.event.Click;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.fieldhandler.ViewsHandler;
import com.yanan.todo.R;

import org.jetbrains.annotations.NotNull;

public class MainRecycleViewHolder extends RecyclerView.ViewHolder {
    @Views(R.id.empty_text)
    private TextView textView;
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
    @Click(R.id.empty_text)
    public void onItemClick(TextView textView){
        Log.d("MAIN_VIEW_HOLDER",textView.getText()+"==>"+textView.toString());
        Toast.makeText(Plugin.currentContext(),"text view 点击",Toast.LENGTH_SHORT).show();
    }
}
