/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers;

import org.vfny.geoserver.global.WMS;
import java.util.Map;


/**
 * Base class for all WMS KvpRequestReaders, wich just adds the
 * getRequestVersion() method wich returns the spec version a client has
 * requested or the default implementation version of this server if no
 * version has been requested, either by the "VERSION" parameter or by the
 * "WMTVER" parameter, wich is deprecated but it is recomended to recognize it
 *
 * @author Gabriel Roldán
 * @version $Id: WmsKvpRequestReader.java,v 1.4 2004/01/21 00:26:09 dmzwiers Exp $
 */
public abstract class WmsKvpRequestReader extends KvpRequestReader {
    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     */

    //protected static final GeoServer config = GeoServer.getInstance();

    /**
     * Creates a new WmsKvpRequestReader object.
     *
     * @param params DOCUMENT ME!
     */
    public WmsKvpRequestReader(Map params) {
        super(params);
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
            version = WMS.getVersion();
        }

        return version;
    }
}
