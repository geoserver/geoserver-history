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
public class PredicateFilter extends XMLFilterImpl {

		// convenience variables to track XML stream
		private boolean insideFilter = false;
		private Operator currentOperator = new Operator();
		static Category _log = Category.getInstance(PredicateFilter.class.getName());

		// variables for holding results
		private SQLStack nestedOperators = new SQLStack();
		private List predicates = Collections.synchronizedList(new LinkedList());  // kill this
		private Hashtable OPERATORS = new Hashtable();

		public PredicateFilter () {
		}

		public PredicateFilter (XMLReader parent) {
				super(parent);
				setOperators();
				//_log.info( "made predicate filter" );

		}

		private void setOperators () {
				OPERATORS.put( "PropertyIsEqualTo", new Operator("PropertyIsEqualTo","comparison"," = ") );
				OPERATORS.put( "PropertyIsLessThan", new Operator("PropertyIsLessThan","comparison"," < ") );
				OPERATORS.put( "PropertyIsGreaterThan", new Operator("PropertyIsGreaterThan","comparison"," > ") );
				OPERATORS.put( "PropertyIsLessThanOrEqualTo", new Operator("PropertyIsLessThanOrEqualTo","comparison"," <= ") );
				OPERATORS.put( "PropertyIsGreaterThanOrEqualTo", new Operator("PropertyIsGreaterThanOrEqualTo","comparison"," >= ") );
				OPERATORS.put( "PropertyIsLike", new Operator("PropertyIsLike","comparison"," LIKE ") );
				OPERATORS.put( "PropertyIsNull", new Operator("PropertyIsNull","comparison"," = NULL ") );
				OPERATORS.put( "PropertyIsBetween", new Operator("PropertyIsBetween","comparison"," <> ") );
				OPERATORS.put( "And", new Operator("And","logical"," AND ") );
				OPERATORS.put( "Or", new Operator("Or","logical"," OR ") );
				OPERATORS.put( "Not", new Operator("Not","negation"," NOT ") );
				OPERATORS.put( "PropertyName", new Operator("PropertyName","value","") );
				OPERATORS.put( "Literal", new Operator("Literal","value","") );
				OPERATORS.put( "Add", new Operator("Or","mathematical"," + ") );
				OPERATORS.put( "Sub", new Operator("Not","mathematical"," - ") );
				OPERATORS.put( "Mul", new Operator("PropertyName","mathematical"," * ") );
				OPERATORS.put( "Div", new Operator("Literal","mathematical"," / ") );
				OPERATORS.put( "NULL", new Operator("NULL","NULL","NULL") );
		}


		public List getPredicates() {
				return new ArrayList();
		}

		public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
				throws SAXException {

				//_log.info( "inside start element" );

				// if inside a filter element, and is not a value, push onto operator stack
				if( insideFilter) {
						currentOperator = ((Operator) OPERATORS.get( localName ));
						if ( !currentOperator.getType().equals("value") )
								nestedOperators.push( currentOperator );
				}

				// if entering a filter, set insideFilter boolean
				if( localName.equals("Filter") ) {
						insideFilter = true;
				}

				// if not inside a filter, pass to parent
				if( !insideFilter ) {
						super.startElement(namespaceURI, localName, rawName, atts);
				}
		}

		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {
				
				// if not inside a filter, pass to parent
				if( !insideFilter )
						super.endElement( namespaceURI, localName, rawName);

				// if leaving a value operator, set currentOperator to null
				else
						if( currentOperator.getType().equals("value") ) {
								currentOperator = ((Operator) OPERATORS.get( "NULL" ));
						}

				// if leaving a filter, set insideFilter boolean to false
				// pass on sqlPredicate to parent for further processing
 				if( localName.equals("Filter") ) {

						insideFilter = false;
						predicates.add( nestedOperators.finalSQL() );
						
						//AttributesImpl emptyAtts = new AttributesImpl();

						super.startElement( "", "SQLPredicate", "", new AttributesImpl());
						super.characters( nestedOperators.finalSQL().toCharArray() , 0, nestedOperators.finalSQL().length() );
						super.endElement( "", "SQLPredicate", "");
				}
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {

				// if not inside a filter, pass to parent
				if( !insideFilter )
						super.characters(ch,start,length);

				// if inside a filter and currentOperator is a value
				// push and simplify (reduce) stack
				else {
						String s = new String(ch, start, length);

						if( currentOperator.getType().equals("value") ) {
								if( currentOperator.getName().equals("Literal") )
										nestedOperators.reduce( "'" + s + "'" );
								else
										nestedOperators.reduce( s );
						}
				}
		}

		public void endDocument()
				throws SAXException {
				System.out.println( nestedOperators.finalSQL() );
		}


}
