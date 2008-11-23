/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geoserver.wcs.WCSInfo;
import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WCSDTO;


/**
 * WCS
 *
 * <p>
 * Represents the GeoServer information required to configure an instance of the
 * WCS Server. This class holds the currently used configuration and is
 * instantiated initially by the GeoServerPlugIn at start-up, but may be
 * modified by the Configuration Interface during runtime. Such modifications
 * come from the GeoServer Object in the SessionContext.
 * </p>
 *
 * <p>
 * WCS wcs = new WCS(dto); System.out.println(wcs.getName());
 * System.out.println(wcs.getAbstract());
 * </p>
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id$
 * 
 * @deprecated use {@link WCSInfo}.
 */
public final class WCS extends Service {
    public static final String WEB_CONTAINER_KEY = "WCS";

    /** list of WMS Exception Formats */
    private static final String[] EXCEPTION_FORMATS = {
            "application/vnd.ogc.se_xml", "application/vnd.ogc.se_inimage",
            "application/vnd.ogc.se_blank"
        };

    ///**
    // *
    // * @uml.property name="gmlPrefixing" multiplicity="(0 1)"
    // */
    //private boolean gmlPrefixing;

    ///**
    // * WCS constructor.
    // *
    // * <p>
    // * Stores the data specified in the WCSDTO object in this WCS Object for
    // * GeoServer to use.
    // * </p>
    // *
    // * @param config
    // *            The data intended for GeoServer to use.
    // */
    //public WCS(WCSDTO config) {
    //    super(config.getService());
    //    setId("wcs");
    //    gmlPrefixing = config.isGmlPrefixing();
    //}

    ///**
    // * Creates the WCS service by getting the WCSDTO object from the
    // * config and calling {@link #WCS(WCSDTO)}.
    // *
    // * @throws ConfigurationException
    // */
    //public WCS(Config config, Data data, GeoServer geoServer)
    //    throws ConfigurationException {
    //    this(config.getWcs());
    //    setData(data);
    //    setGeoServer(geoServer);
    //}

    WCSInfo wcs;
    
    public WCS( org.geoserver.config.GeoServer gs ) {
        super( gs.getService(WCSInfo.class), gs );
        init();
    }
    
    public WCSInfo getInfo() {
        return wcs;
    }
    
    public void init() {
        wcs = gs.getService(WCSInfo.class);
        service = wcs;
    }
    
    ///**
    // * WCS constructor.
    // *
    // * <p>
    // * Package constructor intended for default use by GeoServer
    // * </p>
    // *
    // * @see GeoServer#GeoServer()
    // */
    //WCS() {
    //    super(new ServiceDTO());
    //    setId("wcs");
    //}

    /**
     * load purpose.
     *
     * <p>
     * Loads a new data set into this object.
     * </p>
     *
     * @param config
     */
    public void load(WCSDTO config) {
        super.load(config.getService());
        setGmlPrefixing(config.isGmlPrefixing());
        //gmlPrefixing = config.isGmlPrefixing();
        
        gs.save( wcs );
    }

    /**
     * Implement toDTO.
     *
     * <p>
     * Package method used by GeoServer. This method may return references, and
     * does not clone, so extreme caution sould be used when traversing the
     * results.
     * </p>
     *
     * @return WCSDTO An instance of the data this class represents. Please see
     *         Caution Above.
     *
     * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
     * @see WCSDTO
     */
    public WCSDTO toDTO() {
        WCSDTO dto = new WCSDTO();
        dto.setService((ServiceDTO) super.toDTO());
        dto.setGmlPrefixing(isGmlPrefixing());
        //dto.setGmlPrefixing(gmlPrefixing);

        return dto;
    }

    /**
     * isGmlPrefixing purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public boolean isGmlPrefixing() {
        return wcs.isGMLPrefixing();
        //return gmlPrefixing;
    }

    /**
     * setGmlPrefixing purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     *
     * @uml.property name="gmlPrefixing"
     */
    public void setGmlPrefixing(boolean b) {
        wcs.setGMLPrefixing( b );
        //gmlPrefixing = b;
    }

    /**
     * getExceptionFormats purpose.
     *
     * <p>
     * Returns a static list of Exception Formats in as Strings
     * </p>
     *
     * @return String[] a static list of Exception Formats
     */
    public String[] getExceptionFormats() {
        return EXCEPTION_FORMATS;
    }
}
