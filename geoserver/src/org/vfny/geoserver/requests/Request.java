/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

/**
 * Defines a general Request type and provides accessor methods for unversal 
 * request information.
 * 
 * @version $VERSION$
 * @author Rob Hranac, TOPP
 */
abstract public class Request {

    /** Request service */
    protected String service = "WFS";
    
    /** Request type */
    protected String request = new String();
    
    /** Request version */
    protected String version = new String();
    
    
    /** Empty constructor. */
    public Request () {}
    
    
    /** Gets requested service. */
    public String getService() { return this.service; }

    /** Gets requested service. */
    public void setService(String service) { this.service = service; }


    /** Gets requested request type. */
    public String getRequest() { return this.request; }
        
    /** Sets requested request type. */
    public void setRequest(String reqeust) { this.request = request; }
    
    
    /** Return version type. */
    public String getVersion() { return this.version; }
    
    /** Sets version type. */
    public void setVersion(String version) { this.version = version; }    
}
