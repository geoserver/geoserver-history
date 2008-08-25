package org.vfny.geoserver.wms;

import java.util.List;

import junit.framework.Test;

import org.geoserver.wms.DefaultWebMapServiceTest;
import org.geoserver.wms.WMSExtensions;
import org.geoserver.wms.WMSTestSupport;

/**
 * An integration test for the GetMapProducer implementations
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 */
public class GetMapProducerTest extends WMSTestSupport {

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new DefaultWebMapServiceTest());
    }
    
    public void testGetOutputFormatNames() {
        List<GetMapProducer> producers = WMSExtensions.findMapProducers(applicationContext);
        for (GetMapProducer producer : producers) {
            List<String> outputFormats = producer.getOutputFormatNames();
            assertNotNull(outputFormats);
            assertTrue(outputFormats.size() > 0);
            for (String oformat : outputFormats) {
                assertNotNull(oformat);
            }
        }
    }

    public void testGetOutputFormat() {
        List<GetMapProducer> producers = WMSExtensions.findMapProducers(applicationContext);
        for (GetMapProducer producer : producers) {
            assertNotNull(producer.getOutputFormat());
        }
    }

    public void testSetOutputFormat() {
        List<GetMapProducer> producers = WMSExtensions.findMapProducers(applicationContext);
        for (GetMapProducer producer : producers) {
            List<String> outputFormats = producer.getOutputFormatNames();
            for (String outputFormat : outputFormats) {
                producer.setOutputFormat(outputFormat);
                String producerFormat = producer.getOutputFormat();
                String msg = producer.getClass().getName() + " output format not set";
                assertEquals(msg, outputFormat, producerFormat);
            }

            try {
                producer.setOutputFormat("not-a-valid-output-format");
                fail(producer.getClass().getName() + " didn't throw an IAE when an invalid "
                        + "output format was set");
            } catch (IllegalArgumentException e) {
                assertTrue(true);
            }
        }
    }

}
