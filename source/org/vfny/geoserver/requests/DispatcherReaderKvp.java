/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;


/**
 * This utility reads in a general KVP request and turns it into a list KVPs, stored in a hashtable.
 * 
 * <p>If you pass this utility a KVP request (everything after the '?' in the URI),
 * it will translate this into a list of feature types.  Note that you must check for validity
 * before passing the request.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version alpha, 12/01/01
 *
 */
public class DispatcherReaderKvp extends KvpRequestReader {


		/** Map get capabilities request type */
		public static final int GET_CAPABILITIES_REQUEST = 1;

		/** Map describe feature type request type */
		public static final int DESCRIBE_FEATURE_TYPE_REQUEST = 2;

		/** Map get feature  request type */
		public static final int GET_FEATURE_REQUEST = 3;


	 /**
		* Constructor with raw request string.  This constructor
		* parses the entire request string into a kvp hash table for
		* quick access by sub-classes.
		*
		* @param describeFeatureTypeRequest The raw request string from the client.
		*/
		public DispatcherReaderKvp (String rawRequest) {
				super(rawRequest);
		}

	 /**
		* Constructor with raw request string.  This constructor
		* parses the entire request string into a kvp hash table for
		* quick access by sub-classes.
		*
		*/
		public int getRequestType () {

				String responseType = ((String) kvpPairs.get("REQUEST")).toUpperCase();

				if( responseType.equals("GETCAPABILITIES") )
						return GET_CAPABILITIES_REQUEST;

				else if( responseType.equals("DESCRIBEFEATURETYPE") )
						return DESCRIBE_FEATURE_TYPE_REQUEST;

				else if( responseType.equals("GETFEATURE") )
						return GET_FEATURE_REQUEST;
				
				else
						return -1;
		}


}
