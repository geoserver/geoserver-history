/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.vfny.geoserver.testdata.AbstractCiteDataTest;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Tests that DefaultRasterMapProducerTest, which is based on LiteRenderer,
 * correctly generates a raster map.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: DefaultRasterMapProducerTest.java 6797 2007-05-16 10:23:50Z
 *          aaime $
 */
public class DefaultRasterMapProducerTest extends AbstractCiteDataTest {
	/** DOCUMENT ME! */
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DefaultRasterMapProducerTest.class.getPackage()
					.getName());

	/** DOCUMENT ME! */
	private static final FilterFactory filterFactory = FilterFactoryFinder
			.createFilterFactory();

	/** DOCUMENT ME! */
	private static final StyleFactory sFac = StyleFactoryFinder
			.createStyleFactory();

	/** DOCUMENT ME! */
	private static final Color BG_COLOR = Color.white;

	/** DOCUMENT ME! */
	private DefaultRasterMapProducer rasterMapProducer;

	/**
	 * 
	 */
	public DefaultRasterMapProducerTest() {
		super();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void setUp() throws Exception {
		this.rasterMapProducer = getProducerInstance();
		super.setUp();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected DefaultRasterMapProducer getProducerInstance() {
		return new DummyRasterMapProducer();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void tearDown() throws Exception {
		this.rasterMapProducer = null;
		super.tearDown();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void testSimpleGetMapQuery() throws Exception {
		final String mapFormat = "image/gif";

		final DataStore ds = getCiteDataStore();
		final FeatureSource basicPolygons = ds
				.getFeatureSource(BASIC_POLYGONS_TYPE);
		final Envelope env = basicPolygons.getBounds();

		LOGGER.info("about to create map ctx for BasicPolygons with bounds "
				+ env);

		final WMSMapContext map = new WMSMapContext();
		map.setAreaOfInterest(env);
		map.setMapWidth(300);
		map.setMapHeight(300);
		map.setBgColor(Color.red);
		map.setTransparent(false);
		map.setRequest(new GetMapRequest(null));

		Style basicStyle = getStyle("default.sld");
		map.addLayer(basicPolygons, basicStyle);

		this.rasterMapProducer.setOutputFormat(mapFormat);
		this.rasterMapProducer.setMapContext(map);
		this.rasterMapProducer.produceMap();

		assertNotBlank("testSimpleGetMapQuery", this.rasterMapProducer);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void testDefaultStyle() throws Exception {
		final DataStore ds = getCiteDataStore();
		final String[] typeNames = ds.getTypeNames();

		FeatureSource fSource;

		for (int i = 0; i < typeNames.length; i++) {
			fSource = ds.getFeatureSource(typeNames[i]);
			testDefaultStyle(fSource);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 * @throws IllegalFilterException
	 *             DOCUMENT ME!
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	public void testBlueLake() throws IOException, IllegalFilterException,
			Exception {
		final DataStore ds = getCiteDataStore();
		Envelope env = getBlueLakeBounds();
		double shift = env.getWidth() / 6;

		env = new Envelope(env.getMinX() - shift, env.getMaxX() + shift, env
				.getMinY()
				- shift, env.getMaxY() + shift);

		final WMSMapContext map = new WMSMapContext();
		int w = 400;
		int h = (int) Math.round((env.getHeight() * w) / env.getWidth());
		map.setMapWidth(w);
		map.setMapHeight(h);
		map.setBgColor(BG_COLOR);
		map.setTransparent(true);
		map.setRequest(new GetMapRequest(null));

		map.addLayer(ds.getFeatureSource(FORESTS_TYPE),
				getDefaultStyle(FORESTS_TYPE));
		map.addLayer(ds.getFeatureSource(LAKES_TYPE),
				getDefaultStyle(LAKES_TYPE));
		map.addLayer(ds.getFeatureSource(STREAMS_TYPE),
				getDefaultStyle(STREAMS_TYPE));
		map.addLayer(ds.getFeatureSource(NAMED_PLACES_TYPE),
				getDefaultStyle(NAMED_PLACES_TYPE));
		map.addLayer(ds.getFeatureSource(ROAD_SEGMENTS_TYPE),
				getDefaultStyle(ROAD_SEGMENTS_TYPE));
		map.addLayer(ds.getFeatureSource(PONDS_TYPE),
				getDefaultStyle(PONDS_TYPE));
		map.addLayer(ds.getFeatureSource(BUILDINGS_TYPE),
				getDefaultStyle(BUILDINGS_TYPE));

		map.addLayer(ds.getFeatureSource(DIVIDED_ROUTES_TYPE),
				getDefaultStyle(DIVIDED_ROUTES_TYPE));
		map.addLayer(ds.getFeatureSource(BRIDGES_TYPE),
				getDefaultStyle(BRIDGES_TYPE));

		map.addLayer(ds.getFeatureSource(MAP_NEATLINE_TYPE),
				getDefaultStyle(MAP_NEATLINE_TYPE));

		map.setAreaOfInterest(env);

		this.rasterMapProducer.setOutputFormat("image/gif");
		this.rasterMapProducer.setMapContext(map);
		this.rasterMapProducer.produceMap();

		assertNotBlank("testBlueLake", this.rasterMapProducer);
	}

	/**
	 * Checks that the image generated by the map producer is not blank.
	 * 
	 * @param testName
	 * @param producer
	 */
	protected void assertNotBlank(String testName,
			DefaultRasterMapProducer producer) {
		BufferedImage image = (BufferedImage) producer.getImage();
		assertNotBlank(testName, image, BG_COLOR);
		showImage(testName, image);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param fSource
	 *            DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	private void testDefaultStyle(FeatureSource fSource) throws Exception {
		Style style = getStyle("default.sld");

		Envelope env = getBlueLakeBounds();
		env.expandToInclude(fSource.getBounds());

		int w = 400;
		int h = (int) Math.round((env.getHeight() * w) / env.getWidth());

		double shift = env.getWidth() / 6;

		env = new Envelope(env.getMinX() - shift, env.getMaxX() + shift, env
				.getMinY()
				- shift, env.getMaxY() + shift);

		WMSMapContext map = new WMSMapContext();
		map.setRequest(new GetMapRequest(null));
		map.addLayer(fSource, style);
		map.setAreaOfInterest(env);
		map.setMapWidth(w);
		map.setMapHeight(h);
		map.setBgColor(BG_COLOR);
		map.setTransparent(false);

		this.rasterMapProducer.setOutputFormat("image/gif");
		this.rasterMapProducer.setMapContext(map);
		this.rasterMapProducer.produceMap();

		RenderedImage image = this.rasterMapProducer.getImage();

		String typeName = fSource.getSchema().getTypeName();
		assertNotBlank("testDefaultStyle " + typeName, this.rasterMapProducer);
	}

	/**
	 * Returns the bounds of the entire Blue Lake fictional location, given by
	 * the bounds of the MapNeatline feature source.
	 * 
	 * @return
	 * 
	 * @throws IOException
	 *             DOCUMENT ME!
	 */
	protected Envelope getBlueLakeBounds() throws IOException {
		return getCiteDataStore().getFeatureSource(MAP_NEATLINE_TYPE)
				.getBounds();
	}

	/**
	 * This dummy producer adds no functionality to DefaultRasterMapProducer,
	 * just implements a void formatImageOutputStream to have a concrete class
	 * over which test that DefaultRasterMapProducer correctly generates the
	 * BufferedImage.
	 * 
	 * @author Gabriel Roldan, Axios Engineering
	 * @version $Id: DefaultRasterMapProducerTest.java 6797 2007-05-16 10:23:50Z
	 *          aaime $
	 */
	private static class DummyRasterMapProducer extends
			DefaultRasterMapProducer {
		/**
		 * DOCUMENT ME!
		 * 
		 * @param image
		 *            not used.
		 * @param outStream
		 *            not used.
		 * 
		 * @throws WmsException
		 *             never.
		 * @throws IOException
		 *             never.
		 */
		public void formatImageOutputStream(RenderedImage image,
				OutputStream outStream) throws WmsException, IOException {
			/*
			 * Intentionally left blank, since this class is used just to ensure
			 * the abstract raster producer correctly generates a BufferedImage.
			 */
		}

		protected BufferedImage prepareImage(int width, int height) {
			// final int size = width * height;
			// final byte pixels[] = new byte[size];
			// Arrays.fill(pixels, (byte) 255);
			//
			// // Create a data buffer using the byte buffer of pixel data.
			// // The pixel data is not copied; the data buffer uses the byte
			// buffer
			// // array.
			// final DataBuffer dbuf = new DataBufferByte(pixels, width *
			// height,
			// 0);
			//
			// // Prepare a sample model suitable for the default palette
			// final SampleModel sampleModel = DEFAULT_PALETTE
			// .createCompatibleSampleModel(width, height);
			//
			// // Create a raster using the sample model and data buffer
			// final WritableRaster raster =
			// Raster.createWritableRaster(sampleModel,
			// dbuf, null);
			//
			// // Combine the color model and raster into a buffered image
			// return new BufferedImage(DEFAULT_PALETTE, raster, false, null);
			return new BufferedImage(width, height,
					BufferedImage.TYPE_4BYTE_ABGR);
		}

		public String getContentDisposition() {
			// can be null
			return null;
		}
	}
}
