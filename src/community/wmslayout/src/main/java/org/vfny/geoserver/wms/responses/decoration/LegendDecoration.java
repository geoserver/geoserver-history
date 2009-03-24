/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.decorations;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.Decoration;
import org.vfny.geoserver.wms.responses.LegendUtils;
import org.vfny.geoserver.wms.responses.ImageUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.map.MapLayer;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.imageio.ImageIO;

public class LegendDecoration implements Decoration {
    /** A logger for this class. */
    private static final Logger LOGGER = 
        org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.wms.responses");

    private static final int TITLE_INDENT = 4;

    private static SLDStyleFactory styleFactory = new SLDStyleFactory();

    private static final GeometryFactory geomFac = new GeometryFactory();
    /**
     * Just a holder to avoid creating many polygon shapes from inside
     * <code>getSampleShape()</code>
     */
    private LiteShape2 sampleRect;

    /**
     * Just a holder to avoid creating many line shapes from inside
     * <code>getSampleShape()</code>
     */
    private LiteShape2 sampleLine;

    /**
     * Just a holder to avoid creating many point shapes from inside
     * <code>getSampleShape()</code>
     */
    private LiteShape2 samplePoint;

    private static final StyledShapePainter shapePainter = new StyledShapePainter(null);

    public void loadOptions(Map<String, String> options){
    }

