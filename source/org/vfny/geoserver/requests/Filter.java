/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.requests;

import java.util.*;


/**
 * Contains an OGC filter, implementing the 0.7 specification.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 beta, 11/01/01
 */
public class Filter {


		// NOTE THAT THIS IMPLEMENTATION IS A PLACEHOLDER FOR A MORE
		// SOPHISTICATED APPROACH THAT IS YET TO BE DETERMINED.
		// RIGHT NOW IT JUST WRAPS A STRING

		/** The user-specified name for the query. */
		private String internalRepresentation = new String();


	 /**
		* Empty constructor.
		*
		*/ 
		public Filter() {
		}


	 /**
		* Constructor with string representation of filter.
		*
		* @param internalRepresentation Filter as a string.
		*/ 
		public Filter(String internalRepresentation) {
				
				this.internalRepresentation = internalRepresentation;
		}


	 /**
		* Retrieves the SQL from the filter..
		*
		*/ 
		public String getSQL() {
				
				return this.internalRepresentation;
		}


	 /**
		* Sets the filter via a string SQL representation.
		*
		* @param sql Filter as a string.
		*/ 
		public void setSQL(String sql) {
				
				this.internalRepresentation = sql;
		}


		
}
