package cz.nkp.differ.dao;

import cz.nkp.differ.model.Batch;

/**
 *
 * @author xrosecky
 */
public interface BatchDAO extends GenericDAO<Batch, Long> {
    
    public Batch findByIdentifier(String identifier);
    
}
