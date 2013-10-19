package cz.nkp.differ.dao;

import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface ImageDAO extends GenericDAO<Image, Long> {

    public List<Image> findImagesByUser(User user);
    public List<Image> findSharedImages();

}
