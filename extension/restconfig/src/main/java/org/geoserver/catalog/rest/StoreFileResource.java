/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.catalog.rest;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.geoserver.catalog.Catalog;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.util.RESTUtils;
import org.geotools.util.logging.Logging;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

public abstract class StoreFileResource extends Resource {

    static Logger LOGGER = Logging.getLogger("org.geoserver.catalog.rest");
    
    protected Catalog catalog;
    
    public StoreFileResource( Request request, Response response, Catalog catalog) {
        super( null, request, response );
        this.catalog = catalog;
    }
    
    @Override
    public boolean allowPut() {
        return true;
    }
 
    protected File handleFileUpload(String store, String format, File directory) {
        getResponse().setStatus(Status.SUCCESS_ACCEPTED);

        MediaType mediaType = getRequest().getEntity().getMediaType();
        if(LOGGER.isLoggable(Level.INFO))
            LOGGER.info("PUT file, mimetype: " + mediaType );

        File uploadedFile = null;
        try {
            String method = (String) getRequest().getResourceRef().getLastSegment();
            if (method != null && method.toLowerCase().startsWith("file.")) {
                uploadedFile = RESTUtils.handleBinUpload(store + "." + format, directory, getRequest());
            }
            else if (method != null && method.toLowerCase().startsWith("url.")) {
                uploadedFile = RESTUtils.handleURLUpload(store, format, getRequest());
            }    
            else if (method != null && method.toLowerCase().startsWith("external.")) {
                uploadedFile = RESTUtils.handleEXTERNALUpload(getRequest());
            }
            else{
                final StringBuilder builder = 
                    new StringBuilder("Unrecognized file upload method: ").append(method);
                throw new RestletException( builder.toString(), Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } 
        catch (Throwable t) {
            throw new RestletException( "Error while storing uploaded file:", Status.SERVER_ERROR_INTERNAL, t );
        }
        
        //handle the case that the uploaded file was a zip file, if so unzip it
        if (mediaType!=null && RESTUtils.isZipMediaType( mediaType ) ) {
            //rename to .zip if need be
            if ( !uploadedFile.getName().endsWith( ".zip") ) {
                File newUploadedFile = new File( uploadedFile.getParentFile(), FilenameUtils.getBaseName(uploadedFile.getAbsolutePath()) + ".zip" );
                uploadedFile.renameTo( newUploadedFile );
                uploadedFile = newUploadedFile;
            }
            //unzip the file 
            try {
                RESTUtils.unzipFile(uploadedFile, directory );
                
                //look for the "primary" file
                //TODO: do a better check
                File primaryFile = findPrimaryFile( directory, format );
                if ( primaryFile != null ) {
                    uploadedFile = primaryFile;
                }
                else {
                    throw new RestletException( "Could not find appropriate " + format + " file in archive", Status.CLIENT_ERROR_BAD_REQUEST );
                }
            }
            catch( RestletException e ) {
                throw e;
            }
            catch( Exception e ) {
                throw new RestletException( "Error occured unzipping file", Status.SERVER_ERROR_INTERNAL, e );
            }
        }
        
        return uploadedFile;

    }

    protected File findPrimaryFile(File directory, String format) {
        File[] files = directory.listFiles();
        
        Iterator f = FileUtils.listFiles(directory, new String[]{ format }, false ).iterator(); f.hasNext();
        if ( f.hasNext() ) {
            //assume the first
            return (File) f.next();
        }
        
        return null;
    }

}
