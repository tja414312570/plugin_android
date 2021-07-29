package com.yanan.framework.form.defaults;

import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import com.yanan.framework.form.FormContext;
import com.yanan.framework.form.FormHolder;

public class EditTextHolder implements FormHolder<EditText,String> {
    static {
        FormContext.register(EditText.class,new EditTextHolder());
        FormContext.register(AppCompatEditText.class,new EditTextHolder());
        FormContext.register(MultiAutoCompleteTextView.class,new EditTextHolder());
        FormContext.register(AppCompatMultiAutoCompleteTextView.class,new EditTextHolder());
    }
    @Override
    public void set(EditText view, String value) {
        view.setText(value);
    }

    @Override
    public String get(EditText view) {
        return view.getText().toString();
    }
}
