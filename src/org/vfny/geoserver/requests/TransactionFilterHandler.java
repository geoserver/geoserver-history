/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFilter;
import org.geotools.filter.FilterHandler;
import org.geotools.gml.GMLHandlerFeature;
import org.geotools.gml.GMLFilterFeature;
import org.vfny.geoserver.responses.WfsException;

/**
 * Uses SAX to extact a Transactional request from and incoming XML stream.
 *
 * @version $VERSION$
 * @author Chris Holmes, TOPP
 */
public class TransactionFilterHandler 
    extends FilterFilter  
    implements GMLHandlerFeature  {

        /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");
    
    private TransactionHandler parent;

       /** Empty constructor. */
    public TransactionFilterHandler(TransactionHandler parent, 
				     FeatureType schema) { 
	super((FilterHandler)parent, schema); 
	this.parent = parent;
	
    }

    //    public void feature(Feature feature) {
    //parent.feature(feature);
    //}

    public void feature(Feature feature) {

	LOGGER.finer("sending feature to transaction " + feature);
	this.parent.feature(feature);
    }

    
}
