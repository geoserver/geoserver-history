package org.vfny.geoserver.responses.wms;

import java.util.Set;

import org.geotools.factory.Factory;

public interface GetLegendGraphicProducerSpi extends Factory {

	String getName();
	
    Set getSupportedFormats();

    boolean isAvailable();
	
	boolean canProduce(String mimeType);
	
	GetLegendGraphicProducer createLegendProducer(String mapFormat)throws IllegalArgumentException;
}
