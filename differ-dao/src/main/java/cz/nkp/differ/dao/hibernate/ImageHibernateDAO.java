package cz.nkp.differ.dao.hibernate;

import cz.nkp.differ.dao.ImageDAO;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public class ImageHibernateDAO extends GenericHibernateDAO<Image, Long> implements ImageDAO {

    @Override
    public List<Image> findImagesByUser(User user) {
        return getHibernateTemplate().find("from Image where ownerId = ?", user.getId());
    }

    @Override
    public List<Image> findSharedImages() {
        return (List<Image>) getHibernateTemplate().find("from Image where shared = true");
    }

}
