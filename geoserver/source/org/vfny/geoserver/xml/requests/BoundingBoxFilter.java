/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.xml.requests;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.xerces.parsers.SAXParser;
import org.vfny.geoserver.db.jdbc.*;

/**
 * This class extends a SAX XMLFilter to read a V0.0.7 OGC filter specification and 
 * translate it into a valid SQL predicate..
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 beta, 11/01/01
 *
 */
public class BoundingBoxFilter extends XMLFilterImpl {

		// convenience variables to track XML stream
		private boolean insideFilter = false;
		private boolean insideBoundingBox = false;
		private boolean readCoordinates = false;
		private boolean readPropertyName = false;
		static Category _log = Category.getInstance(PredicateFilter.class.getName());

		// variables for holding results
		private String coordinates;
		private String propertyName;

		public BoundingBoxFilter () {
		}

		public BoundingBoxFilter (XMLReader parent) {
				super(parent);
				//_log.info( "made bounding box filter" );

		}

		public List getPredicates() {
				return new ArrayList();
		}

		public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
				throws SAXException {

				//_log.info( "inside start element: " + localName);

				// if inside a filter element, and is not a value, push onto operator stack
				if( insideFilter ) {
						if( insideBoundingBox ) {
								if ( localName.equals("coordinates") )
										readCoordinates = true;
								else if ( localName.equals("PropertyName") )
										readPropertyName = true;
						}
						else if( localName.equals("BBOX") )
								insideBoundingBox = true;
				}

				// if not inside a filter, pass to parent
				else if( localName.equals("Filter") )
						insideFilter = true;
				else {
				//_log.info( "this element passed up: " + localName);
						super.startElement(namespaceURI, localName, rawName, atts);
				}
		}

		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {

				
				// if not inside a filter, pass to parent
				if( !insideFilter )
						super.endElement( namespaceURI, localName, rawName);

				// if leaving a filter, set insideBoundingBox boolean to false
				// pass on sqlPredicate to parent for further processing
				else {

								//_log.info( "at end element: " + localName);


						if( localName.equals("Filter") ) {
								
								
								
								insideFilter = false;
						}
						else if( insideBoundingBox ) {
								

								
								if( localName.equals("BBOX") ) {
										
										insideBoundingBox = false;
										String finalSQL = propertyName + " && GeometryFromText('BOX3D(" + coordinates + ")'::box3d,-1)";

										//_log.info( "the SQL query: " + finalSQL);
										//_log.info( "the bbox: " + coordinates);

								
										//AttributesImpl emptyAtts = new AttributesImpl();
										
										super.startElement( "", "SQLPredicate", "", new AttributesImpl());
										super.characters( finalSQL.toCharArray() , 0, finalSQL.length() );
										super.endElement( "", "SQLPredicate", "");
										
										super.startElement( "", "BoundingBox", "", new AttributesImpl());
										super.characters( coordinates.toCharArray() , 0, coordinates.length() );
										super.endElement( "", "BoundingBox", "");
								
								}
								else if( localName.equals("PropertyName") ) {
										readPropertyName = false;
								}
						}
				}
				
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {

				String s = new String(ch, start, length);

				// if not inside a filter, pass to parent
				if( !insideFilter )
						super.characters(ch,start,length);

				// if inside a filter and currentOperator is a value
				// push and simplify (reduce) stack
				else if( readCoordinates )
						coordinates = s;
				else if( readPropertyName ) {

						propertyName = s;
								//_log.info( "property name: " + propertyName);

				}
		}
		
		public void endDocument()
				throws SAXException {

								//_log.info( "at end of document");
						super.endDocument();

		}


}
