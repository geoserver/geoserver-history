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
 * Parent class for Update, Insert, and Delete.
 *
 * @version $VERSION$
 * @author Rob Hranac, TOPP
 */
public abstract class SubTransactionRequest {

    public static final short UPDATE = 0;

    public static final short INSERT = 1;

    public static final short DELETE = 2;

    protected String handle = null; 

    protected String typeName = null; 


    public SubTransactionRequest() {}


    public void setHandle(String handle) { this.handle = handle; }

    public void setTypeName(String typeName) { this.typeName = typeName; }

    public void setFilter(Filter filter) throws WfsException {}
   
}
