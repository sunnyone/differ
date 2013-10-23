package cz.nkp.differ.dao.hibernate;

import cz.nkp.differ.dao.BatchDAO;
import cz.nkp.differ.model.Batch;
import org.springframework.dao.support.DataAccessUtils;

/**
 *
 * @author xrosecky
 */
public class BatchHibernateDAO extends GenericHibernateDAO<Batch, Long> implements BatchDAO {

    @Override
    public Batch findByIdentifier(String identifier) {
                return (Batch) DataAccessUtils.singleResult(getHibernateTemplate()
                .find("from Batch where identifier = ?", identifier));
    }
    
}
