/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.global.dto.*;

/**
 * WFS
 * 
 * <p>
 * Represents the GeoServer information required to configure an 
 * instance of the WFS Server. This class holds the currently used 
 * configuration and is instantiated initially by the GeoServerPlugIn 
 * at start-up, but may be modified by the Configuration Interface 
 * during runtime. Such modifications come from the GeoServer Object 
 * in the SessionContext. 
 * </p>
 * 
 * <p>
 * WFS wfs = new WFS(dto);
 * System.out.println(wfs.getName());
 * System.out.println(wfs.getAbstract());
 * </p>
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: WFS.java,v 1.1.2.7 2004/01/08 23:44:48 dmzwiers Exp $
 */
public class WFS extends Service {

	/**
	 * WFS constructor.
	 * <p>
	 * Stores the data specified in the WFSDTO object in this WFS Object for GeoServer to use.
	 * </p>
	 * @param config The data intended for GeoServer to use. 
	 */
    public WFS(WFSDTO config){
    	super(config.getService());
    }

	/**
	 * WFS constructor.
	 * <p>
	 * Package constructor intended for default use by GeoServer
	 * </p>
	 * @see GeoServer#GeoServer()
	 */
    WFS(){
    	super(new ServiceDTO());
    }

	/**
	 * Implement toDTO.
	 * <p>
	 * Package method used by GeoServer. This method may return references, 
	 * and does not clone, so extreme caution sould be used when traversing 
	 * the results.
	 * </p>
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 * @see WFSDTO
	 * 
	 * @return WFSDTO An instance of the data this class represents. Please see Caution Above.
	 */
    Object toDTO(){
    	WFSDTO dto = new WFSDTO();
    	dto.setService(config);
		return dto;
	}
}
