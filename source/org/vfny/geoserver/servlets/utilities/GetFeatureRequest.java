/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets.utilities;

import java.io.*;
import java.util.*;

/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.0 structured
 * XML docs.
 *
 *@author Vision for New York
 *@author Rob Hranac
 *@version 0.9 alpha, 11/01/01
 *
 */
public class GetFeatureRequest {

		// Establishes clean request object, which processes getFeature client request
		private List queries = Collections.synchronizedList(new ArrayList());
		private List boundingBoxes = Collections.synchronizedList(new ArrayList());

		private String outputFormat = "GML2";
		private String handle = new String();
		private String request = new String();
		private String version = new String();
		private String maxFeatures = new String();
		private String objVersion = new String();

	 /**
		* Empty constructor.
		*/ 
		public GetFeatureRequest() {
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*
		* @param outputFormat The XML WFS GetFeature request.
		*/ 
		public void setQueries (List queries) {
				this.queries = queries;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*/ 
		public List getQueries () {
				return this.queries;
		}


	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*
		* @param outputFormat The XML WFS GetFeature request.
		*/ 
		public void setOutputFormat (String outputFormat) {
				this.outputFormat = outputFormat;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*
		* @param outputFormat The XML WFS GetFeature request.
		*/ 
		public String getHandle () {
				return this.handle;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*
		* @param outputFormat The XML WFS GetFeature request.
		*/ 
		public void setMaxFeatures (String maxFeatures) {
				this.maxFeatures = maxFeatures;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*/ 
		public String getOutputFormat () {
				return this.outputFormat;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*/ 
		public void setHandle (String handle) {
				this.handle = handle;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*/ 
		public void setVersion (String version) {
				this.version = version;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*/ 
		public void setRequest (String request) {
				this.request = request;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*/ 
		public String getMaxFeatures () {
				return this.maxFeatures;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*/ 
		public List getBoundingBoxes () {
				return this.boundingBoxes;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*/ 
		public void setBoundingBoxes (List boundingBoxes) {
				this.boundingBoxes = boundingBoxes;
		}


}
