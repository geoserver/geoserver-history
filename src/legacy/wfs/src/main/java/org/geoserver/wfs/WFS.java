/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import org.geoserver.wfs.WFSInfo.ServiceLevel;
import org.vfny.geoserver.global.Config;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WFSDTO;


/**
 * WFS
 *
 * <p>
 * Represents the GeoServer information required to configure an  instance of
 * the WFS Server. This class holds the currently used  configuration and is
 * instantiated initially by the GeoServerPlugIn  at start-up, but may be
 * modified by the Configuration Interface  during runtime. Such modifications
 * come from the GeoServer Object  in the SessionContext.
 * </p>
 *
 * <p>
 * WFS wfs = new WFS(dto); System.out.println(wfs.getName());
 * System.out.println(wfs.getAbstract());
 * </p>
 *
 * @author Gabriel Rold???n
 * @author Chris Holmes
 * @author Justin Deoliveira
 *
 * @version $Id$
 * @deprecated use {@link WFSInfo}
 */
public class WFS extends org.vfny.geoserver.global.Service {
    /** web container key */
    public static final String WEB_CONTAINER_KEY = "WFS";

    /** ServiceLevel bit used to indicate Basic support */
    public static final int SERVICE_BASIC = WFSDTO.SERVICE_BASIC;

    /** ServiceLevel bit used to indicate Transaction Insert support */
    public static final int SERVICE_INSERT = WFSDTO.SERVICE_INSERT;

    /** ServiceLevel bit used to indicate Transaction Update support */
    public static final int SERVICE_UPDATE = WFSDTO.SERVICE_UPDATE;

    /** ServiceLevel bit used to indicate Transaction Delete support */
    public static final int SERVICE_DELETE = WFSDTO.SERVICE_DELETE;

    /** ServiceLevel bit used to indicate Locking support */
    public static final int SERVICE_LOCKING = WFSDTO.SERVICE_LOCKING;

    /** ServiceLevel mask equivilent to basic WFS conformance */
    //public static final int BASIC = WFSDTO.BASIC;
    public static final int BASIC = WFSInfo.ServiceLevel.BASIC.getCode();
    
    /** ServiceLevel mask for transactional WFS conformance. */
    //public static final int TRANSACTIONAL = WFSDTO.TRANSACTIONAL;
    public static final int TRANSACTIONAL = WFSInfo.ServiceLevel.TRANSACTIONAL.getCode();
    
    /** ServiceLevel mask equivilent to complete WFS conformance */
    //public static final int COMPLETE = WFSDTO.COMPLETE;
    public static final int COMPLETE = WFSInfo.ServiceLevel.COMPLETE.getCode();

    /**
     * Properties
     */
    //private GeoValidator gv;
    //private int serviceLevel;
    //private boolean srsXmlStyle;
    //private boolean citeConformanceHacks;
    //private boolean featureBounding;

    ///**
    // * WFS constructor.
    // *
    // * <p>
    // * Stores the data specified in the WFSDTO object in this WFS Object for
    // * GeoServer to use.
    // * </p>
    // *
    // * @param config The data intended for GeoServer to use.
    // */
    //public WFS(WFSDTO config) {
    //    super(config.getService());
    //    setId("wfs");
    //    setSrsXmlStyle(config.isSrsXmlStyle());
    //    setServiceLevel(config.getServiceLevel());
    //    setCiteConformanceHacks(config.getCiteConformanceHacks());
    //    setFeatureBounding(config.isFeatureBounding());
    //}

    ///**
    // * Creates the WFS service by getting the WFSDTO object from the
    // * config and calling {@link #WFS(WFSDTO)}.
    // *
    // * @throws ConfigurationException
    // */
    //public WFS(Config config, Data data, GeoServer geoServer, GeoValidator validator)
    //    throws ConfigurationException {
    //    this(config.getWfs());
    //    setData(data);
    //    setGeoServer(geoServer);
    //    setValidation(validator);
    //}

    ///**
    // * WFS constructor.
    // *
    // * <p>
    // * Package constructor intended for default use by GeoServer
    // * </p>
    // *
    // * @see GeoServer#GeoServer()
    // */
    //WFS() {
    //    super(new ServiceDTO());
    //    setId("wfs");
    //}

    WFSInfo wfs;
    
    public WFS( org.geoserver.config.GeoServer gs ) {
        super( gs.getService(WFSInfo.class), gs );
        init();
    }
    
    public void init() {
        this.wfs = gs.getService(WFSInfo.class);
        service = wfs;
    }
    
    public WFSInfo getInfo() {
        return wfs;
    }
    
