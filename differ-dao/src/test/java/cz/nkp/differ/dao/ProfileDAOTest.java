package cz.nkp.differ.dao;

import cz.nkp.differ.model.Profile;
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
public class ProfileDAOTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Autowired
    private ProfileDAO profileDao;

    @Test
    public void test() {
        Profile profile1 = new Profile();
        profile1.setName("ndk");
        profile1.setShared(true);
        profileDao.persist(profile1);
        Assert.assertNotNull(profile1.getId());
        Assert.assertNotNull(profileDao.findById(profile1.getId()));
        Assert.assertTrue(profileDao.findAllShared().size() > 0);
        profile1.setShared(false);
        profileDao.persist(profile1);
        Assert.assertTrue(profileDao.findAllShared().isEmpty());
    }
    
}
