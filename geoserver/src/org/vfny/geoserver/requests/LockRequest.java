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
 * Represents a lock request.
 *
 * @author Rob Hranac, TOPP
 * @version $version$
 */
public class LockRequest extends Request {

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");
    
    /** A  list of feature types to lock. */
    protected List featureTypes = new ArrayList();
        
    /** Specifies the a list of filters to define locked features. */
    protected List filters = new ArrayList();

    /** Specifies a lock expiration. */
    protected int expiry = -1;
    
    /** Specifices whether or not to lock all features grabbed in request */
    protected boolean lockAll = true;
    

    /** Empty constructor. */ 
    public LockRequest() { setRequest("LockFeature"); }


    /*************************************************************************
     * SIMPLE GET AND SET METHODS                                            *
     *************************************************************************/
    /** Sets the expiration of the locks (in minutes). */ 
    public List getFeatureTypes() { return featureTypes; }

    /** Sets the expiration of the locks (in minutes). */ 
    public void addFeatureType(String featureType) { 
        this.featureTypes.add(featureType); }

    /** Sets the expiration of the locks (in minutes). */ 
    public void setFeatureTypes(List featureTypes) { 
        this.featureTypes = featureTypes; }

    /** Sets the expiration of the locks (in minutes). */ 
    public List getFilters() { return filters; }

    /** Sets the expiration of the locks (in minutes). */ 
    public void addFilter(Filter filter) { this.filters.add(filter); }

    /** Sets the expiration of the locks (in minutes). */ 
    public void setFilters(List filters) { this.filters = filters; }

    /** Sets the expiration of the locks (in minutes). */ 
    public boolean getLockAll() { return lockAll; }

    /** Sets the expiration of the locks (in minutes). */ 
    public void setLockAll(boolean lockAll) { this.lockAll = lockAll; }

    /** Sets the expiration of the locks (in minutes). */ 
    public int getExpiry() { return expiry; }

    /** Sets the expiration of the locks (in minutes). */ 
    public void setExpiry(int expiry) { this.expiry = expiry; }

    /*************************************************************************
     * Overrides of toString and equals methods.                             *
     *************************************************************************/
    public String toString() {
        StringBuffer returnString = new StringBuffer("\nLock Feature Request");
        String indent = "\n    ";
        returnString.append("\n lock all features:" + lockAll);
        returnString.append("\n exires in (min):" + expiry);
        returnString.append("\n queries:" + indent);
        
        int featureSize = featureTypes.size();
        int filterSize = filters.size();
        
        // check for errors in the request
        if((featureSize > filterSize) && (filterSize == 1)) {
            Iterator featureIterator = featureTypes.listIterator();
            while( featureIterator.hasNext()) {
                returnString.append(featureIterator.next().toString() +indent);
                returnString.append(filters.get(0).toString() + indent);
            }
        } else if((featureSize > filterSize) && (filterSize == 0)) {
            Iterator featureIterator = featureTypes.listIterator();
            while( featureIterator.hasNext()) {
                returnString.append(featureIterator.next().toString() +indent);
            }
        } else if(filterSize == featureSize) {
            Iterator featureIterator = featureTypes.listIterator();
            Iterator filtersIterator = filters.listIterator();
            while( featureIterator.hasNext()) {
                returnString.append(featureIterator.next().toString() +indent);
                returnString.append(filtersIterator.next().toString() +indent);
            }
        } else if((featureSize == 1) && (filterSize == 0)) {
            returnString.append(featureTypes.get(0).toString() + " \n");
        }

        //returnString.append("\n inside: " + filter.toString());

        return returnString.toString();
    }

    public boolean equals(LockRequest request) {

        ListIterator internalIterator;
        ListIterator externalIterator;
        boolean isEqual = true;

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

        internalIterator = filters.listIterator();
        externalIterator = request.getFilters().listIterator();
        while( internalIterator.hasNext()) {
            if( !externalIterator.hasNext()) {
                isEqual = false;
                LOGGER.finest("filter lists not same size");
                break;
            } else {
                Filter internalFilter = (Filter) internalIterator.next();
                Filter externalFilter = (Filter) externalIterator.next();
                LOGGER.finest("internal filter: " + internalFilter);
                LOGGER.finest("external filter: " + externalFilter);
                if(internalFilter.equals(externalFilter)) {
                    isEqual = true && isEqual;
                } else {
                    isEqual = false;
                }
                LOGGER.finest("filters match: " + isEqual);
            }
        }

        internalIterator = featureTypes.listIterator();
        externalIterator = request.getFeatureTypes().listIterator();
        while( internalIterator.hasNext()) {
            if( !externalIterator.hasNext()) {
                isEqual = false;
                LOGGER.finest("feature types not same size");
                break;
            } else {
                if(((String) internalIterator.next()).
                    equals((String) externalIterator.next())) {
                    isEqual = true && isEqual;
                    LOGGER.finest("feature types match: " + isEqual);
                } else {
                    isEqual = false;
                    LOGGER.finest("feature types not equal");
                    break;
                }
            }
        }
        return isEqual;
    }
}
