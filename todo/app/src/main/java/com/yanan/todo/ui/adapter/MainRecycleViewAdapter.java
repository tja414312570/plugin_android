package com.yanan.todo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.AutoInject;
import com.yanan.framework.fieldhandler.Service;
import com.yanan.framework.fieldhandler.ViewsHandler;
import com.yanan.todo.R;
import com.yanan.todo.dto.DemoDto;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@AutoInject
public class MainRecycleViewAdapter extends RecyclerView.Adapter<MainRecycleViewHolder> {
    @Service
    private Context context;
    @Service
    private LayoutInflater layoutInflater;

    public List<Map> getDemoList() {
        return demoList;
    }

    public void setDemoList(List<Map> demoList) {
        this.demoList = demoList;
    }

    private List<Map> demoList;
    @NonNull
    @NotNull
    @Override
    public MainRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MainRecycleViewHolder(layoutInflater.inflate(R.layout.item_activity_from,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MainRecycleViewHolder holder, int position) {
        if(position == 0){
            if(demoList.size() == 0)
                holder.empty();
            else
                holder.title();
        }else{
            holder.draw(demoList.get(position-1));
        }
    }


    @Override
    public int getItemCount() {
        return demoList.size()+1;
    }
}
