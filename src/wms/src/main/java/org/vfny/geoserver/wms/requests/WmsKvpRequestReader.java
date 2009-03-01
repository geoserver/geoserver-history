/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSInfo;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wms.servlets.WMService;
import java.util.Map;


/**
 * Base class for all WMS KvpRequestReaders, wich just adds the
 * getRequestVersion() method wich returns the spec version a client has
 * requested or the default implementation version of this server if no
 * version has been requested, either by the "VERSION" parameter or by the
 * "WMTVER" parameter, wich is deprecated but it is recomended to recognize it
 *
 * @author Gabriel Rold?n
 * @version $Id$
 */
public abstract class WmsKvpRequestReader extends KvpRequestReader {

    private WMS wms;

    /**
     * Creates a new kvp reader for a WMS request.
     *
     * @param kvpPairs The raw key value pairs.
     * @param wms The WMS config object.
     */
    public WmsKvpRequestReader(Map kvpPairs, WMS wms) {
        super(kvpPairs, wms.getServiceInfo());
        this.wms = wms;
    }
    
    public WMS getWMS() {
        return wms;
    }
    
    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    protected String getRequestVersion() {
        String version = getValue("VERSION");

        if (version == null) {
            version = getValue("WMTVER");
        }

        if (version == null) {
            version = getWMS().getVersion();
        }

        return version;
    }
}
