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
 * @version 0.9 beta, 11/01/01
 *
 */
public class GetCapabilitiesHandler extends GetCapabilitiesRequest implements ContentHandler {


		/** Local tracking variable for current tag */
		private String insideTag = new String();

		/** Local tracking variable to determine whether or not XML stream is insie a type name */
		private boolean insideTypeName = false;

		/** Standard logging instance */
		private Category _log = Category.getInstance(GetCapabilitiesHandler.class.getName());

		/** Return list of queries, which are all SQLQuery objects */
		private Vector featureTypes = new Vector();


		/** Notes the document locator.	*/ 
		public void setDocumentLocator (Locator locator) {
		}


		/** Notes the document start.	*/ 
		public void startDocument()
				throws SAXException {
		}


		/** Notes the document end.	*/ 
		public void endDocument()
				throws SAXException {
		}


		/** Notes the document processing instructions.	*/ 
		public void processingInstruction(String target, String data)
				throws SAXException {
		}


		/** Notes prefix mapping start.	*/ 
		public void startPrefixMapping(String prefix, String uri) {
		}


		/** Notes prefix mapping end.	*/ 
		public void endPrefixMapping(String prefix) {
		}


	 /**
		* Notes the start of the element and sets version and service tags, as required.
		*
		* @param namespaceURI URI for namespace appended to element.
		* @param localName Local name of element.
		* @param rawName Raw name of element.
		* @param atts Element attributes.
		*/ 
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


		/** Notes the document end element.	*/ 
		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {
		}


		/** Notes the document characters.	*/ 
		public void characters(char[] ch, int start, int length)
				throws SAXException {
		}


		/** Notes the ignorable whitespace.	*/ 
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}


		/** Notes the skipped entities.	*/ 
		public void skippedEntity(String name)
				throws SAXException {
		}

}
