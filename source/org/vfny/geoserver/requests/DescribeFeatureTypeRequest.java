/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;


/**
 * This utility defines a general Request type and provides accessor methods for unversal request information.
 * 
 * @author Rob Hranac, Vision for New York
 * @version alpha, 12/01/01
 */
public class DescribeFeatureTypeRequest extends Request {


		/** Flags whether or not all feature types were requested */
		protected boolean allRequested = true;

		/** Stores all feature types */
		protected Vector featureTypes = new Vector();


	 /**
		* Empty constructor.
		*
		*/
		public DescribeFeatureTypeRequest() {
				super();
		}


	 /**
		* Return request type.
		*
		*/
		public String getRequest() {
				return "DESCRIBEFATURETYPE";
		}


	 /**
		* Return boolean for all requested types.
		*
		*/
		public boolean allRequested() {
				return this.allRequested;
		}


	 /**
		* Set requested feature types..
		*
		*/
		public void setFeatureTypes(Vector featureTypes) {
				this.featureTypes = featureTypes;
				this.allRequested = false;
		}


	 /**
		* Return requested feature types.
		*
		*/
		public Vector getFeatureTypes() {
				return this.featureTypes;
		}


}
