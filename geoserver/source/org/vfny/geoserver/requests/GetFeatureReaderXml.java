/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.xerces.parsers.SAXParser;

import org.apache.log4j.Category;

import org.vfny.geoserver.responses.*;
import org.vfny.geoserver.config.*;


/**
 * This utility reads in XML GetFeature request files and converts them into a generic request object.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 beta, 11/01/01
 *
 */
public class GetFeatureReaderXml {


		/** Standard logging instance for class */
		private static Category _log = Category.getInstance(GetFeatureReaderXml.class.getName());

		/** Response/SAX handler object for return */
		private GetFeatureHandler contentHandler = new GetFeatureHandler();


	 /**
		* Reads the GetFeature XML POST request into a GetFeature object.
		*
		* @param rawRequest The plain POST text from the client.
		*/ 
		public GetFeatureReaderXml(BufferedReader rawRequest) 
				throws WfsException {

				// translate string into a proper SAX input source
				InputSource requestSource = new InputSource(rawRequest);

				// instantiante parsers and content handlers
				XMLReader parser = new SAXParser();
				FilterBoundingBox filterParser = new FilterBoundingBox(parser);

				// read in XML file and parse to content handler
				try {
						filterParser.setContentHandler(contentHandler);
						filterParser.setParent(parser);
						filterParser.parse(requestSource);
				}
				catch (SAXException e) {
						throw new WfsException( e, "XML get feature request SAX parsing error", GetFeatureReaderXml.class.getName() );
				}
				catch (IOException e) {
						throw new WfsException( e, "XML get feature request input error", GetFeatureReaderXml.class.getName() );
				}

		}


	 /**
		* Returns GetFeatureRequest created by the constructor.
		*
		*/ 
		public GetFeatureRequest getRequest() {

				return (GetFeatureRequest) contentHandler;
		}

}
