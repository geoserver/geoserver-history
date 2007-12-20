/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.geotools.data.FeatureSource;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.styling.Symbolizer;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.testdata.AbstractCiteDataTest;
import org.vfny.geoserver.testdata.MockUtils;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;
import org.vfny.geoserver.wms.servlets.GetLegendGraphic;


/**
 * Tets the functioning of the abstract legend producer for raster formats,
 * which relies on Geotools' StyledShapePainter.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class DefaultRasterLegendProducerTest extends AbstractCiteDataTest {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(DefaultRasterLegendProducerTest.class.getPackage()
                                                                                               .getName());

    /** DOCUMENT ME! */
    private DefaultRasterLegendProducer legendProducer;
    GetLegendGraphic service;

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(DefaultRasterLegendProducerTest.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public void setUp() throws Exception {
        super.setUp();
        this.legendProducer = new DefaultRasterLegendProducer() {
                    public void writeTo(OutputStream out)
                        throws ServiceException, IOException {
                        throw new UnsupportedOperationException();
                    }

                    public String getContentType() throws java.lang.IllegalStateException {
                        throw new UnsupportedOperationException();
                    }
                };

        WMS wms = new WMS(MockUtils.newWmsDto());
        service = new GetLegendGraphic(wms);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void tearDown() throws Exception {
        this.legendProducer = null;
        super.tearDown();
    }

    /**
     * Tests that a legend is produced for the explicitly specified rule, when
     * the FeatureTypeStyle has more than one rule, and one of them is
     * requested by the RULE parameter.
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testUserSpecifiedRule() throws Exception {
        //load a style with 3 rules
        Style multipleRulesStyle = getDefaultStyle(ROAD_SEGMENTS_TYPE);
        Rule rule = multipleRulesStyle.getFeatureTypeStyles()[0].getRules()[0];
        LOGGER.info("testing single rule " + rule.getName() + " from style "
            + multipleRulesStyle.getName());

        GetLegendGraphicRequest req = new GetLegendGraphicRequest(service);
        req.setLayer(new MapLayerInfo(getCiteDataStore().getFeatureSource(ROAD_SEGMENTS_TYPE)));
        req.setStyle(multipleRulesStyle);
        req.setRule(rule);
        req.setLegendOptions(new HashMap());

        final int HEIGHT_HINT = 30;
        req.setHeight(HEIGHT_HINT);

        //use default values for the rest of parameters
        this.legendProducer.produceLegendGraphic(req);

        BufferedImage legend = this.legendProducer.getLegendGraphic();

        //was the legend painted?
        super.assertNotBlank("testUserSpecifiedRule", legend, DefaultRasterLegendProducer.BG_COLOR);

        //was created only one rule?
        String errMsg = "expected just one legend of height " + HEIGHT_HINT + ", for the rule "
            + rule.getName();
        int resultLegendCount = legend.getHeight() / HEIGHT_HINT;
        assertEquals(errMsg, 1, resultLegendCount);
    }

    //    /**
    //     * Tests that scale denominator is respected when passed as part of the
    //     * request parameters
    //     *
    //     * @throws Exception DOCUMENT ME!
    //     */
    //    public void testRespectsScale() throws Exception {
    //        Style style = createSampleStyleWithScale();
    //
    //        GetLegendGraphicRequest req = new GetLegendGraphicRequest();
    //        req.setLayer(getCiteDataStore().getSchema(BUILDINGS_TYPE));
    //        req.setStyle(style);
    //
    //        final int HEIGHT_HINT = 30;
    //        req.setHeight(HEIGHT_HINT);
    //
    //        //use default values for the rest of parameters
    //        this.legendProducer.produceLegendGraphic(req);
    //
    //        BufferedImage legend = this.legendProducer.getLegendGraphic();
    //        assertEquals("Expected two symbols since no scale was set yet",
    //            2 * HEIGHT_HINT, legend.getHeight());
    //
    //        req.setScale(1500);
    //        this.legendProducer.produceLegendGraphic(req);
    //        legend = this.legendProducer.getLegendGraphic();
    //        assertEquals("Expected only one symbol", HEIGHT_HINT, legend.getHeight());
    //    }

    /**
     * Creates a Style with two rules: the first with a polygon symbolizer with
     * all red fill  for scale up to 1:1000, the second with a polygon
     * symbolizer with an all blue fill for scale up to 1:5000.
     *
     * @return
     */
    private Style createSampleStyleWithScale() {
        FilterFactory ff = FilterFactoryFinder.createFilterFactory();
        StyleFactory sf = StyleFactoryFinder.createStyleFactory();
        Style s = sf.createStyle();

        Rule rule1_1000 = sf.createRule();
        Fill redFill = sf.createFill(ff.createLiteralExpression("0xFF0000"));
        Symbolizer redSym = sf.createPolygonSymbolizer(null, redFill, null);
        rule1_1000.setSymbolizers(new Symbolizer[] { redSym });
        rule1_1000.setMaxScaleDenominator(1000);

        Rule rule1_5000 = sf.createRule();
        Fill blueFill = sf.createFill(ff.createLiteralExpression("0x0000FF"));
        Symbolizer blueSym = sf.createPolygonSymbolizer(null, blueFill, null);
        rule1_5000.setSymbolizers(new Symbolizer[] { blueSym });
        rule1_5000.setMinScaleDenominator(1000);
        rule1_5000.setMaxScaleDenominator(5000);

        Rule[] rules = new Rule[] { rule1_1000, rule1_5000 };
        FeatureTypeStyle fts = sf.createFeatureTypeStyle(rules);

        s.setFeatureTypeStyles(new FeatureTypeStyle[] { fts });

        return s;
    }

    /**
     * Tests the legend production for the default style of the given cite type
     * name, as defined in AbstractCiteDataTest.
     *
     * <p>
     * The number of rules the default style for the given cite type name is
     * expected at the <code>ruleCount</code> value. It is used to assert that
     * the generated legend graphic has as many stacked graphics as rules.
     * </p>
     *
     * @param citeTypeName
     * @param ruleCount the pre-known number of rules the default style for the
     *        given cite type has.
     *
     * @return the legend graphic produced by DefaultRasterLegendProducer
     *
     * @throws Exception if something goes wrong getting the cite test data for
     *         <code>citeTypeName</code>, getting its default test style, or
     *         asking the producer to generate the legend
     */
    private BufferedImage testProduceLegendGraphic(String citeTypeName, int ruleCount)
        throws Exception {
        FeatureSource fs = getCiteDataStore().getFeatureSource(citeTypeName);
        GetLegendGraphicRequest req = new GetLegendGraphicRequest(service);
        req.setLayer(new MapLayerInfo(fs));
        req.setStyle(getDefaultStyle(citeTypeName));

        final int HEIGHT_HINT = 30;
        req.setHeight(HEIGHT_HINT);
        req.setWidth(30);

        this.legendProducer.produceLegendGraphic(req);

        BufferedImage legend = this.legendProducer.getLegendGraphic();
        showImage("legend", 1000, legend);

        String errMsg = citeTypeName
            + ": number of rules and number of legend graphics don't match";
        assertEquals(errMsg, HEIGHT_HINT * ruleCount, legend.getHeight());

        return legend;
    }
}
