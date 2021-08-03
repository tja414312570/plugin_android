package com.yanan.todo;

import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yanan.framework.Plugin;
import com.yanan.framework.classhandler.ContextView;
import com.yanan.framework.classhandler.NoActionBar;
import com.yanan.framework.event.Click;
import com.yanan.framework.event.EventContext;
import com.yanan.framework.fieldhandler.SQLite;
import com.yanan.framework.fieldhandler.Service;
import com.yanan.framework.fieldhandler.Values;
import com.yanan.framework.fieldhandler.Views;
import com.yanan.framework.form.FormContext;
import com.yanan.todo.dto.DemoDto;
import com.yanan.util.xml.XMLHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoActionBar
@ContextView(R.layout.activity_form)
public class FormActivity extends AppCompatActivity {

    @Values("hello \\{{app_name}\\}") //获取资源数据
    private String app_names;
    @Views(R.id.text_form)
    private ViewGroup viewGroup;
    @SQLite("test.db")
    private SQLiteDatabase sqLiteDatabase;
    @Service
    private DemoDto demoDto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Plugin.inject(this);
        Toast.makeText(getApplication(),app_names,Toast.LENGTH_SHORT).show();
        Map<String,String> params = new HashMap<String,String>();
        params.put("id", "test username");
        params.put("name", "test usex");

        List<Object> objectList = new ArrayList<>();
        objectList.add(params);
        params = new HashMap<String,String>();
        params.put("id", "test username");
        params.put("name", "test usex");
        objectList.add(params);
        demoDto.insert(objectList);
    }
    @Click(R.id.button)
    public void onButtonClick(View view) throws IOException, XmlPullParserException {

        FormContext formContext = FormContext.getFormContext(viewGroup);
        formContext = FormContext.getFormContext(R.id.text_form);
        Toast.makeText(getApplication()," 表单内容"+formContext.toString(),Toast.LENGTH_SHORT).show();
//        demoDto.insert(formContext.toMap());
        sqLiteDatabase.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");
    }
    @Override
    protected void onDestroy() {
        EventContext.completedEvent();
        super.onDestroy();
    }
}