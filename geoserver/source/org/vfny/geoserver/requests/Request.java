/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

/**
 * This utility defines a general Request type and provides accessor methods for unversal request information.
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version alpha, 12/01/01
 *
 */
abstract public class Request {

		// internal tokenizer for raw coordinate string
		protected String version = new String();
		protected String request = new String();

	 /**
		* Empty constructor.
		*
		*/
		public Request () {
		}

	 /**
		* Return request type.
		*/
		public String getRequest () {
				return this.request;
		}

	 /**
		* Return request type.
		*/
		public void setRequest (String reqeust) {
				this.request = request;
		}

	 /**
		* Return version type.
		*/
		public String getVersion () {
				return this.version;
		}

	 /**
		* Return version type.
		*/
		public void setVersion (String version) {
				this.version = version;
		}

}
