/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.util.*;


/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.1 structured
 * XML docs.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 alpha, 11/01/01
 *
 */
public class BoundingBox {


		/** The user-specified name for the query. */
		private String internalRepresentation = new String();


	 /**
		* Empty constructor.
		*
		*/ 
		public BoundingBox() {
		}


	 /**
		* Empty constructor.
		*
		*/ 
		public BoundingBox(String internalRepresentation) {
				
				this.internalRepresentation = internalRepresentation;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param rawRequest The plain POST text from the client.
		*/ 
		public String getSQL() {
				
				return this.internalRepresentation;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public void setCoordinates(String internalRepresentation) {
				
				this.internalRepresentation = internalRepresentation;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public String getCoordinates() {
				
				return this.internalRepresentation;
		}


		
}
