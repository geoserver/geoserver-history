/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.util.*;

/**
 * This class enforces a standard interface for GetCapabilities reqeusts
 * 
 * @version $VERSION$
 * @author Rob Hranac, TOPP
 */
public class CapabilitiesRequest 
    extends Request {

    /** Empty constructor. */
    public CapabilitiesRequest () { super(); }


    /*************************************************************************
     * Overrides of toString and equals methods.                             *
     *************************************************************************/
    public String toString() {
        return "GetCapabilities [service: " + 
            service + ", version: " + version + "]";
    }   

    public boolean equals(CapabilitiesRequest request) {
        return this.getService().equals(request.getService()) &&
            this.getVersion().equals(request.getVersion());
    }   
}
