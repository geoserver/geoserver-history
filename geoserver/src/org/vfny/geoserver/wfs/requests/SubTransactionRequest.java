/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.requests;

import org.geotools.filter.Filter;
import org.vfny.geoserver.wfs.WfsException;


/**
 * Parent class for Update, Insert, and Delete.
 * 
 * <p>
 * Represents an Element of a Transaction Request.
 * </p>
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: SubTransactionRequest.java,v 1.5 2004/01/31 00:27:28 jive Exp $
 */
public abstract class SubTransactionRequest {
    public static final short UPDATE = 0;
    public static final short INSERT = 1;
    public static final short DELETE = 2;
    protected String handle = null;
    protected String typeName = null;

    public SubTransactionRequest() {
    }

    public SubTransactionRequest(String typeName) {
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
     * 
     * <p>
     * We may consider supplying this in the constructor? Why would this need
     * to change?
     * </p>
     *
     * @param typeName
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Filter providing constraints
     *
     * @param filter DOCUMENT ME!
     *
     * @throws WfsException DOCUMENT ME!
     */
    public void setFilter(Filter filter) throws WfsException {
    }

    /**
     * Name of FeatureTypeInfo being modified
     *
     * @return DOCUMENT ME!
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * User defined name for this Transaction element
     *
     * @return DOCUMENT ME!
     */
    public String getHandle() {
        return handle;
    }

    /**
     * One of UPDATE, INSERT, DELETE
     *
     * @return DOCUMENT ME!
     */
    public abstract short getOpType();
}
