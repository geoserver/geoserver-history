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
 * Transactional request object, consisting of one or more sub transactional
 * requests.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $version$
 */
public class TransactionRequest 
    extends Request {

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");
    
    protected List subRequests = new ArrayList();
        
    protected String lockId = null;

    protected boolean releaseAll = true;

   /** Specifices the user-defined name for the entire get feature request */
    protected String handle = null;

    /** Empty constructor. */ 
    public TransactionRequest () { setRequest("Transaction"); }


    /*************************************************************************
     * SIMPLE GET AND SET METHODS                                            *
     *************************************************************************/

    /** adds a delete, insert, or update request to this transaction. */
    public void addSubRequest(SubTransactionRequest subRequest) {
        subRequests.add(subRequest);
    }

    /** 
     * Gets the request held at position i.
     *
     * @param i the position of the operation request to get.
     * @return the request at i.
     */
    public SubTransactionRequest getSubRequest(int i) {
        return (SubTransactionRequest) subRequests.get(i);
    }

    /** Gets the number of delete, update, and insert operations*/
    public int getSubRequestSize() {
        return subRequests.size();
    }

    /** Sets the  user-defined name for the entire Transaction request. */
    public void setHandle(String handle) { this.handle = handle; }
    
    /** Returns the user-defined name for the entire Transaction request. */ 
    public String getHandle() { return handle; }


    public String toString() {
	StringBuffer tRequest = new StringBuffer("Lock Id: " + lockId + "\n");
	tRequest.append("release all: " + releaseAll + "\n");
	for (int i = 0; i < subRequests.size(); i++) {
	    tRequest.append(subRequests.get(i).toString() + "\n");
	}
	return tRequest.toString();
    }    


}
