package cz.nkp.differ.dao.hibernate;

import cz.nkp.differ.dao.BatchDAO;
import cz.nkp.differ.model.Batch;

/**
 *
 * @author xrosecky
 */
public class BatchHibernateDAO extends GenericHibernateDAO<Batch, Long> implements BatchDAO {

    @Override
    public Batch findByIdentifier(String identifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
