/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;
import java.sql.*;


/**
 * Printer for XML schema tags.
 *
 * 
 *@author Rob Hranac, Vision for New York
 *@version 0.9 beta, 11/01/01
 *
 */
public class XmlTag {


		/** XML schema tag name */
		private String name = new String();

		/** XML schema name */
		private String schema = new String(); 


	 /**
		* Creates an new xml tag with a specific name and schema.
		*
		* @param name XML schema tag name.
		* @param schema XML schema name.
		*/ 
		public XmlTag  (String name, String schema) {
				this.name = name;
				this.schema = schema;
    }


	 /**
		* Prints XML start tag.
		*
		*/ 
		public String start () {
				return "<" + schema + ":" + name + ">\n";
		}


	 /**
		* Prints XML end tag.
		*
		*/ 
		public String end () {
				return "</" + schema + ":" + name + ">\n";
		}


	 /**
		* Prints XML only tag.
		*
		*/ 
		public String only () {
				return "<" + schema + ":" + name + "/>\n";
		}


}
