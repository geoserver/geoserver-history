/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.db;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.types.*;


/**
 * Defines a base class for the GetFeature GML response.
 *
 * <p>This class acts as a "smart" string buffer in which to hold the final output and
 * spends most of its time calling subclasses for attributes.  It _must_ be used as a 
 * base class for all supported datastores.  The convention for naming subclasses is
 * to use the datastore name, followed by 'GML.'  For example, for PostGIS, the subclass
 * is called "PostgisGML."  Generally, subclasses should handle all geography-specific
 * GML responses.</p>
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 alpha, 11/01/01
 *
 */
public class GetFeatureGML {


		/** XML fragment **/
		private static final String XML1 = "\n <featureMember";

		/** XML fragment **/
		private static final String XML2 = "\">";

		/** XML fragment **/
		private static final String XML3 = "\n  <";

		/** XML fragment **/
		private static final String XML4 = ">";

		/** XML fragment **/
		private static final String XML5 = "\n </featureMember>";

		/** XML fragment **/
		private static final String XML6 = "\n  </";

		/** XML fragment **/
		private static final String XML7 = " fid=\"";

		/** feature type for this response **/
		protected String featureType;

		/** spatial reference for this response **/
		protected String srs = "911";

		/** final output for this response **/
		protected StringBuffer finalResult = new StringBuffer(20000);

		/** standard logging class **/
		private Category _log = Category.getInstance(GetFeatureGML.class.getName());


	 /**
		* Constructor to set feature type and SRID.
		*
		* @param featureType The GML feature type name.
		* @param srs The SRS name.
		*/ 
		public GetFeatureGML(String featureType, String srs) {
				this.srs = srs;
				this.featureType = featureType;
		}


	 /**
		* Simple method to add an attribute.
		*
		* @param name The attribute name.
		* @param value The attribute value.
		*/ 
		public void addAttribute (String name, String value) {

				finalResult.append("\n   <").append(this.featureType).append(".").append(name).append(">").append(value).append("</").append(this.featureType).append(".").append(name).append(">");

		}


	 /**
		* Add XML to start a feature.
		*
		* @param fid Unique feature identifier.
		*/ 
		public void startFeature (String fid) {

				finalResult.append(XML1).append(XML4).append(XML3).append(this.featureType).append(XML7).append(fid).append(XML2);

		}


	 /**
		* Add XML to end a feature.
		*/ 
		public void endFeature () {

				finalResult.append(XML6).append(this.featureType).append(XML4).append(XML5);

		}


	 /**
		* Return final GML object.
		*/ 
		public String getGML () {

				return finalResult.toString();

		}


}
