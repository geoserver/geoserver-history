/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
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
 * This utility reads in a DescribeFeatureType KVP request and turns it into a list of requested Feature Types.
 * 
 * <p>If you pass this utility a KVP request (everything after the '?' in the URI),
 * it will translate this into a list of feature types.  Note that you must check for validity
 * before passing the request.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version alpha, 12/01/01
 *
 */
public class GetFeatureReaderKvp extends KvpRequestReader {


	 /**
		* Constructor with raw request string.  Calls parent.
		*
		* @param describeFeatureTypeRequest The raw request string from the client.
		*/
		public GetFeatureReaderKvp (String getFeatureRequest) {
				super(getFeatureRequest);

		}


	 /**
		* Returns a list of requested feature types..
		*
		*/
		public GetFeatureRequest getRequest () {

				GetFeatureRequest currentRequest = new GetFeatureRequest();

				currentRequest.setVersion( (String) kvpPairs.get("VERSION") );
				currentRequest.setRequest( (String) kvpPairs.get("REQUEST") );
				currentRequest.setMaxFeatures( (String) kvpPairs.get("MAXFEATURES") );

				currentRequest.setQueries( getQueries() );

				return currentRequest;
		}


	 /**
		* Returns a list of requested feature types..
		*
		*/
		private List getQueries() {

				Vector currentQueries = new Vector();

				StringTokenizer featureTypes = new StringTokenizer( ((String) kvpPairs.get("TYPENAME")), "," );
				//StringTokenizer propertyNames = new StringTokenizer( ((String) kvpPairs.get("PROPERTYNAME")), "," );
				//StringTokenizer featureIds = new StringTokenizer( ((String) kvpPairs.get("FEATUREID")), "," );
				//StringTokenizer filters = new StringTokenizer( ((String) kvpPairs.get("FILTER")), "," );

				while( featureTypes.hasMoreTokens() ) {
						currentQueries.add ( makeQuery( featureTypes.nextToken(), "", "", "" ) ); 
						//currentQueries.add ( makeQuery( featureTypes.nextToken(), propertyNames.nextToken(), featureIds.nextToken(), filters.nextToken() ) ); 
				}  
				
				return currentQueries;

		}


	 /**
		* This utility checks for more coordinates in the set passed by the constructor..
		*
		* @param typeNameValues The unparsed type names from the Hash.
		*/
		private Query makeQuery (String featureType, String propertyNamesGroup, String featureIdsGroup, String filter) {

				//StringTokenizer propertyNames = new StringTokenizer( propertyNamesGroup, "()," );
				//StringTokenizer featureIds = new StringTokenizer( featureIdsGroup, "()," );

				// make new query statement
				Query currentQuery = new Query();

				// set feature type name for new query statement
				currentQuery.setFeatureTypeName( featureType );

				// set property names for new query statement
				//while( propertyNames.hasMoreTokens() ) {
				//currentQuery.setFeatureTypeName( propertyNames.nextToken() );
				//}

				// set filter for new query statement
				//currentQuery.setPredicate( readFilter(filter) );

				// return new query
				return currentQuery;
		}


	 /**
		* This utility checks for more coordinates in the set passed by the constructor..
		*
		* @param filter The unparsed type names from the Hash.
		*/
		private void readFilter (String filter) {
				
				// translate string into a proper SAX input source
				Reader requestReader = new StringReader(filter);
				InputSource requestSource = new InputSource(requestReader);

				// instantiante parsers and content handlers
				XMLReader parser = new SAXParser();
				FilterPredicate filterParser = new FilterPredicate(parser);
				GetFeatureHandler contentHandler = new GetFeatureHandler();

				// read in XML file and parse to content handler
				try {
						filterParser.setContentHandler(contentHandler);
						filterParser.setParent(parser);
						filterParser.parse(requestSource);
				}
				catch (SAXException e) {
						System.out.println("Error in parsing: " + e.getMessage() );
				}
				catch (IOException e) {
						System.out.println("Input error: " + e.getMessage() );
				}

				//return contentHandler.getFilter();

		}
										

}
