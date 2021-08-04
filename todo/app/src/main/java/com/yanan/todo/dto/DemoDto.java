package com.yanan.todo.dto;

import com.yanan.framework.dto.annotations.Param;
import com.yanan.framework.dto.annotations.SQL;
import com.yanan.framework.dto.annotations.Xml;
import com.yanan.framework.fieldhandler.SQLite;
import com.yanan.todo.R;

import java.util.List;
import java.util.Map;

@SQL(id="column",value = "(id,name,mobile,note,action)")
@SQL(id="values",value = "(#{id},#{name},#{mobile},#{note},#{action})")
@SQLite(value = "test",creator = "CREATE TABLE IF NOT EXISTS demo(id integer primary key,name varchar(20),mobile varchar(20),action varchar(20),note varchar(20))")
//@Xml(R.xml.test)
public interface DemoDto {
    @SQL("insert into demo {{column}} values {{values}}")
    public void insert(@Param("id") String key, @Param("name") String uname);
    @SQL("insert into demo {{column}} values {{values}}")
    public void insert2(@Param("list") List<Object> map);
    @SQL("insert into demo {{column}} values {{values}}")
    public void insert3(Map map);
    @SQL("delete from demo")
    public void delete();
    @SQL("select * from demo order by id")
    public List<Map> query();
    @SQL("select count(0) from demo")
    public int queryCount();
}
