/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Creates an encoder for a specific getfeature results output format
 * 
 * <p>
 * this opens the doors for future additions of new output formats (BXML?), by
 * decoupling the execution from the response generation of a getfeature
 * request
 * </p>
 *
 * @author Gabriel Roldán
 * @version $Id: FeatureResponseDelegateFactory.java,v 1.2 2004/03/12 10:19:44 cholmesny Exp $
 */
public class FeatureResponseDelegateFactory {
    /** DOCUMENT ME! */
    private static final List encoders = new LinkedList();

    static {
        encoders.add(new GML2FeatureResponseDelegate());
        encoders.add(new GML2AppSchemaFeatureResponseDelegate());
    }

    private FeatureResponseDelegateFactory() {
    }

    /**
     * Creates an encoder for a specific getfeature results output format
     *
     * @param outputFormat DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public static FeatureResponseDelegate encoderFor(String outputFormat)
        throws NoSuchElementException {
        FeatureResponseDelegate encoder = null;

        for (Iterator it = encoders.iterator(); it.hasNext();) {
            encoder = (FeatureResponseDelegate) it.next();

            if (encoder.canProduce(outputFormat)) {
                try {
                    if (encoder != null) {
                        return (FeatureResponseDelegate) encoder.getClass()
                                                                .newInstance();
                    }
                } catch (Exception ex) {
                    throw new NoSuchElementException(
                        "Can't create the encoder "
                        + encoder.getClass().getName());
                }
            }
        }

        throw new NoSuchElementException("Can't find an encoder for "
            + outputFormat + " output format");
    }
    
    /**
     * Prioritises encoder selection; returns a structured GML2 encoder if  
     * feature type attribute to XML element mappings are available.
     * Currently this is an all or nothing approach, so if there are
     * multiple queries, the target feature types must all have a schema.xml
     * supporting feature type attribute to XML element mapping.
     * 
     * If Outputformat is not GML2 or GML2-ZIP or useXpathMapping is not true
     * then returns FeatureResponseDelegate encoderFor(String outputFormat)
     * 
     * This is mainly a hack to make life easier for WFS clients, who only 
     * know to request GML2 
     *
     * @param outputFormat the outputformat passed in the getFeature request
     * @param useXpathMapping set to true if structured GML2 is required
     * 
     * @return FeatureResponseDelegate wrapping an encoder
     *
     * @throws NoSuchElementException if a mathcing encoder cannot be found
     */
    public static FeatureResponseDelegate encoderFor(
    		String outputFormat, boolean useXpathMapping)
        throws NoSuchElementException {
    
    	if (	("GML2".equalsIgnoreCase(outputFormat) ||
    			"GML2-GZIP".equalsIgnoreCase(outputFormat)) &&
				useXpathMapping) {
    		
    		return new GML2AppSchemaFeatureResponseDelegate();
    	} else {
    		return encoderFor(outputFormat);
    	}
    }    
}

