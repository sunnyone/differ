package cz.nkp.differ.dao;

import cz.nkp.differ.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author xrosecky
 */
@ContextConfiguration(locations = "classpath:appCtx-differ-dao-test.xml")
public class UserDAOTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private UserDAO userDao;

    @Test
    public void test() {
        String username = "user";
        String pwd = "pwd";
        User user = new User();
        user.setUserName(username);
        user.setPasswordHash(pwd);
        userDao.persist(user);
        Assert.assertNotNull(user.getId());
        Assert.assertNotNull(userDao.findByUserName(username));
        Assert.assertNotNull(userDao.findById(user.getId()));
    }
}
