/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;


/**
 * Defines a general Request type and provides accessor methods for unversal request information.
 * 
 * @author Rob Hranac, Vision for New York
 * @version beta, 12/01/01
 *
 */
abstract public class Request {


		/** Request version */
		protected String version = new String();

		/** Request type */
		protected String request = new String();


	 /**
		* Empty constructor.
		*
		*/
		public Request () {
		}


	 /**
		* Returns request type.
		*
		* @return Request type.
		*/
		public String getRequest () {
				return this.request;
		}


	 /**
		* Return request type.
		*
		* @param request Sets the request type.
		*/
		public void setRequest (String reqeust) {
				this.request = request;
		}


	 /**
		* Return version type.
		*
		* @return Version of request.
		*/
		public String getVersion () {
				return this.version;
		}


	 /**
		* Sets version type.
		*
		* @param version Version of request.
		*/
		public void setVersion (String version) {
				this.version = version;
		}

}
