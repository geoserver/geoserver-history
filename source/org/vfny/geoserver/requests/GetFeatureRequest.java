/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

import org.vfny.geoserver.db.jdbc.SQLStatement;


/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.0 structured
 * XML docs.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 beta, 11/01/01
 *
 */
public class GetFeatureRequest {


		/** The maximum returned if the user requests no limit of features at all, but the other request parameters don't restrict to below 500 */
		protected static final int SOFT_MAX_FEATURES = 500;

		/** This is the maximum that is returned if the user specifically requests more than the soft max */
		protected static final int HARD_MAX_FEATURES = 1000;

		/** Creates a full list of queries */
		protected List queries = new Vector();

		// SHOULD CHANGE THIS TO GENERIC OUTPUT FORMAT, WITH STATIC FINAL POTENTIAL INPUTS
		/** Specifies the output format */
		protected String outputFormat = "GML2";

		/** Specifices the user-defined name for the entire get feature request */
		protected String handle = new String();

		// SUBCLASS OF UNIVERSAL REQUEST TYPE
		/** Specifies the type of request - should always be get feature */
		protected String request = new String();

		// SUBCLASS OF UNIVERSAL REQUEST TYPE
		/** Specifies the version of response requested */
		protected String version = new String();

		// I HAVE NO IDEA WHAT THIS DOES
		/** Creates an object version type */
		//protected String objVersion = new String();

		/** Creates a max features constraint for the entire request */
		protected int maxFeatures = SOFT_MAX_FEATURES;


	 /**
		* Empty constructor.
		*/ 
		public GetFeatureRequest() {
		}


	 /**
		* Sets the entire set of queries for this GetFeature request.
		*
		* @param queries The XML WFS GetFeature request.
		*/ 
		public void setQueries (List queries) {
				this.queries = queries;
		}


	 /**
		* Returns the entire set of queries for this GetFeature request.
		*
		*/ 
		public List getQueries () {
				return this.queries;
		}


	 /**
		* Returns the number of queries for this GetFeature request.
		*
		*/ 
		public int getQueryCount() {
				return this.queries.size();
		}


	 /**
		* Returns a specific query from this GetFeature request.
		*
		* @param i The query number to retrieve.
		*/ 
		public Query getQuery (int i) {
				return (Query) this.queries.get(i);
		}


	 /**
		* Returns a specific query from this GetFeature request.
		*
		* @param query The query number to retrieve.
		*/ 
		public void addQuery (Query query) {
				this.queries.add(query);
		}


	 /**
		* Parses the GetFeature reqeust and returns a contentHandler.
		*
		* @param maxFeatures The XML WFS GetFeature request.
		*/ 
		public void setMaxFeatures (int maxFeatures) {

				if( maxFeatures > 0 ) { 
						this.maxFeatures = maxFeatures;
				}

				if( maxFeatures > HARD_MAX_FEATURES ) {
						this.maxFeatures = HARD_MAX_FEATURES;
				}

		}


	 /**
		* Parses the GetFeature reqeust and returns a contentHandler.
		*
		* @param maxFeatures The XML WFS GetFeature request.
		*/ 
		public void setMaxFeatures (String maxFeatures) {

				if( maxFeatures != null ) {
						Integer tempInt = new Integer( maxFeatures );

						if( tempInt.intValue() > 0 ) { 
								this.maxFeatures = tempInt.intValue();
						}
						
						if( tempInt.intValue() > HARD_MAX_FEATURES ) {
								this.maxFeatures = HARD_MAX_FEATURES;
						}

				}

		}


	 /**
		* Sets the output format for this GetFeature request.
		*
		* @param outputFormat The output format for the GetFeature request.
		*/ 
		public void setOutputFormat (String outputFormat) {
				this.outputFormat = outputFormat;
		}


	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*/ 
		public String getOutputFormat () {
				return this.outputFormat;
		}


	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*
		* @param handle The user-defined handle.
		*/ 
		public void setHandle (String handle) {
				this.handle = handle;
		}


	 /**
		* Returns the user-defined name for the entire GetFeature request.
		*
		*/ 
		public String getHandle () {
				return this.handle;
		}


	 /**
		* Returns the version for the entire GetFeature request.
		*
		* @param version The WFS version of the response that the user expects.
		*/ 
		public void setVersion (String version) {
				this.version = version;
		}


	 /**
		* Sets the request for the given GetFeature..
		*
		* @param request The request type that the user expects.
		*/ 
		public void setRequest (String request) {
				this.request = request;
		}


	 /**
		* Returns the maximum number of features for this request.
		*
		*/ 
		public int getMaxFeatures () {
				return this.maxFeatures;
		}

}
