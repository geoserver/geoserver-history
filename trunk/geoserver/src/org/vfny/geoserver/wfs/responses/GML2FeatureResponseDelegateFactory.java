/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.responses;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set; 

import org.vfny.geoserver.wfs.FeatureResponseDelegateProducerSpi;




/**
 * 
 */
public class GML2FeatureResponseDelegateFactory implements FeatureResponseDelegateProducerSpi 
{
	
	static HashSet supportedFormats = new HashSet();
	
	static{
		supportedFormats.add("GML2");
		supportedFormats.add("GML2-GZIP");
	}

    /**
     * Creates a new GifMapProducerFactory object.
     */
    public GML2FeatureResponseDelegateFactory() {
        super();
    }

    /**
     * see interface def'n
     */
    public String getName() {
        return "GML2 Ouput (uncompressed or gziped)";
    }

    /**
     * Returns the Set of output format this producer supports
     *
     * @return 
     */
    public Set getSupportedFormats() 
    {
    	 return supportedFormats;
    }

    /**
     *
     * @return <code>true</code>
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns wether the  producers created by this factory can create output
     * in the passed output format.
     *
     * @param format a MIME type string to check if this producer is able to
     *        handle.
     *
     * @return <code>true</code>  or false
     */
    public boolean canProduce(String outputFormat) {
        return "GML2".equalsIgnoreCase(outputFormat)
        || "GML2-GZIP".equalsIgnoreCase(outputFormat);
    }

    
    
    /* (non-Javadoc)
	 * @see org.geotools.factory.Factory#getImplementationHints()
	 * This just returns java.util.Collections.EMPTY_MAP
	 */
	public Map getImplementationHints() {
		return java.util.Collections.EMPTY_MAP;
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wfs.FeatureResponseDelegateProducerSpi#createFeatureDelegateProducer(java.lang.String)
	 */
	public FeatureResponseDelegate createFeatureDelegateProducer(String format) throws IllegalArgumentException 
	{
		if (canProduce(format))
			return new GML2FeatureResponseDelegate();
		throw new IllegalArgumentException("cannot produce "+format);
	}
	
}
