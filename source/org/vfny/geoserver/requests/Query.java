/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.util.*;

import org.vfny.geoserver.config.FeatureTypeBean;

/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.1 structured
 * XML docs.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 alpha, 11/01/01
 *
 */
public class Query {


		/** The user-specified name for the query. */
		protected String handle = new String();

		/** The feature type name requested. */
		protected String featureTypeName = new String();

		// UNIMPLEMENTED - YOU CAN SET THIS BUT IT DOES NOTHING
		/** The version of the feature to request - current implementation ignores entirely. */
		protected String version = new String();

		/** The property names requested */
		protected Vector propertyNames = new Vector();

		/** The filter for the query */
		protected Filter filter = new Filter();

		/** Stores datbase configuration meta data for the query  */
		protected FeatureTypeBean featureType = null;


	 /**
		* Empty constructor.
		*
		*/ 
		public Query() {
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param rawRequest The plain POST text from the client.
		*/ 
		public void empty() {
				this.featureTypeName = "";
				this.handle = "";
				this.propertyNames.clear();
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param featureTypeName The plain POST text from the client.
		*/ 
		public void setDatastoreConfiguration() {
				this.featureType = new FeatureTypeBean( featureTypeName );
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public FeatureTypeBean getDatastoreConfiguration() {

				return this.featureType;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public Vector getPropertyNames() {

				return this.propertyNames;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param featureTypeName The plain POST text from the client.
		*/ 
		public void setFeatureTypeName(String featureTypeName) {
				this.featureTypeName = featureTypeName;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param rawRequest The plain POST text from the client.
		*/ 
		public String getFeatureTypeName() {
				return this.featureTypeName;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param rawRequest The plain POST text from the client.
		*/ 
		public void addPropertyName(String propertyName) {
				this.propertyNames.add(propertyName);
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param rawRequest The query handle, or client-specified name.
		*/ 
		public void setHandle (String handle) {
				this.handle = handle;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public String getHandle() {
				return this.handle;
		}
		

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param version The specified version.
		*/ 
		public void setVersion (String version) {
				this.version = version;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public String getVersion() {
				return this.version;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param filter The specified filter.
		*/ 
		public void setFilter (Filter filter) {
				this.filter = filter;
		}


	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public Filter getFilter() {
				return this.filter;
		}
		

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public int getDatastoreType() {
				return 1;
		}
		

}
