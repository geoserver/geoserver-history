/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;


/**
 * This utility reads in a DescribeFeatureType KVP request and turns it into a list of requested Feature Types.
 * 
 * <p>If you pass this utility a KVP GET request (everything after the '?' in the URI),
 * it will translate this into a list of feature types.  Note that you must check for validity
 * before passing the request.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version beta, 12/01/01
 *
 */
public class DescribeFeatureTypeReaderKvp extends KvpRequestReader {


	 /**
		* Constructor with raw request string.  Calls parent.
		*
		* @param describeFeatureTypeRequest The raw request string from the client.
		*/
		public DescribeFeatureTypeReaderKvp (String describeFeatureTypeRequest) {
				super(describeFeatureTypeRequest);

		}


	 /**
		* Returns a list of requested feature types..
		*
		*/
		public DescribeFeatureTypeRequest getRequest () {
				DescribeFeatureTypeRequest currentRequest = new DescribeFeatureTypeRequest();
				
				currentRequest.setVersion( ((String) kvpPairs.get("VERSION")) );
				currentRequest.setRequest( ((String) kvpPairs.get("REQUEST")) );
				currentRequest.setFeatureTypes( getFeatureTypes() );

				return currentRequest;
		}


	 /**
		* Returns a list of requested feature types.  If no feature types were requested, returns an empty vector.
		*
		*/
		private Vector getFeatureTypes () {

				// asks for a new string from the TYPENAME requests
				String requestedTypes = ((String) kvpPairs.get("TYPENAME")); 

				// checks to see if this is null:
				//  if so, then return an empty vector
				//  if, not call private function to parse feature types
				if( !(requestedTypes == null) )
						return getFeatureType( requestedTypes );
				else
						return new Vector();

		}


	 /**
		* This utility checks for more coordinates in the set passed by the constructor..
		*
		* @param typeNameValues The unparsed type names from the Hash.
		*/
		private Vector getFeatureType (String typeNameValues) {
				
	 			StringTokenizer	typeListValues = new StringTokenizer( typeNameValues.trim(), LIST_DELIMITER );
				Vector typeList = new Vector();
				
				// checks to see if this is null;
				while( typeListValues.hasMoreTokens() )
						typeList.add( typeListValues.nextToken() );

				// return list back to calling function
				return typeList;
		}
										
}
