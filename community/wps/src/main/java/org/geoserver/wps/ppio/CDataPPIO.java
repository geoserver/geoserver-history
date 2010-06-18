/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.ppio;

/**
 * Process parameter input / output for objects which are text based (no Base64 encoding needed)
 *  
 * @author Andrea Aime, OpenGEO
 */
public abstract class CDataPPIO extends ComplexPPIO {

	protected CDataPPIO(Class externalType, Class internalType, String mimeType) {
		super(externalType, internalType, mimeType);
	}
	
	/**
     * Encodes the internal object representation of a parameter as a string.
     */
    public abstract String encode( Object value );

}
