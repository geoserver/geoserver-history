package org.geoserver.services.hibernate.beans;

import org.geoserver.hibernate.Hibernable;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfoImpl;

public class WMSInfoImplHb 
        extends WMSInfoImpl
        implements WMSInfo, Hibernable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1956340656640000352L;

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
