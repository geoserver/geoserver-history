/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import java.sql.SQLException;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.xerces.parsers.SAXParser;

import org.apache.log4j.Category;

import org.vfny.geoserver.db.jdbc.*;
import org.vfny.geoserver.responses.WfsException;


/**
 * This class extends a SAX XMLFilter to read a bounding box.  Note that the 
 * bounding box format is defined in the V0.0.7 OGC filter specification.  Once
 * the filter has found a bounding box, it passes it to the parent content handler, 
 * using the special <i>BoundingBox</i> tag.
 * 
 * @author Rob Hranac, Vision for New York
 * @version $0.9 beta, 11/01/01$
 *
 */
public class FilterBoundingBox extends XMLFilterImpl {


		/********************************************
		 Internal tracking variables...		
		 *******************************************/

		/** Convenience variables to track XML stream: whether or not the handler is inside the filter portion of the Get Feature request */
		private boolean insideFilter = false;

		/** Convenience variables to track XML stream: whether or not the handler is inside a bounding box */
		private boolean insideBoundingBox = false;

		/** Convenience variables to track XML stream: whether or not the handler should read the coordinates when it sees them */
		private boolean readCoordinates = false;

		/** Convenience variables to track XML stream: whether or not the handler should read the property name when it sees it */
		private boolean readPropertyName = false;

		/** Standard logging instance for the class */
		private Category _log = Category.getInstance(FilterBoundingBox.class.getName());

		// *******************************************


		/** Remembers the coordinates for the bounding box */
		private String coordinates;

		/** Remembers the property name for the bounding box */
		private String propertyName;



	 /**
		* Empty constructor.
		*
		*/ 
		public FilterBoundingBox () {
		}


	 /**
		* Constructor with parent argument.
		*
		* @param parent The parent to this filter.
		*
		*/ 
		public FilterBoundingBox (XMLReader parent) {

				super(parent);

		}


	 /**
		* Gets a list of the predicate (bounding box filters) found in the incoming XML stream.
		*
		*/ 
		public List getPredicates() {
				return new ArrayList();
		}


	 /**
		* Determies whether or not handler is entering a filter, bounding box, and its internal elements.
		* If it is outside of a bounding box, this method passes all content up to the parent.
		*
		* @param namespaceURI The namespace for this element.
		* @param localName The local name (within the namespace) for this element.
		* @param rawName The raw name of this element, including namespace.
		* @param atts The element attribues.
		*/ 
		public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
				throws SAXException {

				// if inside a filter element
				if( insideFilter ) {

						// if inside a bounding box element
						if( insideBoundingBox ) {

								// if just saw the start of a coordinates element, set internal tracker to true
								if ( localName.equals("coordinates") )
										readCoordinates = true;

								// if just saw the start of a property name element, set internal tracker to true
								else if ( localName.equals("PropertyName") )
										readPropertyName = true;
						}

						// if not inside a bounding box element, but just saw the start of one, set tracker to true
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


	 /**
		* Determies whether or not handler is leaving a filter, bounding box, and its internal elements.
		* If it is outside of a bounding box, this method passes all content up to the parent.
		*
		* @param namespaceURI The namespace for this element.
		* @param localName The local name (within the namespace) for this element.
		* @param rawName The raw name of this element, including namespace.
		*/ 
		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {

				
				String tempCoord = new String();

				// if not inside a filter, pass to parent
				if( !insideFilter )
						super.endElement( namespaceURI, localName, rawName);

				// if leaving a filter, set insideBoundingBox boolean to false
				// pass on sqlPredicate to parent for further processing
				else {

						// if leaving the filter, then set inside filter tracker to false
						if( localName.equals("Filter") ) {
								insideFilter = false;
						}

						// check if inside the bounding box
						else if( insideBoundingBox ) {
								
								// if leaving bounding box, then create the bounding box object to pass to final handler
								if( localName.equals("BBOX") ) {
										
										// note that we are now outside the bounding box
										insideBoundingBox = false;

										// create a bounding box object and attempt to initialize it
										// MUST FIX SRID

										tempCoord = coordinates.replace( ',' , '!' );
										tempCoord = tempCoord.replace( ' ' , ',' );										
										tempCoord = tempCoord.replace( '!' , ' ' );										

										// THIS IS UGLY KLUDGE
										// SHOULD PASS AS A POSTGIS OBJECT
										String finalSQL = propertyName + " && GeometryFromText('BOX3D (" + tempCoord + ")'::box3d,";				
										
										// generate the predicate for the GetFeatureHandler to grab
										super.startElement( "", "SQLPredicate", "", new AttributesImpl());
										super.characters( finalSQL.toCharArray() , 0, finalSQL.length() );
										super.endElement( "", "SQLPredicate", "");
										
										// generate the raw bounding box for the GetFeatureHandler to grab
										super.startElement( "", "BoundingBox", "", new AttributesImpl());
										super.characters( coordinates.toCharArray() , 0, coordinates.length() );
										super.endElement( "", "BoundingBox", "");
								
								}

								// note, if leaving property name tag
								else if( localName.equals("PropertyName") ) {
										readPropertyName = false;
								}

								// note, if leaving cooordinates tag
								else if( localName.equals("coordinates") ) {
										readCoordinates = false;
								}
						}
				}
				
		}


	 /**
		* Determies whether or not it should read internal bounding box elements and does so, as needed.
		* If it is outside of a bounding box, this method passes all content up to the parent.
		*
		* @param ch The discovered character array.
		* @param start The start of the discovered character array.
		* @param length The length of the discovered character array.
		*/ 
		public void characters(char[] ch, int start, int length)
				throws SAXException {

				// if not inside a bounding box, pass to parent
				if( !insideFilter )
						super.characters(ch,start,length);

				// if inside a bounding box and currentOperator is a value
				// if internal tracker says to read coordinates, do so
				else if( readCoordinates ) {

						// create string from characters, for simplicity
						String s = new String(ch, start, length);
						coordinates = s;
				}

				// if internal tracker says to read property name, do so
				else if( readPropertyName ) {

						// create string from characters, for simplicity
						String s = new String(ch, start, length);
						propertyName = s;
				}
		}
		

	 /**
		* Passes end of document notification to parent.
		*
		*/ 
		public void endDocument()
				throws SAXException {

						super.endDocument();

		}


}
