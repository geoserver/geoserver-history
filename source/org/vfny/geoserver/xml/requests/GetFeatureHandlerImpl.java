/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.xml.requests;

import org.xml.sax.ContentHandler;
import java.util.*;

/**
 * This class extends a the ContentHandler to include methods
 * for extracting queries from the GetFeature XML stream.
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 beta, 11/01/01
 *
 */
public interface GetFeatureHandlerImpl extends ContentHandler{
		public String getSQL();
		public List getQueries();
		public int getQueryCount();
		public List getBoundingBoxes();
		public int getBoundingBoxesCount();
}
