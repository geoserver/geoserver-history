/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

import java.util.*;
import java.util.logging.Logger;

/**
 * This utility defines a general Request type and provides accessor methods 
 * for unversal request information.
 * 
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class WfsException extends Exception {
    
    /** message inserted by GeoServer as to what it thinks happened */
    protected String preMessage = new String();
    
    /** full classpath of originating GeoServer class */
    protected String locator = new String();
    
    /** the standard exception that was thrown */
    protected Exception standardException = new Exception();
    
    /** Class logger */
    private static Logger LOGGER =  
        Logger.getLogger("org.vfny.geoserver.responses");
    
    
    /** Empty constructor. */
    public WfsException () { super(); }
        
    /**
     * Empty constructor.
     * @param message The message for the .
     */
    public WfsException (String message) {
        super( message );
        LOGGER.fine( this.getMessage() );
    }
    
    
    /**
     * Empty constructor.
     * @param message The message for the .
     */
    public WfsException (Exception e) {
        super( e.getMessage() );
        LOGGER.fine( this.getMessage() );             
    }
    
    
    /**
     * Empty constructor.
     * @param message The message for the .
     * @param locator The message for the .
     */
    public WfsException (String message, String locator) {
        super(message);
        this.locator = locator;
        LOGGER.fine( "> [" + this.locator + "]:\n  " + this.getMessage());
    }
    
    
    /**
     * Empty constructor.
     * @param message The message for the .
     * @param locator The message for the .
     */
    public WfsException (Exception e, String preMessage, String locator) {
        super( e.getMessage() );
        this.preMessage = preMessage;               
        this.locator = locator;
        LOGGER.fine( "> [" + this.locator + "]:\n  " + 
                     this.preMessage + ": " + this.getMessage());
    }
    
    
    /**
     * Return request type.
     */
    public String getXmlResponse () {
        String returnXml;
        returnXml = "<WFS_Exception>\n";
        returnXml = returnXml + "   <Exception>\n";
        returnXml = returnXml + "      <Locator>" + this.locator + 
            "</Locator>\n";
        returnXml = returnXml + "      <Message>" + this.preMessage + ": " + 
            this.getMessage() + "</Message>\n";
        returnXml = returnXml + "   </Exception>\n";
        returnXml = returnXml + "</WFS_Exception>\n";
        return returnXml;
    }    
}
