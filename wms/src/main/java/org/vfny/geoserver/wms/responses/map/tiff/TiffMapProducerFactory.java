/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.tiff;

import java.util.Collections;
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
	private static final Set SUPPORTED_FORMATS = Collections
			.singleton(MIME_TYPE);

	/**Logger */
	private static final Logger LOGGER = Logger
			.getLogger(TiffMapProducerFactory.class.getPackage().getName());

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
		return true;
	}

	public boolean canProduce(String mapFormat) {
		return MIME_TYPE.equals(mapFormat);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param mapFormat
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws IllegalArgumentException
	 *             DOCUMENT ME!
	 */
	public GetMapProducer createMapProducer(String mapFormat, WMS wms)
			throws IllegalArgumentException {
		if (!canProduce(mapFormat))
			throw new IllegalArgumentException("Can't produce " + mapFormat
					+ " format");
		return new TiffMapProducer(mapFormat);
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
