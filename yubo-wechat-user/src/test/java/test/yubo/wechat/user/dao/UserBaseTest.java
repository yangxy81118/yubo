package test.yubo.wechat.user.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yubo.wechat.user.dao.UserBaseMapper;
import com.yubo.wechat.user.dao.pojo.UserBase;

/**
 * @author huangGuotao
 * 
 * @date 2014年7月15日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class UserBaseTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    UserBaseMapper dao;

    @Test
    public void test() {
    	UserBase ub = dao.selectByPrimaryKey(1);
    	System.out.println(ub);
    }
}