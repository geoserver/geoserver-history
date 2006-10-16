/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.ows.ServiceException;


/**
 * Encodes features into a particular output format.
 * 
 * <p>
 * An implementation of this interface reports wich output formats can produce
 * through its <code>canProduce()</code> method. So, when FeatureResponse
 * finds a capable implementation based on the requested output format, it
 * will pass the results of the query to the <code>prepare(String
 * outputFormat, GetFeatureResults results)</code> method, and later will ask
 * for the traslation of that results to the desired outputformat using
 * <code>encode(OutputStream)</code>
 * </p>
 *
 * @author Gabriel Rold?n
 * @author Justin Deoliveira
 * 
 * @version $Id$
 */
public interface FeatureProducer {
	
	/**
	 * @return The names output formats supported by the produce.
	 */
	Set getOutputFormats();
	
	/**
	 * Returns any special content encoding.
	 * <p>
	 * This method is mostly used in the header of an http-response to specify 
	 * a compressed content encoding, such as "gzip", or "deflate". This method
	 * should return <code>null</code> if no special content encoding is being
	 * used.
	 * </p>
	 * 
	 * @return The content encoding, or <code>null</code>
	 */
	String getContentEncoding();
	
	/**
	 * @return the mime type of hte content being returned.
	 */
	String getMimeType();
	
	/**
     * Produces the features. 
     * <p>
     * This method is never called unless canProduce( outputFormat ) returns
     * true.
     * </p>
     * 
     * @param outputFormat The output format being used.
     * @param results The wfs feature collection.
     * @param output The output stream to encode features to.
     * 
     * @throws ServiceException Any errors that occur while interacting with the
     * service.
     * @throws IOException Any I/O errors that occur.
     */
    void produce( String outputFormat, FeatureCollectionType results, OutputStream output ) 
    		throws ServiceException, IOException;

}
