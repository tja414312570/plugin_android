package com.yanan.framework.form;

import android.view.View;

public class FormItem {
    private View view;
    private FormHolder formHolder;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public FormHolder getFormHolder() {
        return formHolder;
    }

    public void setFormHolder(FormHolder formHolder) {
        this.formHolder = formHolder;
    }
}