    /**
     * load purpose.
     *
     * <p>
     * Loads a new data set into this object.
     * </p>
     *
     * @param config
     */
    public void load(WFSDTO config) {
        super.load(config.getService());
        setSrsXmlStyle(config.isSrsXmlStyle());
        setServiceLevel(config.getServiceLevel());
        setCiteConformanceHacks(config.getCiteConformanceHacks());
        setFeatureBounding(config.isFeatureBounding());
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
     * @return WFSDTO An instance of the data this class represents. Please see
     *         Caution Above.
     *
     * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
     * @see WFSDTO
     */
    public WFSDTO toDTO() {
        WFSDTO dto = new WFSDTO();
        dto.setService((ServiceDTO) super.toDTO());

        dto.setServiceLevel(getServiceLevel());
        dto.setSrsXmlStyle(isSrsXmlStyle());
        dto.setCiteConformanceHacks(getCiteConformanceHacks());
        dto.setFeatureBounding(isFeatureBounding());
        
        //dto.setServiceLevel(serviceLevel);
        //dto.setSrsXmlStyle(srsXmlStyle);
        //dto.setCiteConformanceHacks(citeConformanceHacks);
        //dto.setFeatureBounding(featureBounding);

        return dto;
    }

    /**
    * Whether the srs xml attribute should be in the EPSG:4326 (non-xml)
    * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326 style.
    *
    * @return <tt>true</tt> if the srs is reported with the xml style
    */
    public boolean isSrsXmlStyle() {
        GMLInfo gml = wfs.getGML().get( WFSInfo.Version.V_10 );
        return gml.getSrsNameStyle() == GMLInfo.SrsNameStyle.XML;
        //return srsXmlStyle;
    }

    /**
     * Sets whether the srs xml attribute should be in the EPSG:4326 (non-xml)
     * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326 style.
     *
     * @param doXmlStyle whether the srs style should be xml or not.
     */
    public void setSrsXmlStyle(boolean doXmlStyle) {
        GMLInfo gml = wfs.getGML().get( WFSInfo.Version.V_10 );
        
        if ( doXmlStyle ) {
            gml.setSrsNameStyle(GMLInfo.SrsNameStyle.XML);    
        }
        else {
            gml.setSrsNameStyle(GMLInfo.SrsNameStyle.NORMAL);
        }
        
        //this.srsXmlStyle = doXmlStyle;
    }

    /**
     * Gets the prefix for the srs name, based on the SrsXmlStyle property.  If
     * srsXmlStyle is <tt>true</tt> then it is of the xml style, if false then
     * it is EPSG: style.  More apps seem to like the EPSG: style, but the
     * specs seem to lean more to the xml type.  The xml style should actually
     * be more complete, with online lookups for the URI's, but I've seen no
     * real online srs services.
     *
     * @return <tt>http://www.opengis.net/gml/srs/epsg.xml#</tt> if srsXmlStyle
     *         is  <tt>true</tt>, <tt>EPSG:</tt> if <tt>false</tt>
     */
    public String getSrsPrefix() {
        GMLInfo gml = wfs.getGML().get( WFSInfo.Version.V_10 );
        return gml.getSrsNameStyle().getPrefix();
        //return srsXmlStyle ? "http://www.opengis.net/gml/srs/epsg.xml#" : "EPSG:";
    }

    ///**
    // * Access gv property.
    // *
    // * @return Returns the gv.
    // */
    //public GeoValidator getValidation() {
    //    return gv;
    //}

    ///**
    // * Set gv to gv.
    // *
    // * @param gv The gv to set.
    // */
    //void setValidation(GeoValidator gv) {
    //    this.gv = gv;
    //}

    /**
     * Sets serviceLevel property.
     *
     * @param serviceLevel The new service level.
     */
    public void setServiceLevel(int serviceLevel) {
        wfs.setServiceLevel( ServiceLevel.get( serviceLevel ) );
        //this.serviceLevel = serviceLevel;
    }

    /**
     * Access serviceLevel property.
     *
     * @return Returns the serviceLevel.
     */
    public int getServiceLevel() {
        return wfs.getServiceLevel().getCode();
        //return serviceLevel;
    }

    /**
     * turn on/off the citeConformanceHacks option.
     *
     * @param on
     */
    public void setCiteConformanceHacks(boolean on) {
        wfs.setCiteCompliant(on);
        //citeConformanceHacks = on;
    }

    /**
     * get the current value of the citeConformanceHacks
     *
     * @return
     */
    public boolean getCiteConformanceHacks() {
        return wfs.isCiteCompliant();
        //return (citeConformanceHacks);
    }

    /**
     * Returns whether the gml returned by getFeature includes an
     * auto-calculated bounds element on each feature or not.
     *
     * @return <tt>true</tt> if the gml features will have boundedBy
     *         automatically generated.
     */
    public boolean isFeatureBounding() {
        return wfs.isFeatureBounding();
        //return featureBounding;
    }

    /**
     * Sets whether the gml returned by getFeature includes an auto-calculated
     * bounds element on each feature or not.
     *
     * @param featureBounding <tt>true</tt> if gml features should have
     *        boundedBy automatically generated.
     */
    public void setFeatureBounding(boolean featureBounding) {
        wfs.setFeatureBounding(featureBounding);
        //this.featureBounding = featureBounding;
    }
}
