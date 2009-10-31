package org.vfny.geoserver.wms.responses.map.worldwind;

import junit.framework.Test;

import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import org.vfny.geoserver.wms.responses.map.DefaultRasterMapProducerTest;

public class DDSMapProducerTest extends DefaultRasterMapProducerTest  {
	/**
    * This is a READ ONLY TEST so we can use one time setup
    */
   public static Test suite() {
       return new OneTimeTestSetup(new DDSMapProducerTest());
   }
   
   protected DefaultRasterMapProducer getProducerInstance() {
       return new DDSMapProducer(getWMS()); 
   }
}
