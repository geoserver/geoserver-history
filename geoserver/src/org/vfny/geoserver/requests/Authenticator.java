/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;


/**
 * Assists in the authentication of IP addresses by this server. 
 *
 * @author Rob Hranac, Vision for New York
 * @version $0.9 beta, 11/01/01$
 */
public class Authenticator {


		// THIS CLASS IS TOTALLY UNIMPLEMENTED
		// JUST A SMALL EXPERIMENT THAT WILL LATER BE INTEGRATED

		/** Standard logging instance for the class */
		private Category _log = Category.getInstance( Authenticator.class.getName() );

		/** Creates a list of feature types allowed for each IP */
		private Hashtable featureTypes = new Hashtable();

		/** Creates a list of hosts allowed for this server installation */
		private Hashtable allowedHosts = new Hashtable();
		

	 /**
		* Enpty constuctor.
		*
		*/ 
		public Authenticator() {
				allowedHosts.put( "10.254.0.29", "ALLOWED");
				
		}


	 /**
		* Checks to see if a given IP is allowed to access the layers on this server.
		*
		* @param ipAddress The servlet request object.
		*/
		public boolean isAllowed(String ipAddress) {
				if( allowedHosts.containsKey(ipAddress) ) {
						return true;
				}
				else {
						return false;
				}
		}


	 /**
		* Checks to see if a given IP is allowed to access specific feature types on this server.
		*
		* @param ipAddress The servlet request object.
		* @param featureType The servlet response object.
		*/
		public boolean isAllowed(String ipAddress, String featureType) {
				//((Hashtable) allowedHosts) = featureTypes.get(featureType);
				if( allowedHosts != null ) {
						if( allowedHosts.containsKey(ipAddress) ) {
								return true;
						}
						else
								return false;
				}
				else
						return false;

		}

}


