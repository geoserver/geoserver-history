/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.geotools.filter.Filter;
import org.vfny.geoserver.responses.WfsException;

/**
 * 
 * @version $VERSION$
 * @author Rob Hranac, TOPP
 */
public class InsertRequest 
    extends SubTransactionRequest {

    public static final short operationType = INSERT;

    /** Empty constructor. */
    public InsertRequest() {}


    public void setFilter(Filter filter)
        throws WfsException {
        throw new WfsException("Attempted to add filter (" + filter + 
                               ") to update request: " + handle);
    }

    public short getOpType() { return operationType; }
   
}
