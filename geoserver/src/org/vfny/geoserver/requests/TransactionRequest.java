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
 * @version $version$
 */
public class TransactionRequest 
    extends Request {

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");
    
    protected List subRequests = new ArrayList();
        
    protected String lockId = null;

    protected boolean releaseAll = false;


    /** Empty constructor. */ 
    public TransactionRequest () { setRequest("Transaction"); }


    /*************************************************************************
     * SIMPLE GET AND SET METHODS                                            *
     *************************************************************************/
    public void addSubRequest(SubTransactionRequest subRequest) {
        subRequests.add(subRequest);
    }

    public SubTransactionRequest getSubRequest(int i) {
        return (SubTransactionRequest) subRequests.get(i);
    }

    public int getSubRequestSize() {
        return subRequests.size();
    }

}
