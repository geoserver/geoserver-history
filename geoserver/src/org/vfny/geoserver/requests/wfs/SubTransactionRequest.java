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
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: SubTransactionRequest.java,v 1.1.2.1 2003/11/04 22:48:26 cholmesny Exp $
 */
public abstract class SubTransactionRequest {
    public static final short UPDATE = 0;
    public static final short INSERT = 1;
    public static final short DELETE = 2;
    private static short operationType;
    protected String handle = null;
    protected String typeName = null;

    public SubTransactionRequest() {
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setFilter(Filter filter) throws WfsException {
    }

    public String getTypeName() {
        return typeName;
    }

    public String getHandle() {
        return handle;
    }

    public abstract short getOpType();
}
