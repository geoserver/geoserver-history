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
import org.vfny.geoserver.config.*;


/**
 * This utility reads in a DescribeFeatureType XML request and turns it into a list of requested Feature Types.
 * 
 * <p>If you pass this utility an XML request, it will call the appropriate SAX handlers 
 * to interpret it and translate it into a describe feature type request object, to pass
 * to the describe feature type response. Note that you must check for validity
 * before passing the request.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version beta, 12/01/01
 *
 */
public class DescribeFeatureTypeReaderXml {


		/** create standard logging instance for class */
		private static Category _log = Category.getInstance(DescribeFeatureTypeReaderXml.class.getName());

		/** create a describe feature type request class to return */
		DescribeFeatureTypeHandler contentHandler = new DescribeFeatureTypeHandler();


	 /**
		* Constructor with raw request buffer.
		*
		* @param describeFeatureTypeRequest The raw request string from the client.
		*/
		public DescribeFeatureTypeReaderXml (BufferedReader rawRequest) 
				throws WfsException {
				
				/** create a describe feature type request class to return */
				InputSource requestSource = new InputSource(rawRequest);

				/** instantiates a generic SAX parser to read the request */
				XMLReader parser = new SAXParser();
				
				// read in XML file and parse to content handler
				try {
						parser.setContentHandler(contentHandler);
						parser.parse(requestSource);
				}
				catch (SAXException e) {
						throw new WfsException( e, "XML describe feature type request parsing error", DescribeFeatureTypeReaderXml.class.getName() );
				}
				catch (IOException e) {
						throw new WfsException( e, "XML describe feature type input error", DescribeFeatureTypeReaderXml.class.getName() );
				}

		}


	 /**
		* Return the describe feature type request object.
		*
		*/
		public DescribeFeatureTypeRequest getRequest () {
				return (DescribeFeatureTypeRequest) contentHandler;
		}

}
