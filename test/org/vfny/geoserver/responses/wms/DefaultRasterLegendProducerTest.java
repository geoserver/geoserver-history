/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.geotools.feature.FeatureType;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.requests.wms.GetLegendGraphicRequest;
import org.vfny.geoserver.testdata.AbstractCiteDataTest;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class DefaultRasterLegendProducerTest extends AbstractCiteDataTest {
    /** DOCUMENT ME! */
    private DefaultRasterLegendProducer legendProducer;

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

                    public String getContentType()
                        throws java.lang.IllegalStateException {
                        throw new UnsupportedOperationException();
                    }
                };
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
     * Tests the legend graphic production for some simple styles from the cite
     * dataset and their testing styles.
     *
     * @throws Exception
     */
    public void testSimpleStyles() throws Exception {
        //a single rule line based one
        testProduceLegendGraphic(DIVIDED_ROUTES_TYPE, 1);

        //a two rules polygon one
        testProduceLegendGraphic(NAMED_PLACES_TYPE, 2);

        //thrww rules, line based one
        testProduceLegendGraphic(ROAD_SEGMENTS_TYPE, 3);

        //a single rule + graphic fill one
        testProduceLegendGraphic(FORESTS_TYPE, 1);

        //a single rule, default point one
        testProduceLegendGraphic(BRIDGES_TYPE, 1);
    }

    /**
     * Tests that a legend is produced for the explicitly specified rule, when
     * the FeatureTypeStyle has more than one rule, and one of them is
     * requested by the RULE parameter.
     */
    public void testUserSpecifiedRule() {
    }

    /**
     * Tests the legend production for the default style of the given cite type
     * name, as defined in AbstractCiteDataTest.
     * 
     * <p>
     * The number of rules the default style for the given cite type name is expected at
     * the <code>ruleCount</code> value. It is used to assert that the generated legend
     * graphic has as many stacked graphics as rules.
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
    private BufferedImage testProduceLegendGraphic(String citeTypeName,
        int ruleCount) throws Exception {
        FeatureType layer = getCiteDataStore().getSchema(citeTypeName);
        GetLegendGraphicRequest req = new GetLegendGraphicRequest();
        req.setLayer(layer);
        req.setStyle(getDefaultStyle(citeTypeName));

        final int HEIGHT_HINT = 30;
        req.setHeight(HEIGHT_HINT);
        req.setWidth(30);

        this.legendProducer.produceLegendGraphic(req);

        BufferedImage legend = this.legendProducer.getLegendGraphic();
        showImage("legend", 1000, legend);

        String errMsg = citeTypeName + ": number of rules and number of legend graphics don't match";
        assertEquals(errMsg, HEIGHT_HINT * ruleCount, legend.getHeight());

        return legend;
    }
}