    public Dimension findOptimalSize(Graphics2D g2d, WMSMapContext mapContext){
        int x = 0, y = 0;
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont().deriveFont(Font.BOLD));
        try {
            for (MapLayerInfo layer : mapContext.getRequest().getLayers()){
                try {
                    BufferedImage legend = getLegend(layer);
                    x = Math.max(x, (int)legend.getWidth());
                    x = Math.max(x, TITLE_INDENT + metrics.stringWidth(layer.getLabel()));
                    y += legend.getHeight() + metrics.getHeight(); 
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error getting legend for " + layer);
                    continue;
                }
            }
            x += metrics.getDescent();
            return new Dimension(x, y);
        } catch (Exception e) {
            return new Dimension(50,50);
        }
    }

    public void paint(Graphics2D g2d, Rectangle paintArea, WMSMapContext mapContext) 
    throws Exception {
        Dimension d = findOptimalSize(g2d, mapContext);
        Rectangle bgRect = new Rectangle(0, 0, d.width, d.height);
        double scaleDenominator = RendererUtilities.calculateOGCScale(
            mapContext.getAreaOfInterest(),
            mapContext.getRequest().getWidth(),
            new HashMap()
        );


        Color oldColor = g2d.getColor();
        AffineTransform oldTransform = (AffineTransform)g2d.getTransform().clone();
        Font oldFont = g2d.getFont();
        Stroke oldStroke = g2d.getStroke();
        g2d.translate(paintArea.getX(), paintArea.getY());
        final WMS wms = mapContext.getRequest().getWMS();

        AffineTransform tx = new AffineTransform(); 

        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont().deriveFont(Font.BOLD));

        double scaleFactor = (paintArea.getWidth() / d.getWidth());
        scaleFactor = Math.min(scaleFactor, paintArea.getHeight() / d.getHeight());

        if (scaleFactor < 1.0) {
            g2d.scale(scaleFactor, scaleFactor);
        }
        AffineTransform bgTransform = g2d.getTransform();
        g2d.setColor(Color.WHITE);
        g2d.fill(bgRect);
        g2d.setColor(Color.BLACK);

        for (MapLayer layer : mapContext.getLayers()){
            g2d.translate(0, metrics.getHeight());
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD));
            g2d.drawString(layer.getTitle(), TITLE_INDENT, 0 - metrics.getDescent());
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN));
            Dimension dim = new LegendRenderer().drawLegend(
                (SimpleFeatureType)layer.getFeatureSource().getSchema(),
                layer.getStyle(),
                scaleDenominator,
                g2d
            );
            g2d.translate(0, dim.getHeight());
        }

        g2d.setTransform(bgTransform);
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new Rectangle(bgRect.x, bgRect.y, bgRect.width -1, bgRect.height - 1));

        g2d.setStroke(oldStroke);
        g2d.setTransform(oldTransform);
        g2d.setFont(oldFont);
        g2d.setColor(oldColor);
    }

    protected BufferedImage getLegend(MapLayerInfo layer) 
    throws MalformedURLException, IOException {
        // TODO: Do this via direct GetLegendGraphic instantiation
        URL imageURL = new URL("http://localhost:8080/geoserver/wms?request=getLegendGraphic&format=image/png&layer=" + layer.getName());

        return ImageIO.read(imageURL);
    }

    public Dimension drawLegend(
            final SimpleFeatureType layer,
            final Style style,
            final double scaleDenominator,
            Graphics2D g2d) throws WmsException {

		final SimpleFeature sampleFeature = createSampleFeature(layer);
        final FeatureTypeStyle[] ftStyles = style.getFeatureTypeStyles();
        final Rule[] applicableRules = LegendUtils.getApplicableRules(ftStyles, scaleDenominator);
        final NumberRange<Double> scaleRange = NumberRange.create(scaleDenominator, scaleDenominator);
        final int ruleCount = applicableRules.length;

        /**
         * A legend graphic is produced for each applicable rule. They're being
         * held here until the process is done and then painted on a "stack"
         * like legend.
         */
        final int w = 20;
        final int h = 20;
        final Color bgColor = Color.WHITE;

        FontMetrics metrics = g2d.getFontMetrics();
        AffineTransform oldTransform = g2d.getTransform();
        Composite oldComposite = g2d.getComposite();

        float totalHeight = 0, totalWidth = 0;

        for (int i = 0; i < ruleCount; i++) {
            final Symbolizer[] symbolizers = applicableRules[i].getSymbolizers();

            // TODO: revive antialiasing!
            //            g2d.setRenderingHint(
            //                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            //            );

            for (int sIdx = 0; sIdx < symbolizers.length; sIdx++) {
                final Symbolizer symbolizer = symbolizers[sIdx];

                if (symbolizer instanceof RasterSymbolizer) {
                    throw new IllegalStateException(
                            "It is not legal to have a RasterSymbolizer here"
                            );
                } else {
                    Style2D style2d = 
                        styleFactory.createStyle(sampleFeature, symbolizer, scaleRange);
                    LiteShape2 shape = getSampleShape(symbolizer, w, h);

                    if (style2d != null) {
                        shapePainter.paint(g2d, shape, style2d, scaleDenominator);
                    }
                }
            }

            String label = applicableRules[i].getTitle();
            if (label == null) label = applicableRules[i].getName();
            if (label == null) label = "";

            g2d.setColor(Color.BLACK);
            g2d.setComposite(AlphaComposite.SrcOver);
            g2d.drawString(
                    label, 
                    h + metrics.getDescent(), 
                    metrics.getHeight()
                    );

            float heightIncrement = Math.max(h, metrics.getHeight());
            g2d.translate(0, heightIncrement);

            totalHeight = totalHeight + heightIncrement;
            totalWidth = 
                Math.max(totalWidth, w + metrics.getDescent() + metrics.stringWidth(label));

        }

        g2d.setTransform(oldTransform);
        g2d.setComposite(oldComposite);

        return new Dimension((int)totalWidth, (int)totalHeight);
    }

    /**
     * Creates a sample Feature instance in the hope that it can be used in the
     * rendering of the legend graphic.
     *
     * @param schema the schema for which to create a sample Feature instance
     *
     * @throws WmsException
     */
    private static SimpleFeature createSampleFeature(SimpleFeatureType schema)
        throws WmsException {
        SimpleFeature sampleFeature;

        try {
            sampleFeature = SimpleFeatureBuilder.template(schema, null); 
        } catch (IllegalAttributeException e) {
            throw new WmsException(e);
        }

        return sampleFeature;
    }

    /**
     * Returns a <code>java.awt.Shape</code> appropiate to render a legend
     * graphic given the symbolizer type and the legend dimensions.
     *
     * @param symbolizer the Symbolizer for whose type a sample shape will be
     *        created
     * @param legendWidth the requested width, in output units, of the legend
     *        graphic
     * @param legendHeight the requested height, in output units, of the legend
     *        graphic
     *
     * @return an appropiate Line2D, Rectangle2D or LiteShape(Point) for the
     *         symbolizer, wether it is a LineSymbolizer, a PolygonSymbolizer,
     *         or a Point ot Text Symbolizer
     *
     * @throws IllegalArgumentException if an unknown symbolizer impl was
     *         passed in.
     */
    private LiteShape2 getSampleShape(Symbolizer symbolizer, int legendWidth, int legendHeight) {
        LiteShape2 sampleShape;
        final float hpad = (legendWidth * LegendUtils.hpaddingFactor);
        final float vpad = (legendHeight * LegendUtils.vpaddingFactor);

        if (symbolizer instanceof LineSymbolizer) {
            if (this.sampleLine == null) {
                Coordinate[] coords = {
                        new Coordinate(hpad, legendHeight - vpad),
                        new Coordinate(legendWidth - hpad, vpad)
                    };
                LineString geom = geomFac.createLineString(coords);

                try {
                    this.sampleLine = new LiteShape2(geom, null, null, false);
                } catch (Exception e) {
                    this.sampleLine = null;
                }
            }

            sampleShape = this.sampleLine;
        } else if ((symbolizer instanceof PolygonSymbolizer)
                || (symbolizer instanceof RasterSymbolizer)) {
            if (this.sampleRect == null) {
                final float w = legendWidth - (2 * hpad);
                final float h = legendHeight - (2 * vpad);

                Coordinate[] coords = {
                        new Coordinate(hpad, vpad), new Coordinate(hpad, vpad + h),
                        new Coordinate(hpad + w, vpad + h), new Coordinate(hpad + w, vpad),
                        new Coordinate(hpad, vpad)
                    };
                LinearRing shell = geomFac.createLinearRing(coords);
                Polygon geom = geomFac.createPolygon(shell, null);

                try {
                    this.sampleRect = new LiteShape2(geom, null, null, false);
                } catch (Exception e) {
                    this.sampleRect = null;
                }
            }

            sampleShape = this.sampleRect;
        } else if (symbolizer instanceof PointSymbolizer || symbolizer instanceof TextSymbolizer) {
            if (this.samplePoint == null) {
                Coordinate coord = new Coordinate(legendWidth / 2, legendHeight / 2);

                try {
                    this.samplePoint = new LiteShape2(geomFac.createPoint(coord), null, null, false);
                } catch (Exception e) {
                    this.samplePoint = null;
                }
            }

            sampleShape = this.samplePoint;
        } else {
            throw new IllegalArgumentException("Unknown symbolizer: " + symbolizer);
        }

        return sampleShape;
    }


}
