/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.requests.GetMapRequest;

/**
 * This class prints undistorted watermarks on the map by getting information
 * from the layers.
 */
public class WatermarkPainter {
	/** A logger for this class. */
	private static final Logger LOGGER = Logger
			.getLogger("org.vfny.geoserver.wms.responses");

	public static final Color TRANSPARENT = new Color(255, 255, 255, 0);

	private static final int TRANSPARENT_CODE = (255 << 16) | (255 << 8) | 255;

	/**
	 * The GetMapRequest
	 */
	private GetMapRequest request;

	/**
	 * Initializes a new Watermark Painter
	 * 
	 * @param background
	 *            background color, or null if transparent
	 */
	public WatermarkPainter(GetMapRequest request) {
		this.request = request;
	}

	/**
	 * Prevent void initialization.
	 * 
	 */
	private WatermarkPainter() {
	}

	/**
	 * Print the WaterMarks into the graphic2D.
	 * 
	 * @param g2D
	 * @param paintArea
	 * @throws IOException
	 * @throws ClassCastException
	 * @throws MalformedURLException
	 */
	public void paint(Graphics2D g2D, Rectangle paintArea)
			throws MalformedURLException, ClassCastException, IOException {
		// //
		// rendered Map Layers
		// //
		final MapLayerInfo[] mapLayers = this.request.getLayers();

		// /////////////////////////////////////////////////////////////////////
		//
		// Getting logo.
		//
		// I prefere to watermrk myself rather than relying too much on the
		// underlying lib
		//
		// /////////////////////////////////////////////////////////////////////
		LOGGER.info("Loading logo...");
		final BufferedImage logo = ImageIO.read(new URL(this.request
				.getHttpServletRequest().getRequestURL()
				+ "/../data/images/nurc.png"));
		final int logoWidth = logo.getWidth();
		final int logoHeight = logo.getHeight();
		final int marginX = 5;
		final int marginY = 5;

		// /////////////////////////////////////////////////////////////////////
		//
		// Watermark the files
		//
		// /////////////////////////////////////////////////////////////////////
		g2D.drawRenderedImage(logo, AffineTransform
				.getTranslateInstance(paintArea.getWidth() - logoWidth - marginX,
						paintArea.getHeight() - logoHeight - marginY));
	}
}