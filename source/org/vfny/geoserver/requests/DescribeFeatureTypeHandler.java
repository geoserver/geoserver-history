/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.apache.xerces.parsers.SAXParser;

import org.apache.log4j.Category;


/**
 * Uses SAX to extact a DescribeFeatureType query from and incoming GetFeature request XML stream.
 *
 * <p>Note that this Handler extension ignores Filters completely and must be chained
 * as a parent to the PredicateFilter method in order to recognize them.  If it is not
 * chained, it will still generate valid queries, but with no filtering whatsoever.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version 0.9 alpha, 11/01/01
 *
 */
class DescribeFeatureTypeHandler extends DescribeFeatureTypeRequest implements ContentHandler {

		/** standard logging class */
		static Category _log = Category.getInstance(DescribeFeatureTypeHandler.class.getName());

		/** local variable to track current tag */
		private String currentTag = new String();


		// ********************************************
		// Start of SAX Content Handler Methods
		//  most of these are unused at the moment
		//  no namespace awareness, yet


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
				
				currentTag = localName;
		}

		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {

				currentTag = "";
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {

				String s = new String(ch, start, length);
				if( currentTag.equals("TypeName") ) {
						featureTypes.add(s);
				}
		}

		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		public void skippedEntity(String name)
				throws SAXException {
		}

}
