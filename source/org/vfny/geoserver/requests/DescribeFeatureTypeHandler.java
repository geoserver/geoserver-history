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
 * @version 0.9 beta, 11/01/01
 *
 */
class DescribeFeatureTypeHandler extends DescribeFeatureTypeRequest implements ContentHandler {


		/** Standard logging class */
		static Category _log = Category.getInstance(DescribeFeatureTypeHandler.class.getName());

		/** Local variable to track current tag */
		private String currentTag = new String();


		/*********************************************
		 Start of SAX Content Handler Methods
		  most of these are unused at the moment
		  no namespace awareness, yet
		*********************************************/


		/** Notes the document locator.	*/ 
		public void setDocumentLocator (Locator locator) {}


		/** Notes the start of the document.	*/ 
		public void startDocument()
				throws SAXException {}


		/** Notes the start of the document.	*/ 
		public void endDocument()
				throws SAXException {}


		/** Notes processing instructions.	*/ 
		public void processingInstruction(String target, String data)
				throws SAXException {}


		/** Notes start of prefix mappings. */ 
		public void startPrefixMapping(String prefix, String uri) {}


		/** Notes end of prefix mappings. */ 
		public void endPrefixMapping(String prefix) {}


	 /**
		* Notes the start of the element and sets the current tag.
		*
		* @param namespaceURI URI for namespace appended to element.
		* @param localName Local name of element.
		* @param rawName Raw name of element.
		* @param atts Element attributes.
		*/ 
		public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
				throws SAXException {
				
				currentTag = localName;
		}


	 /**
		* Notes the end of the element and sets the current tag.
		*
		* @param namespaceURI URI for namespace appended to element.
		* @param localName Local name of element.
		* @param rawName Raw name of element.
		*/ 
		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {

				currentTag = "";
		}


	 /**
		* Checks if inside type name and adds to feature type list, if so.
		*
		* @param ch URI for namespace appended to element.
		* @param start Local name of element.
		* @param length Raw name of element.
		*/ 
		public void characters(char[] ch, int start, int length)
				throws SAXException {

				String s = new String(ch, start, length);
				if( currentTag.equals("TypeName") ) {
						featureTypes.add(s);
				}
		}


	 /**
		* Notes ignorable whitespace.
		*
		* @param ch URI for namespace appended to element.
		* @param start Local name of element.
		* @param length Raw name of element.
		*/ 
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}


	 /**
		* Notes skipped entity.
		*
		* @param name Name of skipped entity.
		*
		*/ 
		public void skippedEntity(String name)
				throws SAXException {}

}
