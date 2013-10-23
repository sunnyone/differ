package cz.nkp.differ.dao;

import cz.nkp.differ.model.Image;
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
public class ImageDAOTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private ImageDAO imageDao;

    @Test
    public void test() {
        Image image = new Image();
        imageDao.persist(image);
        Assert.assertNotNull(image.getId());
        Assert.assertNotNull(imageDao.findById(image.getId()));
    }
}
