package org.vfny.geoserver.wms.responses;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.opengis.filter.expression.Expression;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;

public class LegendUtils {

	/**
	 * Default Legend graphics background color
	 */
	public static final Color BG_COLOR = Color.WHITE;
	/**
	 * Default label color
	 */
	public static final Color FONT_COLOR = Color.BLACK;
	/** padding percentaje factor at both sides of the legend. */
	public static final float hpaddingFactor = 0.15f;
	/** top & bottom padding percentaje factor for the legend */
	public static final float vpaddingFactor = 0.15f;
	/**
	 * shared package's logger
	 */
	private static final Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(LegendUtils.class.getPackage().getName());

	public static Font getLabelFont(GetLegendGraphicRequest req) {

		String legendFontName = "Sans-Serif";
		String legendFontFamily = "plain";
		int legendFontSize = 12;

		Map legendOptions = req.getLegendOptions();
		if (legendOptions.get("fontName") != null) {
			legendFontName = (String) legendOptions.get("fontName");
		}
		if (legendOptions.get("fontStyle") != null) {
			legendFontFamily = (String) legendOptions.get("fontStyle");
		}
		if (legendOptions.get("fontSize") != null) {
			try {
				legendFontSize = Integer.parseInt((String) legendOptions
						.get("fontSize"));
			} catch (Exception e) {
				LOGGER
						.warning("Error trying to interpret legendOption 'fontSize': "
								+ legendOptions.get("fontSize"));
			}
		}

		Font legendFont;
		if (legendFontFamily.equalsIgnoreCase("italic")) {
			legendFont = new Font(legendFontName, Font.ITALIC, legendFontSize);
		} else if (legendFontFamily.equalsIgnoreCase("bold")) {
			legendFont = new Font(legendFontName, Font.BOLD, legendFontSize);
		} else {
			legendFont = new Font(legendFontName, Font.PLAIN, legendFontSize);
		}

		return legendFont;
	}

	public static Color getLabelFontColor(GetLegendGraphicRequest req) {
		Map legendOptions = req.getLegendOptions();
		String color = (String) legendOptions.get("fontColor");
		if (color == null) {
			// return the default
			return FONT_COLOR;
		}

		try {
			return color(color);
		} catch (NumberFormatException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.warning("Could not decode label color: " + color
						+ ", default to " + FONT_COLOR.toString());
			return FONT_COLOR;
		}
	}

	/**
	 * Returns the image background color for the given
	 * {@link GetLegendGraphicRequest}.
	 * 
	 * @param req
	 * @return the Color for the hexadecimal value passed as the
	 *         <code>BGCOLOR</code>
	 *         {@link GetLegendGraphicRequest#getLegendOptions() legend option},
	 *         or the default background color if no bgcolor were passed.
	 */
	public static Color getBackgroundColor(GetLegendGraphicRequest req) {
		Map legendOptions = req.getLegendOptions();
		String color = (String) legendOptions.get("bgColor");
		if (color == null) {
			// return the default
			return BG_COLOR;
		}

		try {
			return color(color);
		} catch (NumberFormatException e) {
			LOGGER.warning("Could not decode background color: " + color
					+ ", default to " + BG_COLOR.toString());
			return BG_COLOR;
		}

	}

