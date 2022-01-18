package com.jungle.start.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlRunner;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

public class Example {
    private Connection connection = null;

    @Before
    public void initData() {
        try {
//            加载驱动
            Class.forName("org.hsqldb.jdbcDriver");
//            获取connection对象
            connection = DriverManager.getConnection("jdbc:hsqldb:mem:mybatis", "sa", "");
            ScriptRunner runner = new ScriptRunner(connection);
            runner.runScript(Resources.getResourceAsReader("create-table.sql"));
            runner.runScript(Resources.getResourceAsReader("init-data.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testHsqldbQuery() {
        SqlRunner sqlRunner = new SqlRunner(connection);

        try {
            List<Map<String, Object>> all = sqlRunner.selectAll("select * from user");
            all.forEach(System.out::println);
            sqlRunner.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
