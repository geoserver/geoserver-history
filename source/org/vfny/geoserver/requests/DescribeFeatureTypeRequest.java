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
public class DescribeFeatureTypeRequest extends Request {

		// internal tokenizer for raw coordinate string
		protected boolean allRequested = true;
		protected Vector featureTypes = new Vector();

	 /**
		* Empty constructor.
		*/
		public DescribeFeatureTypeRequest() {
				super();
		}

	 /**
		* Return request type.
		*/
		public String getRequest() {
				return "DESCRIBEFATURETYPE";
		}

	 /**
		* Return boolean for all requested types.
		*/
		public boolean allRequested() {
				return this.allRequested;
		}

	 /**
		* Return requested feature types.
		*/
		public void setFeatureTypes(Vector featureTypes) {
				this.featureTypes = featureTypes;
				this.allRequested = false;
		}

	 /**
		* Set requested feature types..
		*/
		public Vector getFeatureTypes() {
				return this.featureTypes;
		}


}
