/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wfs;

import java.util.ArrayList;
import java.util.List;

import org.vfny.geoserver.requests.WFSRequest;
import org.vfny.geoserver.responses.wfs.WfsTransactionException;


/**
 * Transactional request object, consisting of one or more sub transactional
 * requests.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: TransactionRequest.java,v 1.2.2.3 2004/01/02 17:53:28 dmzwiers Exp $
 */
public class TransactionRequest extends WFSRequest {
    public static final String TRANSACTION_REQUEST_TYPE = "Transaction";
        
    /** Assume this is a list of SubTransactionRequest */
    protected List subRequests = new ArrayList();
    
    /** Assume null value means no lockID specified */
    protected String lockId = null;
    
    /** Replaced with releaseAction */ 
    //protected boolean releaseAll = true;


    /**
     * Release lockID when the transaction completes.
     * <p>
     * GlobalWFS Specification Definition of ALL:
     * </p>
     * <p>
     * <i>    A value of ALL indicates that the locks on all feature instances
     * locked using the specified <b>lockId</b> should be released when the
     * transaction completes, regardless of whether or not a particular feature
     * instance in the locked set was actually operated upon. </i>
     * </p>
     */
    public static final ReleaseAction ALL = new ReleaseAction("ALL");
    
    /**
     * Release lockID when the transaction completes.
     * <p>
     * GlobalWFS Specification Definition of SOME:
     * </p>
     * <i>    A value of SOME indicates that only the locks on feature instances
     * modified by the transaction should be released. The other, unmodified,
     * feature instances should remain locked using the same <b>LockId</b> so
     * that subsequent transactions can operate on those feature instances. </i>
     * </p>
     * <p>
     * <i>    In the event that the releaseAction is set to SOME, and an expiry
     * period was specified, the expiry counter must be reset to zero after
     * each transaction unless all feature instances in the locked set have
     * been operated upon. </i>
     * </p>
     */    
    public final static ReleaseAction SOME = new ReleaseAction("SOME");
    
    /**
     * Control how locked features are treated when a transaction is completed.
     */
    protected ReleaseAction releaseAction = ALL;
    
    /**
     * Specifices the user-defined name for the entire transaction request.
     * <p>
     * Note that SubTransactionRequests are allowed there own handle as well,
     * if they don't have one we should try and describe their position
     * in a relative way.
     * </p>
     */
    protected String handle = null;

    /**
     * Create a GlobalWFS Transaction request. 
     */
    public TransactionRequest() {
        super( TRANSACTION_REQUEST_TYPE );        
    }

    /**
     * Adds a delete, insert, or update request to this transaction.
     * <p>
     * If subRequest does not yet have a handle we will invent one here
     * and give it one, the alternative is have subRequest known about its
     * parent Transaction in order to generate a handle.
     * </p>
     * @param subRequest To be part of this transaction request.
     */
    public void addSubRequest(SubTransactionRequest subRequest) {
        subRequests.add(subRequest);
        if( subRequest.getHandle() == null ){
            subRequest.setHandle( getHandle()+" "+subRequest.getTypeName()+" "+subRequests.size() );
        }
    }

    /**
     * Gets the request held at position i.
     *
     * @param i the position of the operation request to get.
     *
     * @return the request at i.
     */
    public SubTransactionRequest getSubRequest(int i) {
        return (SubTransactionRequest) subRequests.get(i);
    }

    /**
     * Gets the number of delete, update, and insert operations
     *
     * @return Number of sub requests.
     */
    public int getSubRequestSize() {
        return subRequests.size();
    }

    /**
     * Sets the  user-defined name for the entire Transaction request.
     *
     * @param handle The user name for this request.
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Returns the user-defined name for the entire Transaction request.
     *
     * @return The user name for this request.
     */
    public String getHandle() {
        return handle;
    }

