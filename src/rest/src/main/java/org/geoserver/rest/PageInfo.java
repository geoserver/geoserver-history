/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

/**
 * An object which contains information about the "page" or "resource" being accessed
 * in a restlet request.
 * <p>
 * An instance of this class can be referenced by any restlet via:
 * <pre>
 * (PageDetails) request.getAttributes().get( PageDetails.KEY );
 * </pre>
 * </p>
 * @author Justin Deoliveira, OpenGEO
 *
 */
public class PageInfo {

    /**
     * key to reference this object by
     */
    public static final String KEY = "org.geoserver.pageDetails";

    /**
     * the root uri
     */
    String rootURI;
    /**
     * the base uri for the page.
     */
    String baseURI;
    /**
     * The full uri for the page.
     */
    String pageURI;
    /**
     * The extension of the page. 
     */
    String extension;
    
    PageInfo() {
    }

    public String getRootURI() {
        return rootURI;
    }
    public void setRootURI(String rootURI) {
        this.rootURI = rootURI;
    }
    
    public String getBaseURI() {
        return baseURI;
    }
    void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }
    
    public String getPageURI() {
        return pageURI;
    }
    void setPageURI(String pageURI) {
        this.pageURI = pageURI;
    }
    
    public String getExtension() {
        return extension;
    }
    void setExtension(String extension) {
        this.extension = extension;
    }
    
}
