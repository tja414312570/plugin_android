package com.yanan.todo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yanan.framework.Plugin;
import com.yanan.framework.fieldhandler.AutoInject;
import com.yanan.framework.fieldhandler.Service;
import com.yanan.todo.R;

import org.jetbrains.annotations.NotNull;

@AutoInject
public class MainRecycleViewAdapter extends RecyclerView.Adapter<MainRecycleViewHolder> {
    @Service
    private Context context;
    @Service
    private LayoutInflater layoutInflater;
    @NonNull
    @NotNull
    @Override
    public MainRecycleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        return Plugin.createInstance(MainRecycleViewHolder.class,true);
        return new MainRecycleViewHolder(layoutInflater.inflate(R.layout.item_empty,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MainRecycleViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 25;
    }
}