    public void setReleaseAction(String releaseAction)
        throws WfsTransactionException {
            
        if (releaseAction != null) {
            if (releaseAction.toUpperCase().equals("ALL")) {
                this.releaseAction = ALL;
            } else if (releaseAction.toUpperCase().equals("SOME")) {
                this.releaseAction= SOME;
            } else {
                throw new WfsTransactionException("Illegal releaseAction: "
                    + releaseAction + ", only " + "SOME or ALL allowed",
                    handle, handle);
            }
        }
        else {
            // TODO: Review this please - should we restore the default here?
            this.releaseAction = ALL;
        }
    }

    /**
     * Sets the release for the Transaction request.
     * <p>
     * <ul>
     * <li>true:  if all features held by the lockID should be released</li>
     * <li>false: if all feature held by the lockID, and not modified by this
     *            transaction should be reset to their initial expire value</li>
     * </ul>
     * <p>
     * No matter what value you pick the features you modify in this transaction
     * that are held by lockID will be released.
     * </p>     
     * @param releaseAll <tt>true</tt> if all features held by the lock should
     *        be released after this operation.
     */
    public void setReleaseAction(boolean releaseAll ) {
        if( releaseAll ){
            releaseAction = ALL;
        }
        else {
            releaseAction = SOME;
        }        
    }

    /**
     * Returns the release for the Transaction request.
     *
     * @return <tt>true</tt> if all features held by the lock should be
     *         released after this operation.
     */
    public ReleaseAction getReleaseAction() {
        return releaseAction;
    }

    /**
     * Sets the lock id for the entire Transaction request.
     *
     * @param lockId The authorization string to perform with this transaction.
     */
    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    /**
     * Returns the lock id for the entire Transaction request.
     *
     * @return The authorization string to perform with this transaction.
     */
    public String getLockId() {
        return lockId;
    }

    public String toString() {
        StringBuffer tRequest = new StringBuffer("Lock Id: " + lockId + "\n");
        tRequest.append("releaseAction: " + releaseAction + "\n");
        tRequest.append("handle: " + handle + "\n");

        for (int i = 0; i < subRequests.size(); i++) {
            tRequest.append(subRequests.get(i).toString() + "\n");
        }

        return tRequest.toString();
    }

    /**
     * helper function for equals.  Checks for nulls, as this class can hold
     * nulls, and will be equal if two of the fields are both null.
     *
     * @param mine The field of this object.
     * @param test the field of to test.
     *
     * @return true if mine equals test, including if they are both null.
     */
    private boolean testField(Object mine, Object test) {
        if (mine != null) {
            return mine.equals(test);
        } else {
            return test == null;
        }
    }

    public boolean equals(Object obj) {
        if ((obj != null) && (obj.getClass() == this.getClass())) {
            TransactionRequest testTrans = (TransactionRequest) obj;
            boolean isEqual = true;

            if (this.releaseAction == testTrans.releaseAction) {
                isEqual = testField(this.lockId, testTrans.getLockId());
                isEqual = testField(this.handle, testTrans.getHandle())
                    && isEqual;

                //isEqual = testField(this.version, testTrans.version) && isEqual;
                if (this.subRequests.size() == testTrans.subRequests.size()) {
                    for (int i = 0; i < subRequests.size(); i++) {
                        isEqual = isEqual
                            && subRequests.contains(testTrans.getSubRequest(i));
                    }
                } else {
                    isEqual = false;
                }
            } else {
                isEqual = false;
            }

            return isEqual;
        } else {
            return false;
        }
    }
}

/**
 * Class used for releaseAction values SOME and ALL.
 * <p>
 * This class is not public, so the only two instances
 * will be TransactionRequest.SOME and TransactionRequest.ALL.
 * </p>
 * <p>
 * Since we are only using two instances we can use the identity opperator,
 * to check the value of releaseAction.
 * </p>
 * <p>
 * The is the standard Java Enum idiom, we could later subclass
 * SOME and ALL to provide support for their respective unlocking
 * policies.
 * </p>
 */
class ReleaseAction {
private String text;
ReleaseAction( String str ){
    text = str;
}
public String toString(){
    return text;
}    

}