package org.geoserver.wms.worldwind;

import java.awt.Color;
import java.util.logging.Logger;

import junit.framework.Test;

public class DDSMapProducerTest extends DefaultRasterMapProducerTest {

	/** DOCUMENT ME! */
	private static final Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(DDSMapProducerTest.class.getPackage().getName());

	private DefaultRasterMapProducer rasterMapProducer;
	
	/** DOCUMENT ME! */
    private static final Color BG_COLOR = Color.white;
    
    /** DOCUMENT ME! */
    private String mapFormat = "image/dds";

	/**
	 * This is a READ ONLY TEST so we can use one time setup
	 */
	public static Test suite() {
		return new OneTimeTestSetup(new DDSMapProducerTest());
	}

	protected DefaultRasterMapProducer getProducerInstance() {
		return new DDSMapProducer(getWMS());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void setUpInternal() throws Exception {
	    super.setUpInternal();
	    this.rasterMapProducer = getProducerInstance();
	}
	
	public String getMapFormat()
	{
		return this.mapFormat;
	}
}
