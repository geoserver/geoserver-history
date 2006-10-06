/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import org.geoserver.ows.OWS;

/**
 * Web Feature Service.
 * 
 * @author Gabriel Rold???n
 * @author Chris Holmes
 * @author Justin Deoliveira
 * 
 * @version $Id: WFS.java,v 1.8 2004/09/09 16:54:19 cholmesny Exp $
 */
public class WFS extends OWS {
  
	/** ServiceLevel bit used to indicate Basic support */
    public static final int SERVICE_BASIC = 1;

    /** ServiceLevel bit used to indicate Transaction Insert support */
    public static final int SERVICE_INSERT = 2;

    /** ServiceLevel bit used to indicate Transaction Update support */
    public static final int SERVICE_UPDATE = 4;

    /** ServiceLevel bit used to indicate Transaction Delete support */
    public static final int SERVICE_DELETE = 8;

    /** ServiceLevel bit used to indicate Locking support */
    public static final int SERVICE_LOCKING = 16;

    /** ServiceLevel mask equivilent to basic WFS conformance */
    public static final int BASIC = 1;

    /** ServiceLevel mask for transactional WFS conformance. */
    public static final int TRANSACTIONAL = SERVICE_BASIC | SERVICE_INSERT
        | SERVICE_UPDATE | SERVICE_DELETE;

    /** ServiceLevel mask equivilent to complete WFS conformance */
    public static final int COMPLETE = TRANSACTIONAL | SERVICE_LOCKING;
    
	private int serviceLevel = COMPLETE;
    private boolean srsXmlStyle;
    private boolean citeConformanceHacks;
    private boolean featureBounding;

   /**
     * Whether the srs xml attribute should be in the EPSG:4326 (non-xml)
     * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326 style.
     *
     * @return <tt>true</tt> if the srs is reported with the xml style
     */
    public boolean isSrsXmlStyle() {
        return srsXmlStyle;
    }

    /**
     * Sets whether the srs xml attribute should be in the EPSG:4326 (non-xml)
     * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326 style.
     *
     * @param doXmlStyle whether the srs style should be xml or not.
     */
    public void setSrsXmlStyle(boolean doXmlStyle) {
        this.srsXmlStyle = doXmlStyle;
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
        return srsXmlStyle ? "http://www.opengis.net/gml/srs/epsg.xml#" : "EPSG:";
    }

    /**
     * Access serviceLevel property.
     *
     * @return Returns the serviceLevel.
     */
    public int getServiceLevel() {
        return serviceLevel;
    }

    /**
     * Sets the service level.
     * 
     * @param serviceLevel 
     */
    public void setServiceLevel(int serviceLevel) {
		this.serviceLevel = serviceLevel;
	}
    
    /**
     * turn on/off the citeConformanceHacks option.
     *
     * @param on
     */
    public void setCiteConformanceHacks(boolean on) {
        citeConformanceHacks = on;
    }

    /**
     * get the current value of the citeConformanceHacks
     *
     * @return
     */
    public boolean getCiteConformanceHacks() {
        return (citeConformanceHacks);
    }

    /**
     * Returns whether the gml returned by getFeature includes an
     * auto-calculated bounds element on each feature or not.
     *
     * @return <tt>true</tt> if the gml features will have boundedBy
     *         automatically generated.
     */
    public boolean isFeatureBounding() {
        return featureBounding;
    }

    /**
     * Sets whether the gml returned by getFeature includes an auto-calculated
     * bounds element on each feature or not.
     *
     * @param featureBounding <tt>true</tt> if gml features should have
     *        boundedBy automatically generated.
     */
    public void setFeatureBounding(boolean featureBounding) {
        this.featureBounding = featureBounding;
    }
}
