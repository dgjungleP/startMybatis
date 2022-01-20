package com.jungle.start.mybatis;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.executor.loader.javassist.JavassistProxyFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class SqlTest {
    private Connection connection;

    @Before
    public void before() throws SQLException {
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:mybatis", "sa", "");
    }

    @Test
    public void testSql() {
        String sql = new SQL() {
            {
                SELECT("*");
                FROM("user");
            }
        }.toString();
        System.out.println(sql);
    }

    @Test
    public void testScriptRunner() throws SQLException, IOException {
        ScriptRunner runner = new ScriptRunner(connection);
        runner.runScript(Resources.getResourceAsReader("create-table.sql"));
        runner.runScript(Resources.getResourceAsReader("init-data.sql"));
        SqlRunner sqlRunner = new SqlRunner(connection);
        String sql = new SQL() {{
            SELECT("*");
            FROM("user_info");
            WHERE("id =?");

        }}.toString();
        Map<String, Object> one = sqlRunner.selectOne(sql, 1);
        System.out.println(JSON.toJSONString(one));
    }

    @Test
    public void testMetaObject() {
        List<Order> orderList = new ArrayList() {
            {
                add(new Order("A1", "hello"));
                add(new Order("A2", "world"));
            }
        };
        User user = new User(orderList, "Jungle", 10);
        MetaObject metaObject = SystemMetaObject.forObject(user);
        System.out.println(metaObject.getValue("orders[0].goodsName"));
        System.out.println(metaObject.getValue("orders[1].goodsName"));
        metaObject.setValue("orders[1].orderNo", "C3");
        System.out.println(metaObject.hasGetter("orderNo"));
        System.out.println(metaObject.hasGetter("name"));
        System.out.println(user);

    }

    @Test
    public void testMetaClass() {
        MetaClass metaClass = MetaClass.forClass(Order.class, new DefaultReflectorFactory());
        String[] getterNames = metaClass.getGetterNames();
        System.out.println(JSON.toJSONString(getterNames));
        System.out.println(metaClass.hasDefaultConstructor());
        System.out.println(metaClass.hasGetter("orderNo"));
        System.out.println(metaClass.hasSetter("orderNo"));
        System.out.println(metaClass.getGetterType("orderNo"));
        Invoker orderNo = metaClass.getGetInvoker("orderNo");
        try {
            Object invoke = orderNo.invoke(new Order("A1", "hello"), null);
            System.out.println(invoke);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testObjectFactory() {
        ObjectFactory factory = new DefaultObjectFactory();
        List<Integer> list = factory.create(List.class);
        Map<String, String> map = factory.create(Map.class);
        list.addAll(Arrays.asList(1, 2, 3, 4));
        map.put("HEll0", "World");
        System.out.println(list);
        System.out.println(map);
    }

    @Test
    public void testProxyFactory() {
        JavassistProxyFactory factory = new JavassistProxyFactory();
        Order order = new Order("A1", "Hello");
        DefaultObjectFactory objectFactory = new DefaultObjectFactory();
        Object proxy = factory.createProxy(order,
                mock(ResultLoaderMap.class),
                mock(Configuration.class),
                objectFactory,
                Arrays.asList(String.class, String.class),
                Arrays.asList(order.getOrderNo(), order.getGoodsName()));
        System.out.println(proxy.getClass());
        System.out.println(((Order) proxy).getGoodsName());

    }


}
