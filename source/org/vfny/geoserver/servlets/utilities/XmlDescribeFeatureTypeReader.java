/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.servlets.utilities;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;
import org.vfny.geoserver.xml.requests.*;
import org.vfny.geoserver.xml.utilities.*;
import org.vfny.geoserver.metadata.*;
import org.vfny.geoserver.servlets.utilities.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.xerces.parsers.SAXParser;
import org.vfny.geoserver.xml.requests.*;

/**
 * This utility reads in a DescribeFeatureType KVP request and turns it into a list of requested Feature Types.
 * 
 * <p>If you pass this utility a KVP request (everything after the '?' in the URI),
 * it will translate this into a list of feature types.  Note that you must check for validity
 * before passing the request.</p>
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version alpha, 12/01/01
 *
 */
public class XmlDescribeFeatureTypeReader {

		// create standard logging instance for class
		private static Category _log = Category.getInstance(XmlDescribeFeatureTypeReader.class.getName());

		private DescribeFeatureTypeRequest currentRequest = new DescribeFeatureTypeRequest();

	 /**
		* Constructor with raw request string.  Calls parent.
		*
		* @param describeFeatureTypeRequest The raw request string from the client.
		*/
		public XmlDescribeFeatureTypeReader (BufferedReader rawRequest) throws IOException {
				
				//InputSource requestSource = new InputSource((Reader) tempReader);
				InputSource requestSource = new InputSource(rawRequest);

				// instantiante parsers and content handlers
				XMLReader parser = new SAXParser();
				DescribeFeatureTypeHandlerImpl contentHandler = new DescribeFeatureTypeHandler();

				// read in XML file and parse to content handler
				try {
						parser.setContentHandler(contentHandler);
						parser.parse(requestSource);
				}
				catch (SAXException e) {
						System.out.println("Error in parsing: " + e.getMessage() );
				}
				catch (IOException e) {
						System.out.println("Input error: " + e.getMessage() );
				}

				_log.info("here is request source: " + currentRequest.getFeatureTypes().toString() );


				currentRequest.setFeatureTypes( contentHandler.getFeatureTypes() );
		}

	 /**
		* Returns a list of requested feature types..
		*
		*/
		public DescribeFeatureTypeRequest getRequest () {
				return currentRequest;
		}

}
