/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.logging.Logger;
import org.geotools.filter.Filter;

/**
 * Represents a lock request.
 *
 * @author Rob Hranac, TOPP <br>
 * @author Chris Holmes, TOPP
 * @version $Id: LockRequest.java,v 1.3 2003/07/03 20:46:10 cholmesny Exp $
 */
public class LockRequest extends Request {

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Specifies a lock expiration. */
    protected int expiry = -1;
    
    /** Specifices whether or not to lock all features grabbed in request */
    protected boolean lockAll = true;
    
    protected List locks = new ArrayList();

    /** Empty constructor. */ 
    public LockRequest() { setRequest("LockFeature"); }


    /*************************************************************************
     * SIMPLE GET AND SET METHODS                                            *
     *************************************************************************/
   

    /** Gets whether lock request should fail if not all can be locked */ 
    public boolean getLockAll() { return lockAll; }

    /** Sets whether lock request should fail if not all can be locked */ 
    public void setLockAll(boolean lockAll) { this.lockAll = lockAll; }

    /** Gets the expiration of the locks (in minutes). */ 
     public int getExpiry() { return expiry; }

    /** Sets the expiration of the locks (in minutes). */ 
    public void setExpiry(int expiry) { this.expiry = expiry; }

    /** Gets a list of the locks held by this request(as LockRequest.Lock 
	objects) */ 
    public List getLocks() { return locks; }

    /** adds a single lock to this request */ 
    public void addLock(Lock lock) { this.locks.add(lock); }

    /** Sets the lock list for this request */
    //do we need this method?
    public void setLocks(List locks) { this.locks = locks; }
    
    /**
     * Sets the locks for this request according to the two lists passed in
     * Little error checking is done, as this is a convenience method for
     * LockKVP Reader, which does its own error checking.  None of the locks
     * created will have handles (kvp's don't have handles).
     *
     * @param typeList a list of featureTypes as Strings.  
     * @param filterList a list of Filters.  Should either be null, in which
     * case the locks have no filters, or else should be of the same length
     * as the typeList.
     * @tasks TODO: move error checking from KVP reader here.
     */
    public void setLocks(List typeList, List filterList){
	//int featureSize = typeList.size();
        //int filterSize = filterList.size();
	Iterator typeIterator = typeList.listIterator();
	Iterator filterIterator = filterList.listIterator();
	this.locks = new ArrayList();
	while( typeIterator.hasNext()) {
	    String curType = (String) typeIterator.next();
	    Filter curFilter = null;
	    if (filterIterator.hasNext()) {
		curFilter = (Filter) filterIterator.next();
	    }	    
	    addLock(curType, curFilter);
	}
    }

    /**
     * Creates a lock of the given featureType, filter and handle and
     * adds it to the lock list.
     * @param featureType the typeName to lock.
     * @param filter which features of the featureType to lock.
     * @param handle the name to identify this lock, for error reporting.
     */
    public void addLock(String featureType, Filter filter, String handle){
	Lock addLock = new Lock(featureType);
	addLock.setHandle(handle);
	addLock.setFilter(filter);
	this.locks.add(addLock);
    }

      /**
     * Creates a lock of the given featureType and filter and
     * adds it to the lock list.
     * @param featureType the typeName to lock.
     * @param filter which features of the featureType to lock.
     */
    public void addLock(String featureType, Filter filter){
	Lock addLock = new Lock(featureType);
	addLock.setFilter(filter);
	this.locks.add(addLock);
    }


    /*************************************************************************
     * Overrides of toString and equals methods.                             *
     *************************************************************************/
    public String toString() {
        StringBuffer returnString = new StringBuffer("\nLock Feature Request");
        String indent = "\n    ";
        returnString.append("\n LockAction: " + (lockAll ? "ALL" : "SOME"));
        returnString.append("\n exires in (min):" + expiry);
                
	Iterator lockIterator = locks.listIterator();
	while( lockIterator.hasNext()) {
	    returnString.append(lockIterator.next().toString() +indent);
	}
	return returnString.toString();
    }

