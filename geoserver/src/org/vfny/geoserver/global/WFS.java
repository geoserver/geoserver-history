/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

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
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: WFS.java,v 1.7 2004/02/12 00:43:20 dmzwiers Exp $
 */
public class WFS extends Service {
    private boolean gmlPrefixing;
    public static final String WEB_CONTAINER_KEY = "WFS";
    
    private GeoValidator gv;
    
    private int serviceLevel;

    /**
     * WFS constructor.
     * 
     * <p>
     * Stores the data specified in the WFSDTO object in this WFS Object for
     * GeoServer to use.
     * </p>
     *
     * @param config The data intended for GeoServer to use.
     */
    public WFS(WFSDTO config) {
        super(config.getService());
        gmlPrefixing = config.isGmlPrefixing();
        serviceLevel = config.getServiceLevel();
    }
    
    /**
     * load purpose.
     * <p>
     * Loads a new data set into this object.
     * </p>
     * @param config
     */
    public void load (WFSDTO config) {
    	super.load(config.getService());
    	gmlPrefixing = config.isGmlPrefixing();
    	serviceLevel = config.getServiceLevel();
    }
    /**
     * WFS constructor.
     * 
     * <p>
     * Package constructor intended for default use by GeoServer
     * </p>
     *
     * @see GeoServer#GeoServer()
     */
    WFS() {
        super(new ServiceDTO());
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
    public Object toDTO() {
        WFSDTO dto = new WFSDTO();
        dto.setService((ServiceDTO)super.toDTO());
        dto.setGmlPrefixing(gmlPrefixing);
        dto.setServiceLevel(serviceLevel);
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
        return gmlPrefixing;
    }

    /**
     * setGmlPrefixing purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setGmlPrefixing(boolean b) {
        gmlPrefixing = b;
    }
	/**
	 * Access gv property.
	 * 
	 * @return Returns the gv.
	 */
	public GeoValidator getValidation() {
		return gv;
	}

	/**
	 * Set gv to gv.
	 *
	 * @param gv The gv to set.
	 */
	void setValidation(GeoValidator gv) {
		this.gv = gv;
	}

	/**
	 * Access serviceLevel property.
	 * 
	 * @return Returns the serviceLevel.
	 */
	public int getServiceLevel() {
		return serviceLevel;
	}

}
