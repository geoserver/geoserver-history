/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
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

		// version and string stores
		private String version = new String();
		private String service = new String();


		public String getVersion() {
				return this.version;
		}


		public String getService() {
				return this.service;
		}


		public void setVersion(String version) {
				this.version = version;
		}


		public void setService(String service) {
				this.service = service;
		}

}
