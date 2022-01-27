import com.alibaba.fastjson.JSON;
import com.jungle.start.mybatis.entity.UserEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XpathTester {

    @Test
    public void testXpathParser() throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        InputStream resource = Resources.getResourceAsStream("user_info.xml");
        Document doc = documentBuilder.parse(resource);


        XPath xPath = XPathFactory.newInstance().newXPath();

        NodeList nodeList = (NodeList) xPath.evaluate("/users/*", doc, XPathConstants.NODESET);
        List<UserEntity> result = new ArrayList<>();

        for (int i = 1; i <= nodeList.getLength(); i++) {
            String path = "/users/user[" + i + "]";
            String id = (String) xPath.evaluate(path + "/@id", doc, XPathConstants.STRING);
            String name = (String) xPath.evaluate(path + "/name", doc, XPathConstants.STRING);
            String createTime = (String) xPath.evaluate(path + "/createTime", doc, XPathConstants.STRING);
            String phone = (String) xPath.evaluate(path + "/phone", doc, XPathConstants.STRING);
            String nickname = (String) xPath.evaluate(path + "/nickename", doc, XPathConstants.STRING);
            UserEntity user = buildUser(id, name, createTime, phone, nickname);
            result.add(user);
        }
        System.out.println(JSON.toJSONString(result));

    }

    @Test
    public void testXpathParser2() throws Exception {
        InputStream resource = Resources.getResourceAsStream("user_info.xml");
        XPathParser parser = new XPathParser(resource);
        DateConverter dateConverter = new DateConverter(null);
        dateConverter.setPattern("yyyy-MM-dd HH:mm:ss");
        ConvertUtils.register(dateConverter, Date.class);
        List<UserEntity> result = new ArrayList<>();
        List<XNode> nodeList = parser.evalNodes("/users/*");
        for (XNode node : nodeList) {
            UserEntity userEntity = new UserEntity();
            result.add(userEntity);
            Long id = node.getLongAttribute("id");
            userEntity.setId(id);
            List<XNode> children = node.getChildren();
            for (XNode child : children) {
                BeanUtils.setProperty(userEntity, child.getName(), child.getStringBody());
            }

        }


        System.out.println(JSON.toJSONString(result));


    }


    private UserEntity buildUser(String id, String name, String createTime, String phone, String nickname) throws ParseException {
        UserEntity entity = new UserEntity();
        entity.setId(Long.valueOf(id));
        entity.setName(name);
        entity.setNickname(nickname);
        entity.setPhone(phone);
        entity.setCreateTime(SimpleDateFormat.getDateInstance().parse(createTime));
        return entity;
    }

}
