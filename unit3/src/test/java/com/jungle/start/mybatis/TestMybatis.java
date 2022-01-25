package com.jungle.start.mybatis;

import com.alibaba.fastjson.JSON;
import com.jungel.start.mybatis.entity.UserEntity;
import com.jungel.start.mybatis.mapper.UserMapper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
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

    @Test
    public void testExecutor() throws Exception {
        Configuration configuration = sqlSession.getConfiguration();
        MappedStatement mappedStatement = configuration.getMappedStatement("com.jungel.start.mybatis.mapper.UserMapper.listAllUser");
        Executor executor = configuration.newExecutor(new JdbcTransaction(sqlSession.getConnection()), ExecutorType.REUSE);
        List<UserEntity> query = executor.query(mappedStatement, null, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
        System.out.println(JSON.toJSON(query));

    }
}
