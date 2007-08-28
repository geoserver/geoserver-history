/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.tiff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;

/**
 * Factory for producing Tiff images.
 * 
 * @author Simone Giannecchini
 * @since .1.4.x
 * @version $Id$
 */
public final class TiffMapProducerFactory implements GetMapProducerFactorySpi {
	/** the only MIME type this map producer supports */
	static final String MIME_TYPE = "image/tiff";

	/**
	 * Convenient singleton Set to expose the output format this producer
	 * supports
	 */
	private static final Set SUPPORTED_FORMATS;

	static {
		SUPPORTED_FORMATS = new HashSet(2);
		SUPPORTED_FORMATS.add(MIME_TYPE);
		SUPPORTED_FORMATS.add("image/tiff8");
	}

	/** Logger */
	private static final Logger LOGGER = Logger
			.getLogger("org.vfny.geoserver.wms.responses.map.tiff");

	/**
	 * Default constructor.
	 */
	public TiffMapProducerFactory() {
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getName() {
		return "Tiff backed raster maps producer";
	}

	public Set getSupportedFormats() {
		return SUPPORTED_FORMATS;
	}

	public boolean isAvailable() {
		try {
			Class
					.forName("com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam");

			return true;
		} catch (Exception e) {
		}

		return false;
	}

	public boolean canProduce(String mapFormat) {
		return SUPPORTED_FORMATS.contains(mapFormat);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param mapFormat
	 *            DOCUMENT ME!
	 * @param wms
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws IllegalArgumentException
	 *             DOCUMENT ME!
	 */
	public GetMapProducer createMapProducer(String mapFormat, WMS wms)
			throws IllegalArgumentException {
		if (!canProduce(mapFormat)) {
			throw new IllegalArgumentException("Can't produce " + mapFormat
					+ " format");
		}

		return new TiffMapProducer(mapFormat, MIME_TYPE, wms);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.factory.Factory#getImplementationHints() This just
	 *      returns java.util.Collections.EMPTY_MAP
	 */
	public Map getImplementationHints() {
		return java.util.Collections.EMPTY_MAP;
	}
}
