/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.servlets.utilities;

import java.io.*;
import java.util.*;

/**
 * This utility reads in a general KVP request and turns it into a list KVPs, stored in a hashtable.
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
abstract public class KvpRequestReader {

		// internal tokenizer for raw coordinate string
		protected static final String KEYWORD_DELIMITER = "&";
		protected static final String VALUE_DELIMITER = "=";
		protected static final String LIST_DELIMITER = ",";
		protected Hashtable kvpPairs = new Hashtable();

	 /**
		* Constructor with raw request string.  This constructor
		* parses the entire request string into a kvp hash table for
		* quick access by sub-classes.
		*
		* @param describeFeatureTypeRequest The raw request string from the client.
		*/
		public KvpRequestReader (String rawRequest) {

				// instantiate request cleaner class
				CleanRequest requestCleaner = new CleanRequest();

				// uses the request cleaner to remove HTTP junk
				String cleanRequest = requestCleaner.clean( rawRequest );

				// parses initial request sream into KVPs
				StringTokenizer requestKeywords = new StringTokenizer( cleanRequest.trim(), KEYWORD_DELIMITER );

				// parses KVPs into values and keywords and puts them in a HashTable
				while( requestKeywords.hasMoreTokens() ) {
						StringTokenizer requestValues = new StringTokenizer( requestKeywords.nextToken(), VALUE_DELIMITER );
						kvpPairs.put( requestValues.nextToken(),requestValues.nextToken() );
				}

		}

}
