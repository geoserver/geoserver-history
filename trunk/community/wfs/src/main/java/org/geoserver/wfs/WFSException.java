/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import org.geoserver.ows.ServiceException;

/**
 * WFS application specific exception.
 */
public class WFSException extends ServiceException {

	public WFSException(String message, String code) {
		super(message, code);
	}

	public WFSException(String message, Throwable cause, String code) {
		super(message, cause, code);
	}

	public WFSException(String code) {
		super(code);
	}

	public WFSException(Throwable cause, String code) {
		super(cause, code);
	
	}
    
}
