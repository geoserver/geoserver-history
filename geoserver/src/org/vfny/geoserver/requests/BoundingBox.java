/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.requests;

import java.util.*;

import org.apache.log4j.Category;


/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.1 structured
 * XML docs.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 beta, 11/01/01
 *
 */
public class BoundingBox {

    
    // NOTE THAT THIS IMPLEMENTATION IS A PLACEHOLDER FOR A MORE
    // SOPHISTICATED APPROACH THAT IS YET TO BE DETERMINED.
    // RIGHT NOW IT JUST WRAPS A STRING
    
    /** Standard logging instance for the class */
    private Category _log = Category.getInstance(BoundingBox.class.getName());
    
    /** The user-specified name for the query. */
    private String internalRepresentation = "";
    
    
    /**
     * Empty constructor.
     *
     */ 
    public BoundingBox() {
    }
    
    
    /**
     * Constructor with an internal representation of the bounding box.
     *
     * @param internalRepresentation Holds an internal representation of the bounding box.
     */ 
    public BoundingBox(String internalRepresentation) {        
        this.internalRepresentation = internalRepresentation;
    }
    
    
    /**
     * Gets the bounding box SQL.
     *
     */ 
    public String getSQL() {        
        return this.internalRepresentation;
    }

    
    /**
     * Passes the Post method to the Get method, with no modifications.
     *
     * @param internalRepresentation Holds an internal representation of the bounding box.
     */ 
    public void setCoordinates(String internalRepresentation) {				
        this.internalRepresentation = internalRepresentation;
    }
    
    
    /**
     * Gets the bounding box coordinates.
     *
     */ 
    public String getCoordinates() {        
        return this.internalRepresentation;
    }
    
    
    /**
     * Checks to see if a bounding box has been set.
     *
     */ 
    public boolean isSet() {        
        if( this.internalRepresentation.equals("") )
            return false;
        else
            return true;
    }
}
