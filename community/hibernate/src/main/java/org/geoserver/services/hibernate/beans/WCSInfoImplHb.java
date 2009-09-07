package org.geoserver.services.hibernate.beans;

import org.geoserver.hibernate.Hibernable;
import org.geoserver.wcs.WCSInfo;
import org.geoserver.wcs.WCSInfoImpl;

public class WCSInfoImplHb 
        extends WCSInfoImpl
        implements WCSInfo, Hibernable {

    public WCSInfoImplHb() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"["
                + "id:" + getId()
                + " name:" + getName()
                + " title:" + getTitle()
                + "]";
    }
}
