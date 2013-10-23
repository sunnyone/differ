package cz.nkp.differ.dao;

import cz.nkp.differ.model.Batch;
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
public class BatchDAOTest extends AbstractTransactionalTestNGSpringContextTests {
    
    @Autowired
    private BatchDAO batchDao;
    
    @Test
    public void test() {
        String ident = "test";
        Batch batch = new Batch();
        batch.setIdentifier(ident);
        batchDao.persist(batch);
        Assert.assertNotNull(batch.getId());
        Assert.assertNotNull(batchDao.findByIdentifier(ident));
        Assert.assertNotNull(batchDao.findById(batch.getId()));
    }
    
}
