/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.xerces.parsers.SAXParser;

import org.apache.log4j.Category;

import org.vfny.geoserver.db.jdbc.*;

/**
 * Uses SAX to extact a GetFeature query from and incoming GetFeature request XML stream.
 *
 * <p>Note that this Handler extension ignores Filters completely and must be chained
 * as a parent to the PredicateFilter method in order to recognize them.  If it is not
 * chained, it will still generate valid queries, but with no filtering whatsoever.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version 0.9 alpha, 11/01/01
 *
 */
public class GetCapabilitiesHandler extends GetCapabilitiesRequest implements ContentHandler {

		// local tracking methods to deal with incoming XML stream
		private String insideTag = new String();
		private boolean insideTypeName = false;

		private Category _log = Category.getInstance(GetCapabilitiesHandler.class.getName());

		// the return list of queries, which are all SQLQuery objects
		private Vector featureTypes = new Vector();


		public void setDocumentLocator (Locator locator) {
		}

		public void startDocument()
				throws SAXException {
		}

		public void endDocument()
				throws SAXException {
		}

		public void processingInstruction(String target, String data)
				throws SAXException {
		}

		public void startPrefixMapping(String prefix, String uri) {
		}

		public void endPrefixMapping(String prefix) {
		}

		public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
				throws SAXException {

				// if at a query element, empty the current query, set insideQuery flag, and get query typeNames
				if( localName.equals("GetCapabilities") ) {

						for( int i=0; i < atts.getLength(); i++ ) {
								if( atts.getLocalName(i).equals("version") ) {
										this.setVersion( atts.getValue(i) );
								}
								if( atts.getLocalName(i).equals("service") ) {
										this.setService( atts.getValue(i) );
								}
						}
				}

		}

		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
		}

		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		public void skippedEntity(String name)
				throws SAXException {
		}

}
