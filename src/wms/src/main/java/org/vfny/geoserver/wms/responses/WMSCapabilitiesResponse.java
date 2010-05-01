/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSExtensions;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.WMSCapabilitiesRequest;
import org.vfny.geoserver.wms.responses.helpers.WMSCapsTransformer;


/**
 * Processes a WMS GetCapabilities request.
 * <p>
 * The response of a GetCapabilities request is general information about the
 * service itself and specific information about the available maps.
 * </p>
 *
 * @author Gabriel Roldan (Axios Engineering, TOPP)
 * @version $Id$
 */
public class WMSCapabilitiesResponse implements Response {
    /** package's logger */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(WMSCapabilitiesResponse.class.getPackage()
                                                                                       .getName());

    /**
     * Byte array holding the raw content of the capabilities document,
     * generated in <code>execute()</code>
     */
    private byte[] rawResponse;

    /**
     * List of formats accessible via a GetMap request.
     */
    private Set<String> mapFormats;
    private ApplicationContext applicationContext;

    public WMSCapabilitiesResponse(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.mapFormats = loadImageFormats(applicationContext);
    }
    
    /**
     * Grabs the list of available MIME-Types for the GetMap operation from the 
     * set of {@link GetMapProducer}s registered in the application context.
     * 
     * @param applicationContext
     *            The application context where to grab the GetMapProducers from.
     * @see GetMapProducer#getContentType()
     */
    private static Set<String> loadImageFormats(final ApplicationContext applicationContext) {
        final Collection<GetMapProducer> producers = WMSExtensions.findMapProducers(applicationContext);
        final Set<String> formats = new HashSet<String>();

        for (GetMapProducer producer : producers) {
            formats.addAll(producer.getOutputFormatNames());
        }

        return formats;
    }
    

    /**
    * Returns any extra headers that this service might want to set in the HTTP response object.
    * @see org.vfny.geoserver.Response#getResponseHeaders()
    */
    public HashMap getResponseHeaders() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     * @throws WmsException DOCUMENT ME!
     */
    public void execute(Request request) throws ServiceException {
        if (!(request instanceof CapabilitiesRequest)) {
            throw new IllegalArgumentException("Not a GetCapabilities Request");
        }
        
        //UpdateSequence handling for WMS:  see WMS 1.1.1 page 23
        final WMSCapabilitiesRequest capreq = (WMSCapabilitiesRequest)request;
        final WMS wmsConfig = capreq.getWMS();
        
        long reqUS = -1;
        if (capreq.getUpdateSequence() != null && !"".equals(capreq.getUpdateSequence().trim())) {
	        try {
	        	reqUS = Long.parseLong(capreq.getUpdateSequence());
	        } catch (NumberFormatException nfe) {
	        	throw new ServiceException("GeoServer only accepts numbers in the updateSequence parameter");
	        }
        }
        long geoUS = wmsConfig.getUpdateSequence();
    	if (reqUS > geoUS) {
    		throw new org.geoserver.platform.ServiceException("Client supplied an updateSequence that is greater than the current sever updateSequence","InvalidUpdateSequence");
    	}
    	if (reqUS == geoUS) {
    		throw new org.geoserver.platform.ServiceException("WMS capabilities document is current (updateSequence = " + geoUS + ")","CurrentUpdateSequence");
    	}
    	//otherwise it's a normal response...
        

        Set<String> legendFormats = GetLegendGraphicResponse.getFormats();
        WMSCapsTransformer transformer = new WMSCapsTransformer(request.getBaseUrl(), mapFormats, legendFormats);

        // if (request.getWFS().getGeoServer().isVerbose()) {
        transformer.setIndentation(2);
        final WMS wms = (WMS)applicationContext.getBean("wms");
        final Charset encoding = wms.getCharSet();
        transformer.setEncoding(encoding);

        // }
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            transformer.transform(request, out);
        } catch (TransformerException e) {
            throw new WmsException(e);
        }

        this.rawResponse = out.toByteArray();
    }

    /**
     * Returns the fixed capabilities MIME type  (application/vnd.ogc.wms_xml)
     * as specified in whe WMS spec, version 1.1.1, section 6.5.3, table 3.
     *
     * @param gs DOCUMENT ME!
     *
     * @return the capabilities document MIME type.
     *
     * @throws IllegalStateException if the response was not yet produced.
     */
    public String getContentType(GeoServer gs) throws IllegalStateException {
        if (rawResponse == null) {
            throw new IllegalStateException("execute() not called or not succeed.");
        }

        return WMSCapsTransformer.WMS_CAPS_MIME;
    }

    /**
     * Just returns <code>null</code>, since no special encoding is applyed to
     * the output data.
     *
     * @return <code>null</code>
     */
    public String getContentEncoding() {
        return null;
    }

    /**
     * Writes the capabilities document generated in <code>execute()</code> to
     * the given output stream.
     *
     * @param out the capabilities document destination
     *
     * @throws ServiceException never, since the whole content was aquired in
     *         <code>execute()</code>
     * @throws IOException if it is thrown while writing to <code>out</code>
     * @throws IllegalStateException if <code>execute()</code> was not
     *         called/succeed before this method is called.
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        if (rawResponse == null) {
            throw new IllegalStateException("No raw response presents!");
        }

        out.write(rawResponse);
    }

    /**
     * Does nothing, since no processing is done after <code>execute()</code>
     * has returned.
     *
     * @param gs the service instance
     */
    public void abort(ServiceInfo gs) {
        //not really much to do
    }

    /*
     * (non-Javadoc)
     *
     * @see org.vfny.geoserver.Response#getContentDisposition()
     */
    public String getContentDisposition() {
        return null;
    }
}
