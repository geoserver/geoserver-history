/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.io.*;


/**
 * Contains an abstraction of each potential SQL predicate element.
 *
 * @author Rob Hranac, Vision for New York
 * @version $0.9 beta, 11/01/01$
 */
public class Operator {


		/** Name of element according to OGC filter spec */
		private String name = new String();

		/** Type of element according to how it must be handled */
		private String type = new String();

		/** SQL code for translation */
		private String SQL = new String();


	 /**
		* Empty constructor.
		*/ 
		public Operator () {
		}


	 /**
		* Predicate element constructor.
		*
		* @param name The name of element according to OGC filter spec.
		* @param type The type of element according to how it must be handled.
		* @param name The SQL code for translation.
		*/ 
		public Operator (String name, String type, String SQL) {
				this.name = name;
				this.type = type;
				this.SQL = SQL;
		}


	 /** Returns element name. */ 
		public String getName () { return this.name;	}

	 /** Returns element type. */ 
		public String getType () { return this.type; }

	 /** Returns SQL coding for element. */ 
		public String getSQL () { return this.SQL; }


	 /**
		* Set element name.
		*
		* @param name The name of element according to OGC filter spec.
		*/ 
		public void setName (String name) {
				this.name = name;
		}


	 /**
		* Set element type.
		*
		* @param name The element type.
		*/ 
		public void setType (String type) {
				this.type = type;
		}


	 /**
		* Set element name.
		*
		* @param SQL The SQL coding for the element.
		*/ 
		public void setSQL (String SQL) {
				this.SQL = SQL;
		}

}
