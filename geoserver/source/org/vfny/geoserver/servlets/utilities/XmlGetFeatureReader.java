/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets.utilities;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;
import org.vfny.geoserver.xml.utilities.*;
import org.vfny.geoserver.metadata.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.xerces.parsers.SAXParser;
import org.vfny.geoserver.xml.requests.*;


/**
 * This utility reads in XML GetFeature request files and converts them into a generic request object.
 *
 *@author Vision for New York
 *@author Rob Hranac
 *@version 0.9 alpha, 11/01/01
 *
 */
public class XmlGetFeatureReader extends GetFeatureRequest {

		// create standard logging instance for class
		static Category _log = Category.getInstance(XmlGetFeatureReader.class.getName());

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*/ 
		public XmlGetFeatureReader(BufferedReader rawRequest) {

				// translate string into a proper SAX input source
				InputSource requestSource = new InputSource(rawRequest);

				//_log.info("got to reader start");
				// instantiante parsers and content handlers
				XMLReader parser = new SAXParser();
				BoundingBoxFilter filterParser = new BoundingBoxFilter(parser);
				GetFeatureHandlerImpl contentHandler = new GetFeatureHandler();

				// read in XML file and parse to content handler
				try {
						//_log.info("in front of content handler");
						filterParser.setContentHandler(contentHandler);
						//_log.info("in front of filter");
						filterParser.setParent(parser);
						//_log.info("in front of parse");
						filterParser.parse(requestSource);
						//_log.info("after parse");
				}
				catch (SAXException e) {
						System.out.println("Error in parsing: " + e.getMessage() );
				}
				catch (IOException e) {
						System.out.println("Input error: " + e.getMessage() );
				}


				//_log.info("before reading queries");

				// Reads the XML request, generates an associated response, and returns the response to the client 
				super.setQueries( contentHandler.getQueries() );

				//_log.info("here is first query: " + contentHandler.getSQL());

				super.setBoundingBoxes( contentHandler.getBoundingBoxes() );

				//_log.info("here is first bbox: " + contentHandler.getBoundingBoxes().get(0));

		}

}
