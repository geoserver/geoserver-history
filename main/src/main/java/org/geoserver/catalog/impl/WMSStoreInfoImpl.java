package org.geoserver.catalog.impl;

import java.io.IOException;
import java.net.URL;

import org.geoserver.catalog.CatalogVisitor;
import org.geoserver.catalog.WMSStoreInfo;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;
import org.opengis.util.ProgressListener;

public class WMSStoreInfoImpl extends StoreInfoImpl implements WMSStoreInfo {

    String capabilitiesURL;

    public WMSStoreInfoImpl(CatalogImpl catalog) {
        super(catalog);
    }

    public String getCapabilitiesURL() {
        return capabilitiesURL;
    }

    public void setCapabilitiesURL(String capabilitiesURL) {
        this.capabilitiesURL = capabilitiesURL;
    }

    public void accept(CatalogVisitor visitor) {
        visitor.visit(this);
    }

    public WebMapServer getWebMapServer(ProgressListener listener) throws IOException {
        
        try {
            return new WebMapServer(new URL(capabilitiesURL));
        } catch (ServiceException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

}
