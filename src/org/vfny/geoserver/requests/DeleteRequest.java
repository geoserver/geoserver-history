/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.geotools.filter.Filter;

/**
 * <p>Defines a WFS delete request, which is a type transaction request.  Full
 * transaction requests may contain one or more delete requests.  Note that 
 * this delete request object is slightly different than it is defined in the 
 * WFS 1.0 specification.  In the specification, the delete request may contain
 * one or more filters and feature types for deletion.  In this construction -
 * for consistency - each delete request may contain only a single feature
 * type delete operation and filter.  This rationalizes the design of the 
 * transaction object (which contains one or more sub requests) and its 
 * children requests, which contain only a single operation.</p>
 *
 * <p>Each delete request contains a type name, (optionally) a filter, and 
 * (optionally) a release all boolean.  Release all defines the action to 
 * preform on feature locks.  If it is specified as true, then all feature 
 * locks are released when the transaction terminates.  If it is specified as 
 * false, only those records that are modified are released.</p>
 *
 *@author Rob Hranac, TOPP
 *@version $VERSION$
 */
public class DeleteRequest 
    extends SubTransactionRequest {

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");
    
    /** Specifies the output format */
    protected static final String operation = "Delete";
    
    /** Specifies the output format */
    protected String typeName = null;
        
    /** Specifies the features to lock. */
    protected Filter filter = null;

    /** Specifices the user-defined name for the entire get feature request */
    protected boolean releaseAll = true;


    /** Empty constructor. */ 
    public DeleteRequest () {}


    /*************************************************************************
     * SIMPLE GET AND SET METHODS                                            *
     *************************************************************************/

    /** Sets the expiration of the locks (in minutes). */ 
    public String getTypeName() { return typeName; }

    /** Sets the expiration of the locks (in minutes). */ 
    public void setTypeName(String typeName) { 
        this.typeName = typeName; }

    /** Sets the expiration of the locks (in minutes). */ 
    public Filter getFilter() { return filter; }

    /** Sets the expiration of the locks (in minutes). */ 
    public void setFilter(Filter filter) { this.filter = filter; }

    /** Sets the expiration of the locks (in minutes). */ 
    public boolean getReleaseAll() { return releaseAll; }

    /** Sets the expiration of the locks (in minutes). */ 
    public void setReleaseAll(boolean releaseAll) { 
        this.releaseAll = releaseAll; }

    /** Sets the expiration of the locks (in minutes). */ 
    public String getOperation() { return operation; }


    /*************************************************************************
     * OVERRIDES OF toString AND equals METHODS.                             *
     *************************************************************************/
    public String toString() {
        StringBuffer returnString = new StringBuffer("Delete Feature Request");
        String indent = "\n    ";
        returnString.append("\n feature type:" + typeName);
        returnString.append("\n filter:" + filter.toString());
        returnString.append("\n release:" + releaseAll);
        return returnString.toString();
    }

    public boolean equals(DeleteRequest request) {
        boolean isEqual = true;

        if(this.typeName != null &&
           this.typeName.equals(request.getTypeName())) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }
        LOGGER.finest("checking feature type names for equality: " + isEqual);

        if(this.releaseAll == request.getReleaseAll()) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }
        LOGGER.finest("checking release action equality: " + isEqual);

        if(this.filter != null &&
           this.filter.equals(request.getFilter())) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }
        LOGGER.finest("checking filter equality: " + isEqual);

        return isEqual;
    }
}
