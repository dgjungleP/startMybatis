package com.jungle.start.mybatis;

import com.jungel.start.mybatis.entity.UserEntity;
import com.jungel.start.mybatis.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;

public class TestMybatis {
    public SqlSession sqlSession;
    public SqlSessionFactory sqlSessionFactory;
    public SqlSessionManager sqlSessionManager;

    @Before
    public void before() throws Exception {
        InputStream resourceFactory = Resources.getResourceAsStream("mybatis-config.xml");
        InputStream resourceManager = Resources.getResourceAsStream("mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceFactory);
        sqlSessionManager = SqlSessionManager.newInstance(resourceManager);
        sqlSessionManager.startManagedSession();
        sqlSession = sqlSessionFactory.openSession();
        Connection connection = sqlSession.getConnection();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.runScript(Resources.getResourceAsReader("create-table.sql"));
        runner.runScript(Resources.getResourceAsReader("init-data.sql"));
    }

    @Test
    public void testMybatis() throws Exception {
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<UserEntity> userEntities = mapper.listAllUser();
        System.out.println(userEntities);
    }

    @Test
    public void testSqlSessionManager() {
        UserMapper mapper = sqlSessionManager.getMapper(UserMapper.class);
        List<UserEntity> userEntities = mapper.listAllUser();
        System.out.println(userEntities);
    }
}
