/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import java.util.Map;

import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

/**
 * Abstract base class for resources.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 */
public abstract class AbstractResource extends Resource {

    /**
     * list of formats used to read and write representations of the resource.
     */
    protected Map<String,DataFormat> formats;
    
    /**
     * Constructs a new resource from contex, request, and response.
     */
    protected AbstractResource( Context context, Request request, Response response ) {
        super( context, request, response );
    }

    /**
     * Constructs a new resource.
     * <p>
     * When using this constructor, {@link #init(Context, Request, Response)} needs to be 
     * called before any other operaions.
     * </p>
     */
    protected AbstractResource() {
    }
    
    /**
     * Creates the map of formats used to create and read representations of the resource.
     * <p>
     * Keys in the map are file extensions (examples: xml,json), and mime types (examples: text/xml,
     * text/json). Values are instances of {@link DataFormat}. The null key is used to identify the 
     * default format. The default format is used when a request does not specify a format explicitly.
     *  </p>
     */
    protected abstract Map<String, DataFormat> createSupportedFormats(Request request,Response response);
    
    /**
     * Returns the format to use to serialize during a GET request.
     * <p>
     * The format is located by checking the "format" or "type" request attribute, ie 
     * <pre>getRequest().getAttributes()</pre>. These values originate from the route 
     * template used to find this resource. If no such attributes are found the default 
     * format is used.
     * </p>
     */
    protected DataFormat getFormatGet() {
        String format = null;
        format = (String) getRequest().getAttributes().get( "format");
        if ( format == null) {
            format = (String) getRequest().getAttributes().get( "type" );
        }
        if ( format == null ) {
            //try from the resource uri
            String uri = getRequest().getResourceRef() != null ? 
                getRequest().getResourceRef().getLastSegment() : null;
            if ( uri != null ) {
                String ext = ResponseUtils.getExtension(uri);
                if ( ext != null ) {
                    format = ext;
                }
            }
        }
        
        if ( format == null && !getFormats().containsKey(null) ) {
            String msg = "No format supplied and no default available. Try specifiying a file extension."; 
            throw new RestletException( msg, Status.CLIENT_ERROR_BAD_REQUEST );
        }
        return getFormats().get( format ); 
    }

    /**
     * returns the format to use to de-serialize during a POST or PUT request.
     * <p>
     * The format is located by looking up <pre>getRequest().getEntity().getMediaType()</pre> from
     * the map created by {@link #createSupportedFormats()}. If no such format is found the "default"
     * format is by looking up the null entry.
     * </p>
     */
    protected DataFormat getFormatPostOrPut() {
        MediaType type = getRequest().getEntity().getMediaType();
        if ( type != null ) {
            DataFormat format = getFormats().get( type.toString() );
            if ( format == null ) {
                //check the sub type
                String sub = type.getSubType();
                if ( sub != null ) {
                    format = getFormats().get( sub );
                    if ( format == null ) {
                        //check for sub type specified with '+'
                        int plus = sub.indexOf( '+' );
                        if ( plus != -1 ) {
                            sub = sub.substring(0,plus);
                            format = getFormats().get( sub );
                        }
                    }
                }
            }
            
            if ( format != null ) {
                return format;
            }
        }
        
        throw new RestletException( "Could not determine format. Try setting the Content-type header.",
            Status.CLIENT_ERROR_BAD_REQUEST );
    }
    
    /**
     * Accessor for formats which lazily creates the format map.
     */
    protected Map<String,DataFormat> getFormats() {
        if ( formats == null ) {
            synchronized (this) {
                if ( formats == null ) {
                    formats = createSupportedFormats(getRequest(), getResponse());
                }
            }
        }
        return formats;
    }
    
    /**
     * Returns the object which contains information about the page / resource bring requested. 
     * <p>
     * This object is created by the rest dispatcher when a request comes in.  
     * </p>
     */
    protected PageInfo getPageInfo() {
        return (PageInfo) getRequest().getAttributes().get( PageInfo.KEY );
    }
}
