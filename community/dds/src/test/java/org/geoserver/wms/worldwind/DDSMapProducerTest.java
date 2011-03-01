package org.geoserver.wms.worldwind;

import junit.framework.Test;

import org.geoserver.wms.map.RenderedImageMapOutputFormat;
import org.geoserver.wms.map.RenderedImageMapOutputFormatTest;

public class DDSMapProducerTest extends RenderedImageMapOutputFormatTest {

	/**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new DDSMapProducerTest());
    }

    protected RenderedImageMapOutputFormat getProducerInstance() {
        return new RenderedImageMapOutputFormat("image/dds", getWMS());
    }
}
