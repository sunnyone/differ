package cz.nkp.differ.dao;

import cz.nkp.differ.model.Result;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;

/**
 *
 * @author xrosecky
 */
@ContextConfiguration(locations = "classpath:appCtx-differ-dao-test.xml")
public class ResultDAOTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private ResultDAO resultDao;

    @Test
    public void test() {
        Result result = new Result();
        resultDao.persist(result);
        Assert.assertNotNull(result.getId());
        Assert.assertNotNull(resultDao.findById(result.getId()));
    }

}
