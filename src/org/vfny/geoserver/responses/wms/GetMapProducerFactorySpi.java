/*
 * Created on Feb 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
