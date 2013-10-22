/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.nkp.differ.io;

import cz.nkp.differ.exceptions.ImageDifferException;
import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.io.File;
import java.util.List;

/**
 *
 * @author xrosecky
 */
public interface ImageManager {

    public boolean checkIfFileSizeIsOK(long size);

    public boolean deleteImage(User user, Image image);

    public List<Image> getImages(User user);

    public int getMaxSize();

    public boolean isSupportedByExtension(String fileName);

    public void updateImage(Image image);

    public Image uploadImage(User user, File source) throws ImageDifferException;

    public Image uploadImage(User user, File source, String fileName) throws ImageDifferException;
    
}
