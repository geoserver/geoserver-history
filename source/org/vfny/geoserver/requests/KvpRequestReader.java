/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is released under the Apache license, availible at the root GML4j directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;


/**
 * Reads in a general KVP request and turns it into a list KVPs, stored in a hashtable.
 * 
 * <p>If you pass this utility a KVP request (everything after the '?' in the URI),
 * it will translate this into a list of key-word value pairs.  These pairs represent
 * every element in the KVP GET request.  This class may then be subclassed and
 * used by request-specific classes.  Note that you must check for validity
 * before passing the request.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version beta, 12/01/01
 *
 */
abstract public class KvpRequestReader {


		/** Internal tokenizer for raw coordinate string */
		protected static final String KEYWORD_DELIMITER = "&";

		/** Internal tokenizer for raw coordinate string */
		protected static final String VALUE_DELIMITER = "=";

		/** Internal tokenizer for raw coordinate string */
		protected static final String LIST_DELIMITER = ",";

		/** KVP pair listing; stores all data from the KVP request */
		protected Hashtable kvpPairs = new Hashtable();


	 /**
		* Constructor with raw request string.  This constructor
		* parses the entire request string into a kvp hash table for
		* quick access by sub-classes.
		*
		* @param rawRequest The raw request string from the client.
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
						String key;
						String value;

						// make sure that there is a key token
						if( requestValues.hasMoreTokens() ) {

								// assign key as uppercase version to eliminate case conflicts
								key = requestValues.nextToken().toUpperCase();

								// make sure that there is a value token
								if( requestValues.hasMoreTokens() ) {

										// assign value and store in hash with key
										value = requestValues.nextToken();
										kvpPairs.put( key, value );
								}
						}
				}

		}

}
