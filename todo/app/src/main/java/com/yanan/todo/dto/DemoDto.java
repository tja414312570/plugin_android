package com.yanan.todo.dto;

import com.yanan.framework.dto.annotations.Param;
import com.yanan.framework.dto.annotations.SQL;
import com.yanan.framework.dto.annotations.Xml;
import com.yanan.framework.fieldhandler.SQLite;
import com.yanan.todo.R;

import java.util.List;

@SQL(id="column",value = "(key,uname,mobile)")
@SQL(id="values",value = "(#{key},#{uname},#{mobile})")
@SQLite(value = "test",creator = "{com.yanan.todo.dto.DemoDto.creator}")
//@Xml(R.xml.test)
public interface DemoDto {
    @SQL("insert into account {{column}} values {{values}}")
    public void insert(@Param("key") String key, @Param("uname") String uname, @Param("mobile")String mobile);
    @SQL("insert into account {{column}} values {{values}}")
    public void insert2(@Param("list") List<Object> map);
}
