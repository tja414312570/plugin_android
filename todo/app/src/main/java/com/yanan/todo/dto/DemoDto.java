package com.yanan.todo.dto;

import com.yanan.framework.dto.Label;
import com.yanan.framework.dto.annotations.Param;
import com.yanan.framework.dto.annotations.SQL;
import com.yanan.framework.dto.annotations.SQLFragment;
import com.yanan.framework.dto.annotations.Table;
import com.yanan.framework.fieldhandler.SQLite;
import com.yanan.util.xml.XmlResource;

import java.util.Map;

@Table(test="",creator="CREATE TABLE IF NOT EXISTS account(uid integer primary key,uname varchar(20),mobile varchar(20))")
@SQLite("test")
@SQL(id="column",value = "(key,uname,mobile)")
@SQL(id="values",value = "(#{key},#{uname},#{mobile})")
@SQLFragment({
        "select * from {table} where",
        "{trim('','','','')",
        "   {if(id != null){id = #{id}}}",
        "}"
})
public interface DemoDto {
    @SQL(id="column",value = "(key,uname,mobile)")
    public String column(String tokenizer);
    @SQL("insert account {column} value {values}")
    public void insert(@Param("key") String key, @Param("uname") String uname, @Param("mobile")String mobile);
    @SQL("insert account {column} value {values}")
    public void insert(Map map);
}
