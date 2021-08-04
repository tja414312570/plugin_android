package com.yanan.todo.dto;

import com.yanan.framework.dto.annotations.Param;
import com.yanan.framework.dto.annotations.SQL;
import com.yanan.framework.dto.annotations.Xml;
import com.yanan.framework.fieldhandler.SQLite;
import com.yanan.todo.R;

import java.util.List;
import java.util.Map;

@SQL(id="column",value = "(id,name)")
@SQL(id="values",value = "(#{id},#{name})")
@SQLite(value = "test",creator = "CREATE TABLE IF NOT EXISTS test(id integer primary key,name varchar(20))")
//@Xml(R.xml.test)
public interface DemoDto {
    @SQL("insert into test {{column}} values {{values}}")
    public void insert(@Param("id") String key, @Param("name") String uname);
    @SQL("insert into test {{column}} values {{values}}")
    public void insert2(@Param("list") List<Object> map);
    @SQL("insert into test {{column}} values {{values}}")
    public void insert3(Map map);
    @SQL("select * from test")
    public List<Map> query();
}
