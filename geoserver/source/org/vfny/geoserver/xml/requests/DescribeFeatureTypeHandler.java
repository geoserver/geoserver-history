/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.xml.requests;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.apache.xerces.parsers.SAXParser;
import org.vfny.geoserver.db.jdbc.*;
import org.xml.sax.helpers.*;


/**
 * Uses SAX to extact a GetFeature query from and incoming GetFeature request XML stream.
 *
 * <p>Note that this Handler extension ignores Filters completely and must be chained
 * as a parent to the PredicateFilter method in order to recognize them.  If it is not
 * chained, it will still generate valid queries, but with no filtering whatsoever.</p>
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class DescribeFeatureTypeHandler implements DescribeFeatureTypeHandlerImpl {

		// local tracking methods to deal with incoming XML stream
		private String insideTag = new String();
		private boolean insideTypeName = false;

		static Category _log = Category.getInstance(DescribeFeatureTypeHandler.class.getName());

		// the return list of queries, which are all SQLQuery objects
		private List featureTypes = Collections.synchronizedList(new ArrayList());

		public List getFeatureTypes() {
				return this.featureTypes;
		}

		public int getFeatureTypeCount() {
				return this.featureTypes.size();
		}

		public void setDocumentLocator (Locator locator) {
		}

		public void startDocument()
				throws SAXException {
				//_log.info("start of document");
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

				// at start of element, set insidetag flag to whatever tag we are inside
				insideTag = localName;

				//_log.info("at start element: " + localName);

				// if at a query element, empty the current query, set insideQuery flag, and get query typeNames
				if( insideTag.equals("FeatureType") ) {

						//_log.info("inside typeName");

						insideTypeName = true;
				}

		}

		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {

				// as we leave query, set insideTag to "NULL" (otherwise the stupid characters method picks up external chars)
				insideTag = "NULL";

				// set insideQuery flag as we leave the query and add the query to the return list
				if( localName.equals("FeatureType") ) {
						insideTypeName = false;
						//currentQuery.empty();  // this method should probably occur here, but then it dereferences the list element
				}
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {

				String s = new String(ch, start, length);

				// if inside a property element, add the element
				if( insideTypeName )
						featureTypes.add(s);

		}

		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		public void skippedEntity(String name)
				throws SAXException {
		}

}