    public boolean equals(Object obj) {
	ListIterator internalIterator;
        ListIterator externalIterator;
	boolean isEqual = true;
	if (obj != null && 
	    obj.getClass() == this.getClass()){
	    LockRequest request = (LockRequest)obj;
	    if(this.expiry == request.getExpiry()) {
		isEqual = isEqual && true;
	    } else {
		isEqual = false;
	    }
	    LOGGER.finest("checking expiry equality: " + isEqual);
	    
	    if(this.lockAll == request.getLockAll()) {
		isEqual = isEqual && true;
	    } else {
		isEqual = false;
	    }
	    LOGGER.finest("checking locking equality: " + isEqual);
	    
	    internalIterator = locks.listIterator();
	    externalIterator = request.getLocks().listIterator();
	    while( internalIterator.hasNext()) {
		if( !externalIterator.hasNext()) {
		    isEqual = false;
		    LOGGER.finest("lock lists not same size");
		    break;
		} else {
		    Lock internalLock = (Lock) internalIterator.next();
		    Lock externalLock = (Lock) externalIterator.next();
		    LOGGER.finest("internal lock: " + internalLock);
		    LOGGER.finest("external lock: " + externalLock);
		    if(internalLock.equals(externalLock)) {
			isEqual = true && isEqual;
		    } else {
			isEqual = false;
		    }
		    LOGGER.finest("Locks match: " + isEqual);
		}
	    }
	} else {
	    isEqual = false;
	}
	return isEqual;
    }

    /**
     * helper function for equals.  Checks for nulls, as
     * this class can hold nulls, and will be equal if
     * two of the fields are both null.
     *
     * @param mine The field of this object.
     * @param test the field of to test.
     *
     * @return true if mine equals test, including if they
     * are both null.
     */
    private static boolean testField(Object mine, Object test) {
	LOGGER.finest("testing " + mine + " and " + test);
	if (mine != null) {
	    return mine.equals(test);
	} else {
	    return test == null;
	}
    }

    /**
     * Represents a single Lock element. 
     * @author Chris Holmes
     */
    public static class Lock { 

	/** The feature types to lock. */
	protected String featureType = new String();
        
	/** Specifies the filter to define locked features. */
	protected Filter filter;

	/** The handle to identify this lock by.*/
	protected String handle;

	public Lock(String featureType) {
	    this.featureType = featureType;
	}
	
	/** Gets the feature for this to lock. */
	public String getFeatureType() { return featureType; }

	/** Sets the feature for this to lock */ 
	public void setFeatureType(String featureType) { 
	    //filter out myns: type prefixes here?
	    //or will SAXParser do that for us?
	    this.featureType = featureType; }

	/** Gets the filter of the features to lock. */
	public Filter getFilter() { return filter; }

	/** Sets the filter to define which features to lock */ 
	public void setFilter(Filter filter) { this.filter = filter; }

	/** Gets the handle that identifies this lock. */
	public String getHandle() { return handle; }

	/** Sets the handle that identifies this */ 
	public void setHandle(String handle) { 
	    this.handle = handle; }



         /********************************************************************
	 * Overrides of toString and equals methods.                         *
	 ********************************************************************/
	public String toString() {
	    return("\n Lock\n   typeName: " + featureType + 
		   "\n   handle: " + handle + "\n   filter: " + filter);
	}

	public boolean equals(Object obj) {
	    if (obj != null && 
		obj.getClass() == this.getClass()){
		Lock testLock = (Lock)obj;
		return (testField(this.handle, testLock.getHandle()) &&
			testField(this.featureType, testLock.getFeatureType()) &&
			testField(this.filter, testLock.getFilter()));
	    } else {
		return false;
	    }
	}
	
    }
}

