package cz.nkp.differ.dao;

import cz.nkp.differ.dao.hibernate.GenericHibernateDAO;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class ImageDAOImpl extends GenericHibernateDAO<Image, Long> implements ImageDAO {

    @Override
    public List<Image> findImagesByUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Image> findSharedImages() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
