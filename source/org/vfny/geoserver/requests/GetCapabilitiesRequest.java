/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.util.*;


/**
 * This class enforces a standard interface for GetCapabilities reqeusts
 * 
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class GetCapabilitiesRequest {


		/** Stores version of WFS protocol requested (i.e. 0.0.14 or 0.0.15) */
		private String version = new String();

		/** Stores the service requested by the client (i.e. WFS, WMS, etc.).  Is ignored by GeoServer at this point.  */
		private String service = new String();


	 /**
		* Returns version of WFS requested.
		*
		* @return Version of WFS requested.
		*/
		public String getVersion() {
				return this.version;
		}


	 /**
		* Constructor with raw request string.  Calls parent.
		*
		* @return Service requested
		*/
		public String getService() {
				return this.service;
		}


	 /**
		* Sets version of WFS requested.
		*
		* @param version Version requested.
		*/
		public void setVersion(String version) {
				this.version = version;
		}


	 /**
		* Sets service requested.
		*
		* @param service Sets service requested.
		*/
		public void setService(String service) {
				this.service = service;
		}

}
