/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.responses.wms.map;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.Expression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.IllegalFilterException;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.LiteRenderer;
import org.geotools.renderer.lite.LiteRenderer2;
import org.geotools.resources.TestData;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbol;
import org.geotools.styling.Symbolizer;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.responses.wms.WMSMapContext;
import org.vfny.geoserver.testdata.AbstractCiteDataTest;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.transform.TransformerException;


/**
 * Relies on the CITE data provided by AbstractCiteDataTest to excersice the
 * JAI map response delegate.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class JAIMapResponseTest extends AbstractCiteDataTest {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(JAIMapResponseTest.class.getPackage()
                                                                                  .getName());

    /** DOCUMENT ME! */
    private static final FilterFactory filterFactory = FilterFactory
        .createFilterFactory();

    /** DOCUMENT ME! */
    private static final boolean INTERACTIVE = true;

    /** DOCUMENT ME! */
    private static final int DEFAULT_STROKE_WIDTH = 3;

    /** DOCUMENT ME! */
    private static final String DEFAULT_STROKE_COLOR = "#000000";

    /** DOCUMENT ME! */
    private static final String DEFAULT_FILL_COLOR = "#0000FF";

    /** DOCUMENT ME! */
    private static final StyleFactory sFac = StyleFactory.createStyleFactory();

    /** DOCUMENT ME! */
    private static final int SHOW_TIMEOUT = 200;

    /** DOCUMENT ME! */
    private JAIMapProducer jaiMap;

    /**
     *
     */
    public JAIMapResponseTest() {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void setUp() throws Exception {
        this.jaiMap = new JAIMapProducer();
        super.setUp();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void tearDown() throws Exception {
        this.jaiMap = null;
        super.tearDown();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testSimpleGetMapQuery() throws Exception {
        final String mapFormat = "image/png";

        final DataStore ds = getCiteDataStore();
        final FeatureSource basicPolygons = ds.getFeatureSource(BASIC_POLYGONS_TYPE);
        final Envelope env = basicPolygons.getBounds();

        LOGGER.info("about to create map ctx for BasicPolygons with bounds "
            + env);

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(env);
        map.setMapWidth(300);
        map.setMapHeight(300);
        map.setBgColor(Color.red);
        map.setTransparent(false);

        Style basicStyle = getStyle("default.sld");
        map.addLayer(basicPolygons, basicStyle);

        this.jaiMap.setOutputFormat(mapFormat);
        this.jaiMap.produceMap(map);

        BufferedImage image = this.jaiMap.getImage();
        showImage("BasicPolygons - ", SHOW_TIMEOUT, image);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fSource DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private void testDefaultStyle(FeatureSource fSource)
        throws Exception {
        System.out.println("****  Rendering "
            + fSource.getSchema().getTypeName() + "   *********");

        FeatureReader r = fSource.getFeatures().reader();

        while (r.hasNext()) {
            System.out.println(r.next().getDefaultGeometry());
        }

        Style style = getStyle("default.sld");

        Envelope env = getBlueLakeBounds();
        int w = 400;
        int h = (int) Math.round((env.getHeight() * w) / env.getWidth());

        double shift = env.getWidth() / 6;

        env = new Envelope(env.getMinX() - shift, env.getMaxX() + shift,
                env.getMinY() - shift, env.getMaxY() + shift);

        WMSMapContext map = new WMSMapContext();
        map.addLayer(fSource, style);
        map.setAreaOfInterest(env);
        map.setMapWidth(w);
        map.setMapHeight(h);
        map.setBgColor(Color.white);
        map.setTransparent(true);

        this.jaiMap.setOutputFormat("image/png");
        this.jaiMap.produceMap(map);

        BufferedImage image = this.jaiMap.getImage();

        showImage(fSource.getSchema().getTypeName(), SHOW_TIMEOUT, image);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws IllegalFilterException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void testBlueLake()
        throws IOException, IllegalFilterException, Exception {
        final DataStore ds = getCiteDataStore();
        Envelope env = getBlueLakeBounds();
        double shift = env.getWidth() / 6;

        env = new Envelope(env.getMinX() - shift, env.getMaxX() + shift,
                env.getMinY() - shift, env.getMaxY() + shift);

        final WMSMapContext map = new WMSMapContext();
        int w = 400;
        int h = (int) Math.round((env.getHeight() * w) / env.getWidth());
        map.setMapWidth(w);
        map.setMapHeight(h);
        map.setBgColor(Color.white);
        map.setTransparent(true);

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

        this.jaiMap.setOutputFormat("image/png");
        this.jaiMap.produceMap(map);

        BufferedImage image = this.jaiMap.getImage();
        showImage("Blue Lake", 500, image);
    }

    /**
     * Returns the bounds of the entire Blue Lake fictional location, given by
     * the bounds of the MapNeatline feature source.
     *
     * @return
     *
     * @throws IOException DOCUMENT ME!
     */
    protected Envelope getBlueLakeBounds() throws IOException {
        return getCiteDataStore().getFeatureSource(MAP_NEATLINE_TYPE).getBounds();
    }

    /**
     * DOCUMENT ME!
     *
     * @param citeTypeName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private Style getDefaultStyle(String citeTypeName)
        throws Exception {
        return getStyle(citeTypeName + ".sld");
    }

    /**
     * DOCUMENT ME!
     *
     * @param styleName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private Style getStyle(String styleName) throws Exception {
        SLDParser parser = new SLDParser(sFac);
        URL styleRes = getClass().getResource("/test-data/styles/" + styleName);
        parser.setInput(styleRes);

        Style s = parser.readXML()[0];

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
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
     * bounds may be null
     *
     * @param testName DOCUMENT ME!
     * @param renderer DOCUMENT ME!
     * @param timeOut DOCUMENT ME!
     * @param bounds DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private void showRender(String testName, Object renderer, long timeOut,
        Envelope bounds) throws Exception {
        int w = 500;
        int h = (int) Math.round((bounds.getHeight() * w) / bounds.getWidth());
        final BufferedImage image = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        render(renderer, g, new Rectangle(w, h), bounds);

        showImage(testName, timeOut, image);

        if (true) {
            LOGGER.warning(
                "dont forget to remove this and check the generated image");

            return;
        }

        // java.net.URL base = TestData.getResource(this, ".");
        java.io.File base = TestData.file(this, ".");
        java.io.File file = new java.io.File(base,
                testName + "_"
                + renderer.getClass().getName().replace('.', '_') + ".png");
        java.io.FileOutputStream out = new java.io.FileOutputStream(file);
        boolean fred = javax.imageio.ImageIO.write(image, "PNG", out);
        out.close();

        if (!fred) {
            System.out.println("Failed to write image to " + file.toString());
        }

        java.io.File fileExemplar = new java.io.File(base.getPath()
                + "/exemplars",
                testName + "_"
                + renderer.getClass().getName().replace('.', '_') + ".png");

        FileInputStream inExemplar = new FileInputStream(fileExemplar);
        FileInputStream inTest = new FileInputStream(file);

        BufferedImage imageTest = ImageIO.read(inTest);
        BufferedImage imageExemplar = ImageIO.read(inExemplar);

        for (int y = 0; y < imageExemplar.getHeight(); y++) {
            for (int x = 0; x < imageExemplar.getWidth(); x++) {
                assertEquals(imageExemplar.getRGB(x, y), imageTest.getRGB(x, y));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param frameName
     * @param timeOut
     * @param image
     */
    private void showImage(String frameName, long timeOut,
        final BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (((System.getProperty("java.awt.headless") == null)
                || !System.getProperty("java.awt.headless").equals("true"))
                && INTERACTIVE) {
            Frame frame = new Frame(frameName);
            frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        e.getWindow().dispose();
                    }
                });

            Panel p = new Panel() {
                    public void paint(Graphics g) {
                        g.drawImage(image, 0, 0, this);
                    }
                };

            frame.add(p);
            frame.setSize(width, height);
            frame.setVisible(true);

            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            frame.dispose();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param rect DOCUMENT ME!
     * @param bounds DOCUMENT ME!
     */
    private void render(Object obj, Graphics g, Rectangle rect, Envelope bounds) {
        if (obj instanceof LiteRenderer2) {
            LiteRenderer2 renderer = (LiteRenderer2) obj;

            if (bounds == null) {
                renderer.paint((Graphics2D) g, rect, new AffineTransform());
            } else {
                renderer.paint((Graphics2D) g, rect,
                    renderer.worldToScreenTransform(bounds, rect));
            }
        }

        if (obj instanceof LiteRenderer) {
            LiteRenderer renderer = (LiteRenderer) obj;

            if (bounds == null) {
                renderer.paint((Graphics2D) g, rect, new AffineTransform());
            } else {
                renderer.paint((Graphics2D) g, rect,
                    renderer.worldToScreenTransform(bounds, rect));
            }
        }
    }
}
