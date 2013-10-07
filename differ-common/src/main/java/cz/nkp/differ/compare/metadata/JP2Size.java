/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.nkp.differ.compare.metadata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author xrosecky
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public class JP2Size {
    public int width;
    public int height;

    public JP2Size() {
    }

    public JP2Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.width;
        hash = 53 * hash + this.height;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JP2Size other = (JP2Size) obj;
        if (this.width != other.width) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }
        return true;
    }
    
}
