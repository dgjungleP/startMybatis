import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ConfigurationTester {
    private InputStream resource;

    @Before
    public void before() throws IOException {
        resource = Resources.getResourceAsStream("mybatis-config.xml");
    }

    @Test
    public void testConfiguration() {
        XMLConfigBuilder builder = new XMLConfigBuilder(resource);
        Configuration configuration = builder.parse();
        System.out.println(configuration);
    }

    @Test
    public void testSqlSession() {
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(resource);
        SqlSession sqlSession = factory.openSession();

        System.out.println(sqlSession);
    }
}
