package cz.nkp.differ.dao.hibernate;

import cz.nkp.differ.dao.ResultDAO;
import cz.nkp.differ.model.Result;
import cz.nkp.differ.model.User;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class ResultHibernateDAO extends GenericHibernateDAO<Result, Long> implements ResultDAO  {

    @Override
    public List<Result> findAllShared() {
	return (List<Result>) getHibernateTemplate().find("from Result where shared = true");
    }

    @Override
    public List<Result> findByUser(User user) {
	return (List<Result>) getHibernateTemplate().find("from Result where userId = ?", user.getId());
    }

    @Override
    public List<Result> findAll() {
	return (List<Result>) getHibernateTemplate().find("from Result");
    }

}
