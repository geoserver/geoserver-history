package org.vfny.geoserver.requests.readers;

import java.util.Map;

import org.vfny.geoserver.global.WMS;


/**
 * Base class for all WMS KvpRequestReaders, wich just adds the
 * getRequestVersion() method wich returns the spec version a client
 * has requested or the default implementation version of this server
 * if no version has been requested, either by the "VERSION" parameter
 * or by the "WMTVER" parameter, wich is deprecated but it is recomended
 * to recognize it
 *
 * @author Gabriel Roldán
 * @version $Id: WmsKvpRequestReader.java,v 1.3 2004/01/12 21:01:25 dmzwiers Exp $
 */
public abstract class WmsKvpRequestReader extends KvpRequestReader
{
    /** DOCUMENT ME! */
    //protected static final GeoServer config = GeoServer.getInstance();

    /**
     * Creates a new WmsKvpRequestReader object.
     *
     * @param params DOCUMENT ME!
     */
    public WmsKvpRequestReader(Map params)
    {
        super(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getRequestVersion()
    {
        String version = getValue("VERSION");

        if (version == null)
            version = getValue("WMTVER");

        if (version == null)
            version = WMS.getVersion();

        return version;
    }
}
