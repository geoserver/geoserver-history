/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import org.apache.xerces.parsers.SAXParser;
import org.apache.log4j.Category;

import org.vfny.geoserver.responses.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.config.*;


/**
 * This utility reads in a DescribeFeatureType KVP request and returns a DescribeFeatureType request object.
 * 
 * <p>If you pass this utility a KVP request (everything after the '?' in the URI),
 * it will translate this into a list of feature types, encapsulated in a DescribeFeatureType request object.
 * Note that you must check for validity before passing the request.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class GetCapabilitiesReaderXml {


		/** Standard logging instance for class. */
		private static Category _log = Category.getInstance(GetCapabilitiesReaderXml.class.getName());

		/** GetCapabilities request object. */
		private GetCapabilitiesHandler currentRequest;


	 /**
		* Constructor with raw request string.  Calls parent.
		*
		* @param rawRequest The raw request string from the client.
		*/
		public GetCapabilitiesReaderXml (BufferedReader rawRequest)
				throws WfsException {
				
				//InputSource requestSource = new InputSource((Reader) tempReader);
				InputSource requestSource = new InputSource(rawRequest);

				// instantiante parsers and content handlers
				XMLReader parser = new SAXParser();
				currentRequest = new GetCapabilitiesHandler();

				// read in XML file and parse to content handler
				try {
						parser.setContentHandler(currentRequest);
						parser.parse(requestSource);
				}
				catch (SAXException e) {
						throw new WfsException( e, "XML get capabilities request parsing error", GetCapabilitiesReaderXml.class.getName() );
				}
				catch (IOException e) {
						throw new WfsException( e, "XML get capabilities request input error", GetCapabilitiesReaderXml.class.getName() );
				}

		}


	 /**
		* Returns a GetCapabilities request object (i.e. list of requested feature types).
		*
		*/
		public GetCapabilitiesRequest getRequest () {
				return (GetCapabilitiesRequest) currentRequest;
		}

}
