/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.requests;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.catalog.servlets.CATALOGService;
import org.vfny.geoserver.global.CATALOG;
import org.vfny.geoserver.global.GeoServer;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class CATALOGRequest extends Request {
    public static final String CATALOG_SERVICE_TYPE = "CATALOG";

    /**
     * A CATALOGRequest configured with CATALOG_SERVICE_TYPE
     */
    public CATALOGRequest(String requestType, CATALOGService service) {
        super(CATALOG_SERVICE_TYPE, requestType, service);
    }

    /**
     * Sets the catalog service object.
     */
    public void setCATALOGervice(CATALOGService catalog) {
        setServiceRef(catalog);
    }

    /**
     * Returns the catalog service object..
     */
    public CATALOGService getCATALOGervice() {
        return (CATALOGService) getServiceRef();
    }

    /**
     * Convenience method for obtaining the global catalog service instance.
     */
    public CATALOG getCATALOG() {
        return getCATALOGervice().getCATALOG();
    }

    /**
     * Convenience method for obtaining the global geoserver instance.
     */
    public GeoServer getGeoServer() {
        GeoServer gs = getCATALOG().getGeoServer();

        return gs;
    }
}
