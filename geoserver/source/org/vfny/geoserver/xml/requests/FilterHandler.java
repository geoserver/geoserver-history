/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.xml.requests;

import org.xml.sax.ContentHandler;
import java.util.*;

public interface FilterHandler extends ContentHandler{
		public String getSQL();
		public List getQueries();
}
