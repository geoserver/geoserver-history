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
public class GetFeatureHandler implements GetFeatureHandlerImpl {

		// local tracking methods to deal with incoming XML stream
		private String insideTag = new String();

		private List queries = Collections.synchronizedList(new ArrayList());
		private SQLStatement currentQuery = new SQLStatement("SELECT");
		private boolean insideQuery = false;

		private List boundingBoxes = Collections.synchronizedList(new ArrayList());
		private String currentBoundingBox = new String("1002221 193190, 1002229, 193300");
		private boolean insideBoundingBox = false;

		static Category _log = Category.getInstance(GetFeatureHandler.class.getName());


		// the return list of queries, which are all SQLQuery objects

		public List getQueries() {
				return this.queries;
		}

		public int getQueryCount() {
				return queries.size();
		}

		public List getBoundingBoxes() {
				return this.boundingBoxes;
		}

		public int getBoundingBoxesCount() {
				return boundingBoxes.size();
		}

		public String getSQL() {
				//_log.info("here is the SQL!");
				return currentQuery.getSQL();
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
 								_log.info( "at end of document");
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
				if( insideTag.equals("Query") ) {

						//_log.info("inside query");

						currentQuery.empty();  // needs to be dereferenced?  causes problems for arraylist of queries
						insideQuery = true;
						for( int i=0; i < atts.getLength(); i++ ) {

								//_log.info("inside atts: " + atts.getLocalName(i));

								if( atts.getLocalName(i).equals("typeName") ) {

										//_log.info("found typename: " + atts.getValue(i));

										currentQuery.setFeatureTypeName( atts.getValue(i) );
								}
						}
				}

				// if at a query element, empty the current query, set insideQuery flag, and get query typeNames
				if( insideTag.equals("BoundingBox") ) {

						//_log.info("inside bbox");

						currentBoundingBox = "";  // needs to be dereferenced?  causes problems for arraylist of queries
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

				if( localName.equals("BoundingBox") ) {
						insideBoundingBox = false;
						boundingBoxes.add( currentBoundingBox );
						//currentQuery.empty();  // this method should probably occur here, but then it dereferences the list element
				}

		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {

				String s = new String(ch, start, length);

				// if inside a property element, add the element
				if( insideTag.equals("PropertyName") )
						currentQuery.addProperty(s);

				// if inside chain-generated (by PredicateFilter) SQLPredicate tag
				// check to make sure we are inside a query
				// if so, set this predicate for the current query
				// if outside a current query, add this predicate to all predicates
				if( insideTag.equals("SQLPredicate") ) {
						if( insideQuery )
								currentQuery.setPredicate(s);
						else {
								if( queries.size() > 0 ) {
										for( int i=0; i < queries.size(); i++ ) {
												((SQLStatement) queries.get(i)).setPredicate( ((SQLStatement) queries.get(i)).getPredicate() + " AND " + s );
										}
								}
						}
				}
				if( insideTag.equals("BoundingBox") ) {
						if( insideBoundingBox ) {
								currentBoundingBox = s;
						//_log.info("bbox = " + s);
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
