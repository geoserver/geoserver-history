package org.geoserver.services.hibernate.beans;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.hibernate.Hibernable;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfoImpl;
import org.geoserver.wms.WatermarkInfo;

public class WMSInfoImplHb 
        extends WMSInfoImpl
        implements WMSInfo, Hibernable {

    public WMSInfoImplHb() {
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
