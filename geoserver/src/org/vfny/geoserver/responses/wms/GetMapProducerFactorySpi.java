/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import java.util.Set;

import org.geotools.factory.Factory;
import org.vfny.geoserver.responses.wms.map.GetMapProducer;

/**
 * @author Gabriel Roldan, Axios Engineering
 */
public interface GetMapProducerFactorySpi extends Factory {

	String getName();
	
    Set getSupportedFormats();

    boolean isAvailable();
	
	boolean canProduce(String mapFormat);
	
	GetMapProducer createMapProducer(String mapFormat)throws IllegalArgumentException;
}
