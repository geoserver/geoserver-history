/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wfs;

import java.util.logging.Logger;

import org.geotools.filter.Filter;


/**
 * Defines a GlobalWFS Delete request, an element of TransactionRequest.
 * <p>
 * Defines a GlobalWFS delete request, which is a type transaction request.  Full
 * transaction requests may contain one or more delete requests.  Note that
 * this delete request object is slightly different than it is defined in the
 * GlobalWFS 1.0 specification.  In the specification, the delete request may
 * contain one or more filters and feature types for deletion.  In this
 * construction - for consistency - each delete request may contain only a
 * single feature type delete operation and filter.  This rationalizes the
 * design of the  transaction object (which contains one or more sub requests)
 * and its  children requests, which contain only a single operation.
 * </p>
 * 
 * <p>
 * Each delete request contains a type name, (optionally) a filter, and
 * (optionally) a release all boolean.  Release all defines the action to
 * preform on feature locks.  If it is specified as true, then all feature
 * locks are released when the transaction terminates.  If it is specified as
 * false, only those records that are modified are released.
 * </p>
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: DeleteRequest.java,v 1.2.2.3 2004/01/02 17:53:28 dmzwiers Exp $
 */
public class DeleteRequest extends SubTransactionRequest {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.wfs");    

    /** Specifies the output format */
    protected String typeName = null;

    /** Specifies the features to lock. */
    protected Filter filter = null;

    protected boolean releaseAll = true;

    /**
     * Empty constructor.
     */
    public DeleteRequest() {
    }

    /**
     * Gets the Name of the FeatureTypeConfig for this request.
     *
     * @return The feature type name.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the name of type to delete.
     *
     * @param typeName The feature type name.
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets the filter of what features should be returned.
     *
     * @return The geotools filter to query against.
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Sets the filter of what features should be returned.
     *
     * @param filter The geotools filter to query against.
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Gets whether all locks should be released after this transaction.
     *
     * @return <tt>true</tt> if all locks should be released.
     */
    public boolean getReleaseAll() {
        return releaseAll;
    }

    /**
     * Sets whether all locks should be released after this transaction.
     *
     * @param releaseAll if all locks should be released.
     */
    public void setReleaseAll(boolean releaseAll) {
        this.releaseAll = releaseAll;
    }

    /**
     * Returns what type of transaction operation this is.
     *
     * @return the string "Delete"
     */
    public String getOperation() {
        return "Delete";
    }

    /**
     * Gets the short representation of this operation.
     *
     * @return the short representation of delete.
     */
    public short getOpType() {
        return DELETE;
    }

    /**
     * Override of toString().
     *
     * @return a string representation of this delete request.
     */
    public String toString() {
        StringBuffer returnString = new StringBuffer("Delete Feature Request");
        returnString.append("\n feature type:" + typeName);
        returnString.append("\n filter:" + filter.toString());
        returnString.append("\n release:" + releaseAll);

        return returnString.toString();
    }

    public boolean equals(Object obj) {
        if ((obj != null) && (obj.getClass() == this.getClass())) {
            DeleteRequest request = (DeleteRequest) obj;
            boolean isEqual = true;

            if (this.typeName != null) {
                if (this.typeName.equals(request.getTypeName())) {
                    isEqual = isEqual && true;
                } else {
                    isEqual = false;
                }
            } else {
                isEqual = isEqual && (request.getTypeName() == null);
            }

            LOGGER.finest("checking feature type names for equality: "
                + isEqual);

            if (this.releaseAll == request.getReleaseAll()) {
                isEqual = isEqual && true;
            } else {
                isEqual = false;
            }

            LOGGER.finest("checking release action equality: " + isEqual);

            if (this.filter != null) {
                if (this.filter.equals(request.getFilter())) {
                    isEqual = isEqual && true;
                } else {
                    isEqual = false;
                }
            } else {
                isEqual = isEqual && (request.getFilter() == null);
            }

            LOGGER.finest("checking filter equality: " + isEqual);

            return isEqual;
        } else {
            return false;
        }
    }
}
