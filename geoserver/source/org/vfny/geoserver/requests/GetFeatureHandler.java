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
public class GetFeatureHandler extends GetFeatureRequest implements ContentHandler {


		/** standard logging class */
		static Category _log = Category.getInstance(GetFeatureHandler.class.getName());


		// **********************************
		// Local tracking methods to deal with incoming XML stream

		/** Tracks tag we are currently inside */
		private String insideTag = new String();

		/** Boolean to flag whether or not we are inside a query */
		private boolean insideQuery = false;

		/** Boolean to flag whether or not we are inside a bounding box */
		private boolean insideBoundingBox = false;

		// **********************************

		/** Tracks current query */
		private Query currentQuery = new Query();

		/** Tracks current bounding box */
		private BoundingBox currentBoundingBox = new BoundingBox();


		// ********************************************
		// Start of SAX Content Handler Methods
		//  most of these are unused at the moment
		//  no namespace awareness, yet


		public void setDocumentLocator (Locator locator) {
		}


		public void startDocument()
				throws SAXException {
				//_log.info("start of document");
		}


		public void endDocument()
				throws SAXException {
				//_log.info( "at end of document");
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

				// if at a query element, empty the current query, set insideQuery flag, and get query typeNames
				if( insideTag.equals("Query") ) {

						currentQuery.empty();  // needs to be dereferenced?  causes problems for arraylist of queries
						insideQuery = true;

						for( int i=0; i < atts.getLength(); i++ ) {

								if( atts.getLocalName(i).equals("typeName") ) {
										//_log.info("found typename: " + atts.getValue(i));
										currentQuery.setFeatureTypeName( atts.getValue(i) );
								}
						}
				}

				if( insideTag.equals("GetFeature") ) {
						
						for( int i=0; i < atts.getLength(); i++ ) {
								if( atts.getLocalName(i).equals("maxFeatures") ) {
										//_log.info("found max features: " + atts.getValue(i));
										this.setMaxFeatures( atts.getValue(i) );
								}
						}
				}

				// check to see if inside bounding box and set to nothing, if true
				if( insideTag.equals("BoundingBox") ) {

						currentBoundingBox.setCoordinates("");  // needs to be dereferenced?  causes problems for arraylist of queries
						insideBoundingBox = true;
				}

		}

		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {

				//_log.info("at end element: " + localName);

				// as we leave query, set insideTag to "NULL" (otherwise the stupid characters method picks up external chars)
				insideTag = "NULL";

				// set insideQuery flag as we leave the query and add the query to the return list
				if( localName.equals("Query") ) {
						insideQuery = false;
						queries.add( currentQuery );
						//currentQuery.empty();  // this method should probably occur here, but then it dereferences the list element
				}

				// set insideQuery flag as we leave the bounding box and add the bounding box to the return list
				if( localName.equals("BoundingBox") ) {
						insideBoundingBox = false;
						//boundingBoxes.add( currentBoundingBox );
						//currentQuery.empty();  // this method should probably occur here, but then it dereferences the list element
				}

		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {

				String s = new String(ch, start, length);

				// if inside a property element, add the element
				if( insideTag.equals("PropertyName") ) {
						//_log.info("found prop name: " + s);
						currentQuery.addPropertyName(s);
				}

				// if inside chain-generated (by PredicateFilter) SQLPredicate tag
				// check to make sure we are inside a query
				// if so, set this predicate for the current query
				// if outside a current query, add this predicate to all predicates
				if( insideTag.equals("SQLPredicate") ) {
						if( insideQuery )
								currentQuery.getFilter().setSQL(s);
						else {
								if( this.getQueryCount() > 0 ) {
										for( int i = 0; i < this.getQueryCount(); i++ ) {
												//_log.info("found total predicate: " + s);
												this.getQuery(i).getFilter().setSQL( this.getQuery(i).getFilter() + " AND " + s );
										}
								}
						}
				}
	
				if( insideTag.equals("BoundingBox") ) {
						if( insideBoundingBox ) {
								//_log.info("found bbox: " + s);
								currentBoundingBox.setCoordinates(s);
						}
				}
		}

		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		public void skippedEntity(String name)
				throws SAXException {
		}

}
