/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.servlets.utilities;

import java.io.*;
import java.util.*;

/**
 * This utility reads in a DescribeFeatureType KVP request and turns it into a list of requested Feature Types.
 * 
 * <p>If you pass this utility a KVP request (everything after the '?' in the URI),
 * it will translate this into a list of feature types.  Note that you must check for validity
 * before passing the request.</p>
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version alpha, 12/01/01
 *
 */
public class KvpDescribeFeatureTypeReader extends KvpRequestReader {

	 /**
		* Constructor with raw request string.  Calls parent.
		*
		* @param describeFeatureTypeRequest The raw request string from the client.
		*/
		public KvpDescribeFeatureTypeReader (String describeFeatureTypeRequest) {
				super(describeFeatureTypeRequest);

		}

	 /**
		* Returns a list of requested feature types..
		*
		*/
		public DescribeFeatureTypeRequest getRequest () {
				DescribeFeatureTypeRequest currentRequest = new DescribeFeatureTypeRequest();
				
				currentRequest.setVersion( ((String) kvpPairs.get("VERSION")) );
				currentRequest.setVersion( ((String) kvpPairs.get("REQUEST")) );
				currentRequest.setFeatureTypes( getFeatureTypes() );

				return currentRequest;
		}


	 /**
		* Returns a list of requested feature types..
		*
		*/
		private List getFeatureTypes () {
				String requestedTypes = ((String) kvpPairs.get("TYPENAME")); 
				return getFeatureType( requestedTypes );

		}

	 /**
		* This utility checks for more coordinates in the set passed by the constructor..
		*
		* @param typeNameValues The unparsed type names from the Hash.
		*/
		private List getFeatureType (String typeNameValues) {
				
	 			StringTokenizer	typeListValues = new StringTokenizer( typeNameValues.trim(), LIST_DELIMITER );
				List typeList = new LinkedList();
				
				while( typeListValues.hasMoreTokens() )
						typeList.add( typeListValues.nextToken() );

				return typeList;
		}
										

}
