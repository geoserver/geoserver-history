/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wfs;

import org.vfny.geoserver.*;
import org.vfny.geoserver.requests.*;
import java.util.*;
import java.util.logging.*;


/**
 * Implements the WFS GetFeatureWithLock interface, which  responds to requests
 * for GML and locks the features. It extends GetFeature with the ability to
 * turn itself into a lock request, and with an expiry element.  The
 * lockAction didn't make it in to the 1.0 spec, as far as I can tell, but
 * will likely be a part of the next one, so that will have to be added, but
 * should be trivial, as it's already a part of lockRequest.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: FeatureWithLockRequest.java,v 1.1.2.1 2003/11/04 22:48:26 cholmesny Exp $
 */
public class FeatureWithLockRequest extends FeatureRequest {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** The time to hold the lock for */
    protected int expiry = -1;

    public FeatureWithLockRequest() {
        super();
    }

    /**
     * Turns this request into a lock request.
     *
     * @return the LockRequest equivalent of this request.
     *
     * @task REVISIT: we could also do something tricky with interfaces, have
     *       query implement a Lock interface, but this way got things working
     *       a lot faster, even if it's not as pretty.
     */
    public LockRequest asLockRequest() {
        LockRequest request = new LockRequest();
        request.setExpiry(expiry);

        for (Iterator i = queries.iterator(); i.hasNext();) {
            Query curQuery = (Query) i.next();
            request.addLock(curQuery.getTypeName(), curQuery.getFilter(),
                curQuery.getHandle());
        }

        LOGGER.finest("returning: " + super.toString() + " as " + request);

        return request;
    }

    /**
     * Gets the expiration of the locks (in minutes).
     *
     * @return How many minutes till the lock should expire.
     */
    public int getExpiry() {
        return expiry;
    }

    /**
     * Sets the expiration of the locks (in minutes).
     *
     * @param expiry How many minutes till the lock should expire.
     */
    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public String toString() {
        StringBuffer returnString = new StringBuffer("\nGetFeatureWithLock");
        returnString.append(": " + handle);
        returnString.append("\n output format:" + outputFormat);
        returnString.append("\n max features:" + maxFeatures);
        returnString.append("\n version: " + version);
        returnString.append("\n queries: ");
        returnString.append("\n expiry: " + expiry);

        Iterator iterator = queries.listIterator();

        while (iterator.hasNext()) {
            returnString.append(iterator.next().toString() + " \n");
        }

        //returnString.append("\n inside: " + filter.toString());
        return returnString.toString();
    }
}
