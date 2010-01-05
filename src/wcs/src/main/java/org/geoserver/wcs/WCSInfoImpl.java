package org.geoserver.wcs;

import org.geoserver.config.impl.ServiceInfoImpl;
/**
 * Default implementation for the {@link WCSInfo} bean.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
@SuppressWarnings("unchecked")
public class WCSInfoImpl extends ServiceInfoImpl implements WCSInfo {

    /**
     * 
     */
    private static final long serialVersionUID = 3721044439071286273L;

    boolean gmlPrefixing;

    public WCSInfoImpl() {
        setId("wcs");
    }

    public boolean isGMLPrefixing() {
        return gmlPrefixing;
    }

    public void setGMLPrefixing(boolean gmlPrefixing) {
        this.gmlPrefixing = gmlPrefixing;
    }

}
