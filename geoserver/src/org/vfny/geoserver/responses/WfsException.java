/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.responses;

import java.util.*;

import org.apache.log4j.Category;


/**
 * This utility defines a general Request type and provides accessor methods for unversal request information.
 * 
 * @author Rob Hranac,Vision for New York
 * @version alpha, 12/01/01
 *
 */
public class WfsException extends Exception {

		/** message inserted by GeoServer as to what it thinks happened */
		protected String preMessage = new String();

		/** locator inserted by GeoServer; full classpath of originating GeoServer class */
		protected String locator = new String();

		/** the standard exception that was thrown */
		protected Exception standardException = new Exception();

		/** standard logging instance for class */
		private static Category _log = Category.getInstance(WfsException.class.getName());
		

	 /**
		* Empty constructor.
		*/
		public WfsException () {
				super();
		}


	 /**
		* Empty constructor.
		*
		* @param message The message for the .
		*/
		public WfsException (String message) {
				super( message );
				_log.info( this.getMessage() );
		}


	 /**
		* Empty constructor.
		*
		* @param message The message for the .
		*/
		public WfsException (Exception e) {
				super( e.getMessage() );
				_log.info( this.getMessage() );				
		}


	 /**
		* Empty constructor.
		*
		* @param message The message for the .
		* @param locator The message for the .
		*/
		public WfsException (String message, String locator) {
				super(message);
				this.locator = locator;
				_log.info( "> [" + this.locator + "]:\n  " + this.getMessage() );				
		}


	 /**
		* Empty constructor.
		*
		* @param message The message for the .
		* @param locator The message for the .
		*/
		public WfsException (Exception e, String preMessage, String locator) {
				super( e.getMessage() );
				this.preMessage = preMessage;				
				this.locator = locator;
				_log.info( "> [" + this.locator + "]:\n  " + this.preMessage + ": " + this.getMessage() );				
		}


	 /**
		* Return request type.
		*/
		public String getXmlResponse () {
				String returnXml;

				returnXml = "<WFS_Exception>\n";
				returnXml = returnXml + "   <Exception>\n";
				returnXml = returnXml + "      <Locator>" + this.locator + "</Locator>\n";
				returnXml = returnXml + "      <Message>" + this.preMessage + ": " + this.getMessage() + "</Message>\n";
				returnXml = returnXml + "   </Exception>\n";
				returnXml = returnXml + "</WFS_Exception>\n";
				
				return returnXml;
		}

}