	/**
	 * @param entry
	 * @return
	 * @throws IllegalArgumentException
	 * @throws MissingResourceException
	 */
	public static double getOpacity(ColorMapEntry entry)
			throws IllegalArgumentException, MissingResourceException {
		
//	    ColorMapUtilities.ensureNonNull("ColorMapEntry",entry);
		// //
		//
		// As stated in <a
		// href="https://portal.opengeospatial.org/files/?artifact_id=1188">
		// OGC Styled-Layer Descriptor Report (OGC 02-070) version
		// 1.0.0.</a>:
		// "Not all systems can support opacity in colormaps. The default
		// opacity is 1.0 (fully opaque)."
		//
		// //
//		ColorMapUtilities.ensureNonNull("entry",entry);
		Expression opacity = entry.getOpacity();
		Double opacityValue = null;
		if (opacity != null)
			opacityValue = opacity.evaluate(null, Double.class);
		else
			return 1.0;
		if ((opacityValue.doubleValue() - 1) > 0
				|| opacityValue.doubleValue() < 0) {
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.ILLEGAL_ARGUMENT_$2, "Opacity", opacityValue));
		}
		return opacityValue.doubleValue();
	}

	public static Color color(String hex) throws NumberFormatException {
		if (!hex.startsWith("#")) {
			hex = "#" + hex;
		}
		return Color.decode(hex);
	}
	
	/**
	 * @param entry
	 * @return
	 * @throws NumberFormatException
	 */
	public static Color color(ColorMapEntry entry)
			throws NumberFormatException {
//		ColorMapUtilities.ensureNonNull("ColorMapEntry",entry);
		final Expression color = entry.getColor();
//		ColorMapUtilities.ensureNonNull("color",color);
		final String  colorString= color.evaluate(null, String.class);
//		ColorMapUtilities.ensureNonNull("colorString",colorString);
		return color(colorString);
	}
	
	/**
	 * @param entry
	 * @return
	 */
	public static double getQuantity(ColorMapEntry entry) {
//		ColorMapUtilities.ensureNonNull("ColorMapEntry",entry);
		Expression quantity = entry.getQuantity();
//		ColorMapUtilities.ensureNonNull("quantity",quantity);
		Double quantityString = quantity.evaluate(null, Double.class);
//		ColorMapUtilities.ensureNonNull("quantityString",quantityString);
		double q = quantityString.doubleValue();
		return q;
	}
	/**
	 * Finds the applicable Rules for the given scale denominator.
	 *
	 * @param ftStyles
	 * @param scaleDenominator
	 *
	 * @return
	 */
	public static Rule[] getApplicableRules(FeatureTypeStyle[] ftStyles, double scaleDenominator) {
	    /**
	     * Holds both the rules that apply and the ElseRule's if any, in the
	     * order they appear
	     */
	    final List<Rule> ruleList = new ArrayList<Rule>();
	
	    // get applicable rules at the current scale
	    for (int i = 0; i < ftStyles.length; i++) {
	        FeatureTypeStyle fts = ftStyles[i];
	        Rule[] rules = fts.getRules();
	
	        for (int j = 0; j < rules.length; j++) {
	            Rule r = rules[j];
	
	            if (isWithInScale(r, scaleDenominator)) {
	                ruleList.add(r);
	
	                /*
	                 * I'm commented this out since I guess it has no sense
	                 * for producing the legend, since whether or not the rule
	                 * has an else filter, the legend is drawn only if the
	                 * scale denominator lies inside the rule's scale range.
	                          if (r.hasElseFilter()) {
	                              ruleList.add(r);
	                          }
	                 */
	            }
	        }
	    }
	
	    return ruleList.toArray(new Rule[ruleList.size()]);
	}

	/**
	 * Checks if a rule can be triggered at the current scale level
	 *
	 * @param r The rule
	 * @param scaleDenominator the scale denominator to check if it is between
	 *        the rule's scale range. -1 means that it allways is.
	 *
	 * @return true if the scale is compatible with the rule settings
	 */
	public static boolean isWithInScale(Rule r, double scaleDenominator) {
	    return (scaleDenominator == -1)
	    || (((r.getMinScaleDenominator() - DefaultRasterLegendProducer.TOLERANCE) <= scaleDenominator)
	    && ((r.getMaxScaleDenominator() + DefaultRasterLegendProducer.TOLERANCE) > scaleDenominator));
	}

	/**
	 * Return a {@link BufferedImage} representing this label.
	 * The characters '\n' '\r' and '\f' are interpreted as linebreaks,
	 * as is the characater combination "\n" (as opposed to the actual '\n' character).
	 * This allows people to force line breaks in their labels by
	 * including the character "\" followed by "n" in their
	 * label.
	 *
	 * @param label - the label to render
	 * @param g - the Graphics2D that will be used to render this label
	 * @return a {@link BufferedImage} of the properly rendered label.
	 */
	public static BufferedImage renderLabel(String label, Graphics2D g, GetLegendGraphicRequest req) {
	    // We'll accept '/n' as a text string
	    //to indicate a line break, as well as a traditional 'real' line-break in the XML.
	    BufferedImage renderedLabel;
	    Color labelColor = getLabelFontColor(req);
	    if ((label.indexOf("\n") != -1) || (label.indexOf("\\n") != -1)) {
	        //this is a label WITH line-breaks...we need to figure out it's height *and*
	        //width, and then adjust the legend size accordingly
	        Rectangle2D bounds = new Rectangle2D.Double(0, 0, 0, 0);
	        ArrayList<Integer> lineHeight = new ArrayList<Integer>();
	        // four backslashes... "\\" -> '\', so "\\\\n" -> '\' + '\' + 'n'
	        final String realLabel = label.replaceAll("\\\\n", "\n");
	        StringTokenizer st = new StringTokenizer(realLabel, "\n\r\f");
	
	        while (st.hasMoreElements()) {
	            final String token = st.nextToken();
	            Rectangle2D thisLineBounds = g.getFontMetrics().getStringBounds(token, g);
	
	            //if this is directly added as thisLineBounds.getHeight(), then there are rounding errors
	            //because we can only DRAW fonts at discrete integer coords.
	            final int thisLineHeight = (int) Math.ceil(thisLineBounds.getHeight());
	            bounds.add(0, thisLineHeight + bounds.getHeight());
	            bounds.add(thisLineBounds.getWidth(), 0);
	            lineHeight.add((int) Math.ceil(thisLineBounds.getHeight()));
	        }
	
	        //make the actual label image
	        renderedLabel = new BufferedImage((int) Math.ceil(bounds.getWidth()),
	                (int) Math.ceil(bounds.getHeight()), BufferedImage.TYPE_INT_ARGB);
	
	        st = new StringTokenizer(realLabel, "\n\r\f");
	
	        Graphics2D rlg = renderedLabel.createGraphics();
	        rlg.setColor(labelColor);
	        rlg.setFont(g.getFont());
	        rlg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	            g.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING));
	
	        int y = 0 - g.getFontMetrics().getDescent();
	        int c = 0;
	
	        while (st.hasMoreElements()) {
	            y += lineHeight.get(c++).intValue();
	            rlg.drawString(st.nextToken(), 0, y);
	        }
	        rlg.dispose();
	    } else {
	        //this is a traditional 'regular-old' label.  Just figure the
	        //size and act accordingly.
	        int height = (int) Math.ceil(g.getFontMetrics().getStringBounds(label, g).getHeight());
	        int width = (int) Math.ceil(g.getFontMetrics().getStringBounds(label, g).getWidth());
	        renderedLabel = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	
	        Graphics2D rlg = renderedLabel.createGraphics();
	        rlg.setColor(labelColor);
	        rlg.setFont(g.getFont());
	        rlg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	            g.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING));
	        rlg.drawString(label, 0, height - rlg.getFontMetrics().getDescent());
	        rlg.dispose();
	    }
	
	    return renderedLabel;
	}
	/**
	 * @param left
	 * @param hintsMap
	 * @param graphics
	 * @param central
	 * @param right 
	 * @return
	 */
	public static BufferedImage mergeBufferedImages(
			final BufferedImage left,
			final Map<Key, Object> hintsMap, 
			final Graphics2D graphics,
			final BufferedImage central,
			final BufferedImage right, 
			final boolean transparent,
			final Color backgroundColor,
			final boolean vCenter) {
		
		final int totalHeight =  (int) Math.ceil(Math.max(right.getHeight(),Math.max(central.getHeight(), left.getHeight())));
		final int totalWidth = (int) Math.ceil(left.getWidth() + central.getWidth()+right.getWidth());            
        final BufferedImage finalImage = ImageUtils.createImage(totalWidth, totalHeight, (IndexColorModel)null, transparent);
        final Graphics2D finalGraphics = ImageUtils.prepareTransparency(transparent, backgroundColor, finalImage, hintsMap);
        
        //place the left element
        int offsetY=vCenter?(int)(((totalHeight-left.getHeight())/2.0)+0.5):0;;
        finalGraphics.drawImage(left, 0,offsetY,null);

        ///place the central element
        offsetY=vCenter?(int)(((totalHeight-central.getHeight())/2.0)+0.5):totalHeight-central.getHeight();
        finalGraphics.drawImage(central, left.getWidth(),offsetY,null);
        
        ///place the right element
        offsetY=vCenter?(int)(((totalHeight-right.getHeight())/2.0)+0.5):totalHeight-right.getHeight();
        finalGraphics.drawImage(right,left.getWidth()+ central.getWidth(),offsetY,null);        
        
        graphics.dispose();
		return (BufferedImage) finalImage;
	}
}
