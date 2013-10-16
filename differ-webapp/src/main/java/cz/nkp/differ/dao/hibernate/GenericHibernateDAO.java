package cz.nkp.differ.dao.hibernate;

import cz.nkp.differ.dao.GenericDAO;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author xrosecky
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable> extends HibernateDaoSupport implements GenericDAO<T, ID> {

    private Class<T> persistentClass;
    
    @SuppressWarnings("unchecked")
    public GenericHibernateDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    @Override
    public void persist(T object) {
       super.getHibernateTemplate().persist(object);
    }

    @Override
    public T findById(ID id) {
        return super.getHibernateTemplate().get(persistentClass, id);
    }

    @Override
    public void delete(T object) {
        super.getHibernateTemplate().delete(object);
    }
    
}
