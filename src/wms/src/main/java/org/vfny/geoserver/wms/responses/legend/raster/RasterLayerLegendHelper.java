package org.vfny.geoserver.wms.responses.legend.raster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.geotools.util.NumberRange;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;
import org.vfny.geoserver.wms.responses.DefaultRasterLegendProducer;
import org.vfny.geoserver.wms.responses.ImageUtils;
import org.vfny.geoserver.wms.responses.LegendUtils;

/**
 * @author  Simone Giannecchini, GeoSolutions SAS
 */
@SuppressWarnings("deprecation")
public class RasterLayerLegendHelper {

	/**
	 * The default legend is a simpe image with an R within it which stands for
	 * Raster.
	 */
	private final static BufferedImage defaultLegend;
	static {
		BufferedImage imgShape = null;
		try {
			final File stylesDirectory = GeoserverDataDirectory
					.findCreateConfigDir("styles");
			final File rasterLegend = new File(stylesDirectory,
					"rasterLegend.png");
			if (rasterLegend.exists())
				imgShape = ImageIO.read(rasterLegend);
			else
				imgShape = ImageIO.read(DefaultRasterLegendProducer.class
						.getResource("rasterLegend.png"));
		} catch (Throwable e) {
			imgShape = null;
		}
		// set the default legend
		defaultLegend = imgShape;
	}

	private BufferedImage image;
	private RasterSymbolizer rasterSymbolizer;
	private int width;
	private int height;
	private ColorMapLegendBuilder cmapLegendBuilder;
	private boolean transparent;
	private Color bgColor;

	public RasterLayerLegendHelper(GetLegendGraphicRequest request) {
		// TODO XXX check for null
		parseRequest(request);
	}

	private void parseRequest(final GetLegendGraphicRequest request) {
		// get the requested layer
		// and check that it is actually a grid
		final SimpleFeatureType layer = request.getLayer();
		boolean found =false;
		found = LegendUtils.checkGridLayer(layer);
		if(!found)
			throw new IllegalArgumentException("Unable to create legend for non raster style");
		
		// get the style and its rules
		final Style gt2Style = request.getStyle();
		final FeatureTypeStyle[] ftStyles = gt2Style.getFeatureTypeStyles();
		final double scaleDenominator = request.getScale();

		final Rule[] applicableRules;
		if (request.getRule() != null) {
			applicableRules = new Rule[] { request.getRule() };
		} else {
			applicableRules = LegendUtils.getApplicableRules(ftStyles,
					scaleDenominator);
		}
		if (applicableRules.length != 1)
			throw new IllegalArgumentException(
					"Unable to create a legend for this style, we need exactly 1 rule");

		final NumberRange<Double> scaleRange = NumberRange.create(
				scaleDenominator, scaleDenominator);

		// get width and height
		width = request.getWidth();
		height = request.getHeight();
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("Invalid widht and or height");

		final Symbolizer[] symbolizers = applicableRules[0].getSymbolizers();
		if (symbolizers == null || symbolizers.length != 1
				| symbolizers[0] == null)
			throw new IllegalArgumentException(
					"Unable to create a legend for this style, we need exactly 1 Symbolizer");

		final Symbolizer symbolizer = symbolizers[0];
		if (!(symbolizer instanceof RasterSymbolizer))
			throw new IllegalArgumentException(
					"Unable to create a legend for this style, we need a RasterSymbolizer");
		rasterSymbolizer = (RasterSymbolizer) symbolizer;
		
		// is background transparent?
		transparent = request.isTransparent();

		// background bkgColor
		bgColor = LegendUtils.getBackgroundColor(request);

		// colormap element
		final ColorMap cmap = rasterSymbolizer.getColorMap();
		if (cmap != null && cmap.getColorMapEntries() != null
				&& cmap.getColorMapEntries().length > 0) {
			// colormap legend builder
			cmapLegendBuilder = new ColorMapLegendBuilder();

			// setting type of colormap
			cmapLegendBuilder.setColorMapType(cmap.getType());

			// is this colormap using extended colors
			cmapLegendBuilder.setExtended(cmapLegendBuilder.isExtended());
			

			// setting the requested colormap entries
			cmapLegendBuilder.setRequestedDimension(new Dimension(width,height));

			// setting transparency and background bkgColor
			cmapLegendBuilder.setTransparent(transparent);
			cmapLegendBuilder.setBackgroundColor(bgColor);
			
			//setting band

			// Setting label font and font bkgColor
			cmapLegendBuilder.setLabelFont(LegendUtils.getLabelFont(request));
			cmapLegendBuilder.setLabelFontColor(LegendUtils.getLabelFontColor(request));

			// passing additional options
			cmapLegendBuilder.setAdditionalOptions(request.getLegendOptions());	
			
			//set band
			cmapLegendBuilder.setBand(rasterSymbolizer.getChannelSelection().getGrayChannel());

			// adding the colormap entries
			final ColorMapEntry[] colorMapEntries = cmap.getColorMapEntries();
			for (ColorMapEntry ce : colorMapEntries)
				if (ce != null)
					cmapLegendBuilder.addColorMapEntry(ce);		
		}
	}

	public BufferedImage getLegend() {
		return createResponse();
	}

	@SuppressWarnings("unchecked")
	private BufferedImage createResponse() {

		if (image == null) {
			
			if(cmapLegendBuilder!=null)

				// creating a legend
				image = cmapLegendBuilder.getLegend();

			 else {
				if(defaultLegend==null)
					throw new IllegalStateException("Unable to get default legend");
				
				image = ImageUtils.createImage(width, height,
						(IndexColorModel) null, transparent);
				final Graphics2D graphics = ImageUtils.prepareTransparency(
						transparent, bgColor, image, new HashMap());
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.drawImage(defaultLegend, 0, 0, width, height, null);
				graphics.dispose();
			}
		}

		return image;
	}

}
