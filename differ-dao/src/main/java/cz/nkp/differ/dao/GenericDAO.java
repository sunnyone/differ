package cz.nkp.differ.dao;

import java.io.Serializable;

/**
 *
 * @author xrosecky
 */
public interface GenericDAO<T, ID extends Serializable> {
    
    public void persist(T object);
    
    public T findById(ID id);
    
    public void delete(T object);
    
}
