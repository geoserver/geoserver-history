package org.geoserver.services.hibernate.beans;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import org.geoserver.services.hibernate.intf.GMLInfoHb;
import org.geoserver.wfs.GMLInfoImpl;

public class GMLInfoImplHb extends GMLInfoImpl implements GMLInfoHb {

    /**
	 * 
	 */
    private static final long serialVersionUID = -214390976416597998L;

    private String srsNameStyleHIB = null;

    public GMLInfoImplHb() {
    }

    // --- Wrapping enum into strings for a cleaner db dump

    protected String getSrsNameStyleHIB() {
        return srsNameStyleHIB;
    }

    protected void setSrsNameStyleHIB(String srsNameStyleHIB) {
        this.srsNameStyleHIB = srsNameStyleHIB;
    }

    @PrePersist
    protected void beforeSave() {
        if (getSrsNameStyle() == null)
            setSrsNameStyleHIB(null);
        else
            setSrsNameStyleHIB(getSrsNameStyle().name());
    }

    @PostLoad
    protected void afterLoad() {
        if (getSrsNameStyleHIB() == null)
            setSrsNameStyle(null);
        else
            setSrsNameStyle(SrsNameStyle.valueOf(getSrsNameStyleHIB()));
    }

    // --- /wrapping

}
