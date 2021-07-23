package com.yanan.todo.ui.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yanan.framework.fieldhandler.Views;
import com.yanan.todo.R;

import org.jetbrains.annotations.NotNull;

public class MainRecycleViewHolder extends RecyclerView.ViewHolder {
    @Views(R.id.empty_text)
    private TextView textView;
    public MainRecycleViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.empty_text);
    }

}
