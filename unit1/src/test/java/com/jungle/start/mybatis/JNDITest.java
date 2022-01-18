package com.jungle.start.mybatis;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class JNDITest {

    @Before
    public void before() throws IOException {
        UnpooledDataSourceFactory factory = new UnpooledDataSourceFactory();
        Properties properties = new Properties();
        InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream("datasource.properties");
        properties.load(resource);
        factory.setProperties(properties);
        DataSource dataSource = factory.getDataSource();

        try {
            Properties jndiProperties = new Properties();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            jndiProperties.put(Context.URL_PKG_PREFIXES, "org.apache.naming");
            InitialContext context = new InitialContext(jndiProperties);
            context.bind("java:TestDC", dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJNDI() {
        try {
            Properties jndiProperties = new Properties();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            jndiProperties.put(Context.URL_PKG_PREFIXES, "org.apache.naming");
            InitialContext context = new InitialContext(jndiProperties);
            DataSource dataSource = (DataSource) context.lookup("java:TestDC");
            Connection connection = dataSource.getConnection();
            Assert.assertNotNull(connection);
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

}
