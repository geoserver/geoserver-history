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
import com.vividsolutions.jts.geom.Geometry;
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

    /**
     * Recieves the geometry from a child, and either passes it on
     * to FilterFilter to add to the filter, or if not in a filter
     * then it goes to the transaction handler.
     * 
     * @param geometry called by a child filter when it can not deal
     * with it.
     * @task REVISIT: This whole transaction handling is quite messy
     * with geometries possible in property, filter, or feature.  We
     * may want to re-architect this, something with a more flexible
     * dynamic handler, that creates subhandlers as it needs them, instead
     * of each trying to figure out if it can deal with what the subhandler
     * passes up.
     */
    public void geometry(Geometry geometry){
	LOGGER.finest("filter handler got geometry");
	if (insideFilter){
	    LOGGER.finest("sending to filterfilter " + geometry);
	    super.geometry(geometry);
	} else {
	    LOGGER.finest("sending to transaction " + geometry);
	    parent.geometry(geometry);
	}
    }


    public void feature(Feature feature) {

	LOGGER.finer("sending feature to transaction " + feature);
	this.parent.feature(feature);
    }

    
}
