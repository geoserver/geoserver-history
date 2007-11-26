/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.util;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.geoserver.platform.ServiceException;
import org.geotools.util.Version;

/**
 * This utility class embodies the capabilities resolution algorithm for every service to reuse 
 * @author Justin Deoliveira, TOPP
 * @author Andrea Aime, TOPP
 *
 */
public class CapabilitiesUtils {
    
    /**
     * Given a list of provided versions, and a list of accepted versions, this method will
     * return 
     * @param providedList a non null, non empty list of provided versions (in "x.y.z" format)
     * @param acceptedList a list of accepted versions, eventually null or empty (in "x.y.z" format)
     * @return the negotiated version to be used for response
     */
    public static String getVersion(List<String> providedList, List<String> acceptedList) {
        //first figure out which versions are provided
        TreeSet<Version> provided = new TreeSet<Version>();
        for (String v : providedList) {
            provided.add(new Version(v));
        }
        
        // if no accept list provided, we return the biggest
        if(acceptedList == null || acceptedList.isEmpty())
            return provided.last().toString();

        //next figure out what the client accepts (and check they are good version numbers)
        TreeSet<Version> accepted = new TreeSet<Version>();
        for (String v : acceptedList) {
            if (!v.matches("[0-99]\\.[0-99]\\.[0-99]")) {
                String msg = v + " is an invalid version numver";
                throw new ServiceException(msg, "VersionNegotiationFailed");
            }
            
            accepted.add(new Version(v));
        }

        // prune out those not provided
        for (Iterator<Version> v = accepted.iterator(); v.hasNext();) {
            Version version = (Version) v.next();

            if (!provided.contains(version)) {
                v.remove();
            }
        }

        // lookup a matching version
        String version = null;
        if (!accepted.isEmpty()) {
            //return the highest version provided
            version = ((Version) accepted.last()).toString();
        } else {
            for (String v : acceptedList) {
                accepted.add(new Version(v));
            }

            //if highest accepted less then lowest provided, send lowest
            if ((accepted.last()).compareTo(provided.first()) < 0) {
                version = (provided.first()).toString();
            }

            //if lowest accepted is greater then highest provided, send highest
            if ((accepted.first()).compareTo(provided.last()) > 0) {
                version = (provided.last()).toString();
            }

            if (version == null) {
                //go through from lowest to highest, and return highest provided 
                // that is less than the highest accepted
                Iterator<Version> v = provided.iterator();
                Version last = v.next();

                for (; v.hasNext();) {
                    Version current = v.next();

                    if (current.compareTo(accepted.last()) > 0) {
                        break;
                    }

                    last = current;
                }

                version = last.toString();
            }
        }
        
        return version;
    }
}
