package cz.nkp.differ;

import org.testng.Assert;
import org.testng.annotations.Test;

import cz.nkp.differ.dao.BatchDAO;
import cz.nkp.differ.model.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

/**
 *
 * @author xrosecky
 */
@ContextConfiguration(locations = "classpath:appCtx-differ-dao-test.xml")
public class BatchDAOTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Autowired
    private BatchDAO batchDao;
    
    @Test
    public void test() {
        Batch batch = new Batch();
        batchDao.persist(batch);
        Assert.assertTrue(true);
    }
    
}
