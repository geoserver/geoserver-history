/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wfs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockFactory;
import org.geotools.filter.Filter;
import org.vfny.geoserver.requests.WFSRequest;


/**
 * Represents a lock request.
 *
 * @author Rob Hranac, TOPP <br>
 * @author Chris Holmes, TOPP
 * @version $Id: LockRequest.java,v 1.6 2004/02/09 23:29:41 dmzwiers Exp $
 */
public class LockRequest extends WFSRequest {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.wfs");
    private String handle;

    /** Specifies a lock expiration. */
    protected int expiry = -1;

    /** Specifices whether or not to lock all features grabbed in request */
    protected boolean lockAll = true;
    protected List locks = new ArrayList();

    /**
     * Empty constructor.
     */
    public LockRequest() {
        super();
        setRequest("LockFeature");
    }

    /**
     * Turn this request into a FeatureLock.
     * 
     * <p>
     * You will return FeatureLock.getAuthorization() to your user so they can
     * refer to this lock again.
     * </p>
     * 
     * <p>
     * The getAuthorization() value is based on getHandle(), with a default of
     * "GeoServer" if the user has not provided a handle.
     * </p>
     * The FeatureLock produced is based on expiry:
     * 
     * <ul>
     * <li>
     * negative expiry: reports if lock is available
     * </li>
     * <li>
     * zero expiry: perma lock that never expires!
     * </li>
     * <li>
     * postive expiry: lock expires in a number of minuets
     * </li>
     * </ul>
     * 
     *
     * @return
     */
    public FeatureLock toFeatureLock() {
        //String handle = getHandle();

        if ((handle == null) || (handle.length() == 0)) {
            handle = "GeoServer";
        }

        if (expiry < 0) {
            // negative time used to query if lock is available!
            return FeatureLockFactory.generate(handle, expiry);
        }

        if (expiry == 0) {
            // perma lock with no expiry!
            return FeatureLockFactory.generate(handle, 0);
        }

        // FeatureLock is specified in milli seconds
        return FeatureLockFactory.generate(handle, expiry * 60 * 1000);
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getHandle() {
        return handle;
    }

    /**
     * Gets whether lock request should fail if not all can be locked
     *
     * @return <tt>true</tt> If the request should fail if all are not locked.
     */
    public boolean getLockAll() {
        return lockAll;
    }

    /**
     * Sets whether lock request should fail if not all can be locked
     *
     * @param lockAll <tt>true</tt> If the request should fail if all are not
     *        locked.
     */
    public void setLockAll(boolean lockAll) {
        this.lockAll = lockAll;
    }

    /**
     * Gets the expiration of the locks (in minutes).
     *
     * @return An int of the expiry in minutes.
     */
    public int getExpiry() {
        return expiry;
    }

    /**
     * Sets the expiration of the locks (in minutes).
     *
     * @param expiry An int of the expiry in minutes.
     */
    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public int getExpirySeconds() {
        return expiry * 60;
    }

    /**
     * Gets a list of the locks held by this request(as LockRequest.Lock
     * objects)
     *
     * @return The list of the locks.
     */
    public List getLocks() {
        return locks;
    }

    /**
     * Adds a single lock to this request
     *
     * @param lock A Lock to be added to this request.
     */
    public void addLock(Lock lock) {
        this.locks.add(lock);
    }

    /**
     * Sets the lock list for this request.  Gets rid of the old lock list.
     *
     * @param locks The list of locks to add.
     */
    public void setLocks(List locks) {
        this.locks = locks;
    }

    /**
     * Sets the locks for this request according to the two lists passed in
     * Little error checking is done, as this is a convenience method for
     * LockKVP Reader, which does its own error checking.  None of the locks
     * created will have handles (kvp's don't have handles).
     *
     * @param typeList a list of featureTypes as Strings.
     * @param filterList a list of Filters.  Should either be null, in which
     *        case the locks have no filters, or else should be of the same
     *        length as the typeList.
     *
     * @task TODO: move error checking from KVP reader here.
     */
    public void setLocks(List typeList, List filterList) {
        Iterator typeIterator = typeList.listIterator();
        Iterator filterIterator = filterList.listIterator();
        this.locks = new ArrayList();

        while (typeIterator.hasNext()) {
            String curType = (String) typeIterator.next();
            Filter curFilter = null;

            if (filterIterator.hasNext()) {
                curFilter = (Filter) filterIterator.next();
            }

            addLock(curType, curFilter);
        }
    }

    /**
     * Creates a lock of the given featureType, filter and handle and adds it
     * to the lock list.
     *
     * @param featureType the typeName to lock.
     * @param filter which features of the featureType to lock.
     * @param handle the name to identify this lock, for error reporting.
     */
    public void addLock(String featureType, Filter filter, String handle) {
        Lock addLock = new Lock(featureType);
        addLock.setHandle(handle);
        addLock.setFilter(filter);
        this.locks.add(addLock);
    }

    /**
     * Creates a lock of the given featureType and filter and adds it to the
     * lock list.
     *
     * @param featureType the typeName to lock.
     * @param filter which features of the featureType to lock.
     */
    public void addLock(String featureType, Filter filter) {
        Lock addLock = new Lock(featureType);
        addLock.setFilter(filter);
        this.locks.add(addLock);
    }

    /* ***********************************************************************
     * Overrides of toString and equals methods.                             *
     * ***********************************************************************/
    public String toString() {
        StringBuffer returnString = new StringBuffer("\nLock Feature Request");
        String indent = "\n    ";
        returnString.append("\n LockAction: " + (lockAll ? "ALL" : "SOME"));
        returnString.append("\n exires in (min):" + expiry);

        Iterator lockIterator = locks.listIterator();

        while (lockIterator.hasNext()) {
            returnString.append(lockIterator.next().toString() + indent);
        }

        return returnString.toString();
    }

    public boolean equals(Object obj) {
        super.equals(obj);

        if (!(obj instanceof LockRequest)) {
            return false;
        }

        ListIterator internalIterator;
        ListIterator externalIterator;
        boolean isEqual = true;

        LockRequest lrequest = (LockRequest) obj;

        if (this.expiry == lrequest.getExpiry()) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        LOGGER.finest("checking expiry equality: " + isEqual);

        if (this.lockAll == lrequest.getLockAll()) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        LOGGER.finest("checking locking equality: " + isEqual);

        internalIterator = locks.listIterator();
        externalIterator = lrequest.getLocks().listIterator();

        while (internalIterator.hasNext()) {
            if (!externalIterator.hasNext()) {
                isEqual = false;
                LOGGER.finest("lock lists not same size");

                break;
            } else {
                Lock internalLock = (Lock) internalIterator.next();
                Lock externalLock = (Lock) externalIterator.next();
                LOGGER.finest("internal lock: " + internalLock);
                LOGGER.finest("external lock: " + externalLock);

                if (internalLock.equals(externalLock)) {
                    isEqual = true && isEqual;
                } else {
                    isEqual = false;
                }

                LOGGER.finest("Locks match: " + isEqual);
            }
        }

        return isEqual;
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
     *
     * @author Chris Holmes
     */
    public static class Lock {
        /** The feature types to lock. */
        protected String featureType = new String();

        /** Specifies the filter to define locked features. */
        protected Filter filter;

        /** The handle to identify this lock by. */
        protected String handle;

        public Lock(String featureType) {
            this.featureType = featureType;
        }

        /**
         * Gets the feature for this to lock.
         *
         * @return The name of the feature type to lock.
         */
        public String getFeatureType() {
            return featureType;
        }

        /**
         * Sets the feature for this to lock
         *
         * @param featureType The name of the feature type to lock.
         */
        public void setFeatureType(String featureType) {
            //filter out myns: type prefixes here?
            //or will SAXParser do that for us?
            this.featureType = featureType;
        }

        /**
         * Gets the filter of the features to lock.
         *
         * @return The filter to lock features with.
         */
        public Filter getFilter() {
            return filter;
        }

        /**
         * Sets the filter to define which features to lock
         *
         * @param filter The filter to lock features with.
         */
        public void setFilter(Filter filter) {
            this.filter = filter;
        }

        /**
         * Gets the handle that identifies this lock.
         *
         * @return The handle to identify this lock.
         */
        public String getHandle() {
            return handle;
        }

        /**
         * Sets the handle that identifies this
         *
         * @param handle The handle to identify this lock.
         */
        public void setHandle(String handle) {
            this.handle = handle;
        }

        /* *******************************************************************
         * Overrides of toString and equals methods.                         *
         * *******************************************************************/
        public String toString() {
            return ("\n Lock\n   typeName: " + featureType + "\n   handle: "
            + handle + "\n   filter: " + filter);
        }

        public boolean equals(Object obj) {
            if ((obj != null) && (obj.getClass() == this.getClass())) {
                Lock testLock = (Lock) obj;

                return (testField(this.handle, testLock.getHandle())
                && testField(this.featureType, testLock.getFeatureType())
                && testField(this.filter, testLock.getFilter()));
            } else {
                return false;
            }
        }
    }
}
