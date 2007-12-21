/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.htmlimagemap;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * GetMapProducerFactorySpi that creates an HTMLImageMap GetMapProducer.
 * An HTMLImageMap GetMapProducer is able to render a map as an HTML 4.0 ImageMap,
 * useful as an overlay to a raster map to introduce interactive behaviour to the map.
 * @author Mauro Bartolomeoli
 */
public class HTMLImageMapMapProducerFactory implements GetMapProducerFactorySpi {
    
   
	/**
     * The ImageMap is served as text/html: it is an HTML fragment, after all.
     * TODO: Verify if this doesn't conflict with other map producers.   
     */
    static final String MIME_TYPE = "text/html";
    
    private static final Set SUPPORTED_FORMATS = Collections.singleton(MIME_TYPE);

    /**
     * Creates a new HTMLImageMapMapProducerFactory object.
     */
    public HTMLImageMapMapProducerFactory() {
        super();
    }

    /**
     * Gets a verbose description for the GetMapProducer.
     *
     * @return the verbose description of the GetMapProducer
     */
    public String getName() {
        return "HTML ImageMap (SVG) map producer";
    }

    /**
     * Gets the list of formats supported by this GetMapProducer.
     * text/html is the only supported format.
     * @return the GetMapProducer supported format(s) (text/html)
     */
    public Set getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }

    /**
     * The GetMapProducer is always available.
     *
     * @return <code>true</code>
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * evaluates if this Map producer can generate the map format
     * specified by <code>mapFormat</code><p>In this case, true if
     * <code>mapFormat</code> is text/html
     * 
     * @param mapFormat the mime type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format.
     */
    public boolean canProduce(String mapFormat) {
        return (mapFormat != null) && mapFormat.equals(MIME_TYPE);
    }

    /**
     * Returns an HTMLImageMap GetMapProducer based on the current wms configuration.
     *
     * @param mapFormat requested mapFormat (text/html)
     * @param wms wms service configuration object
     *
     * @return an HTMLImageMapMapProducer
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException {
    			
    	return new HTMLImageMapMapProducer();		
    }

    /* (non-Javadoc)
     * @see org.geotools.factory.Factory#getImplementationHints()
     * This just returns java.util.Collections.EMPTY_MAP
     */
    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }
}
