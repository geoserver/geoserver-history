/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wfs;

import org.geotools.filter.*;
import org.vfny.geoserver.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.wfs.*;


/**
 * Parent class for Update, Insert, and Delete.
 * <p>
 * Represents an Element of a Transaction Request.
 * </p>
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: SubTransactionRequest.java,v 1.2 2003/12/16 18:46:09 cholmesny Exp $
 */
public abstract class SubTransactionRequest {
    public static final short UPDATE = 0;
    public static final short INSERT = 1;
    public static final short DELETE = 2;
    
    protected String handle = null;
    protected String typeName = null;

    public SubTransactionRequest() {
    }
    public SubTransactionRequest( String typeName ){
        this.typeName = typeName;        
    }
    /**
     * User supplied handle for this sub element.
     * 
     * @param handle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Provide typeName for this sub element.
     * <p>
     * We may consider supplying this in the constructor? Why would this need
     * to change?
     * </p>
     * @param typeName
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /** Filter providing constraints */
    public void setFilter(Filter filter) throws WfsException {
    }

    /** Name of FeatureType being modified */
    public String getTypeName() {
        return typeName;
    }

    /** User defined name for this Transaction element */
    public String getHandle() {
        return handle;
    }
    /** One of UPDATE, INSERT, DELETE */
    public abstract short getOpType();
}
