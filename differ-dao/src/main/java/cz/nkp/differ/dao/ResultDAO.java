package cz.nkp.differ.dao;

import java.util.List;

import cz.nkp.differ.model.Result;
import cz.nkp.differ.model.User;

/**
 *
 * @author xrosecky
 */
public interface ResultDAO extends GenericDAO<Result, Long> {

    public List<Result> findAll();

    public List<Result> findByUser(User user);

    public List<Result> findAllShared();

}
