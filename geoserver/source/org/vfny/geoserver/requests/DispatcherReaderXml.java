/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.xerces.parsers.SAXParser;

import org.apache.log4j.Category;

import org.vfny.geoserver.responses.*;
import org.vfny.geoserver.config.*;


/**
 * This utility reads in a DescribeFeatureType KVP request and turns it into a list of requested Feature Types.
 * 
 * <p>If you pass this utility a KVP request (everything after the '?' in the URI),
 * it will translate this into a list of feature types.  Note that you must check for validity
 * before passing the request.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version 1
 *
 */
public class DispatcherReaderXml {


		/** create standard logging instance for class */
		private static Category _log = Category.getInstance(DispatcherReaderXml.class.getName());

		/** create standard logging instance for class */
		private DispatcherHandler currentRequest;


	 /**
		* Constructor with raw request string.  Calls parent.
		*
		* @param rawRequest The raw request string from the client.
		*/
		public DispatcherReaderXml (BufferedReader rawRequest)
				throws WfsException {
				
				//InputSource requestSource = new InputSource((Reader) tempReader);
				InputSource requestSource = new InputSource(rawRequest);

				// instantiante parsers and content handlers
				XMLReader parser = new SAXParser();
				currentRequest = new DispatcherHandler();

				// read in XML file and parse to content handler
				try {
						parser.setContentHandler(currentRequest);
						parser.parse(requestSource);
				}
				catch (SAXException e) {
						throw new WfsException( e, "XML get capabilities request parsing error", DispatcherReaderXml.class.getName() );
				}
				catch (IOException e) {
						throw new WfsException( e, "XML get capabilities request input error", DispatcherReaderXml.class.getName() );
				}

		}

	 /**
		* Returns a list of requested feature types..
		*
		*/
		public int getRequestType () {
				return currentRequest.getRequestType();
		}

}
