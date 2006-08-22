/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.http.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility class performing operations related to http requests.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class RequestUtils {

	/**
	 * Pulls out the base url ( from the client point of view ), based on the 
	 * given request object.
	 * 
	 * @return A String of the form "<schema>://<server>:<port>/<context>/"
	 * 
	 */
	public static String baseURL( HttpServletRequest req ) {
		String url = req.getScheme() + "://" + req.getServerName() + ":"
        + req.getServerPort() + req.getContextPath() +"/";
		
		return url;
	}
}
