/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map.svg;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.styling.Style;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: EncodeSVG.java,v 1.3 2004/04/06 12:12:18 cholmesny Exp $
 */
public class EncodeSVG {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map");
    private EncoderConfig config;
    private SVGWriter writer;
    private boolean abortProcess;

    public EncodeSVG(EncoderConfig encoderConfig) {
        config = encoderConfig;
    }

    /**
     * DOCUMENT ME!
     */
    public void abort() {
        abortProcess = true;
    }

    public void encode(final OutputStream out) throws IOException {
        Envelope env = config.getReferenceSpace();
        this.writer = new SVGWriter(out, config);
        writer.setMinCoordDistance(config.getMinCoordDistance());

        abortProcess = false;

        long t = System.currentTimeMillis();

        try {
            writeHeader();

            writeLayers();

            if (config.isWriteHeader()) {
                writer.write(EncoderConfig.SVG_FOOTER);
            }

            this.writer.flush();
            t = System.currentTimeMillis() - t;
            LOGGER.info("SVG generated in " + t + " ms");
        } catch (IOException ioe) {
            if (abortProcess) {
                LOGGER.fine("SVG encoding aborted");

                return;
            } else {
                throw ioe;
            }
        } catch (AbortedException ex) {
            return;
        }
    }

    public String createViewBox() {
        Envelope referenceSpace = config.getReferenceSpace();
        String viewBox = (long) writer.getX(referenceSpace.getMinX()) + " "
            + (long) (writer.getY(referenceSpace.getMinY())
            - referenceSpace.getHeight()) + " "
            + (long) referenceSpace.getWidth() + " "
            + (long) referenceSpace.getHeight();

        return viewBox;
    }

    private void writeHeader() throws IOException {
        if (config.isWriteHeader()) {
            String viewBox = createViewBox();
            String header = EncoderConfig.SVG_HEADER.replaceAll("_viewBox_",
                    viewBox);
            header = header.replaceAll("_width_",
                    String.valueOf(config.getMapWidth()));
            header = header.replaceAll("_height_",
                    String.valueOf(config.getMapHeight()));
            writer.write(header);
        }
    }

    private void writeDefs(FeatureTypeInfo layer) throws IOException {
        GeometryAttributeType gtype = layer.getFeatureType().getDefaultGeometry();
        Class geometryClass = gtype.getType();

        if ((geometryClass == MultiPoint.class)
                || (geometryClass == Point.class)) {
            writePointDefs();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writePointDefs() throws IOException {
        writer.write(
            "<defs>\n\t<circle id='point' cx='0' cy='0' r='0.02%' fill='blue'/>\n</defs>\n");
    }

    private void writeLayers() throws IOException, AbortedException {
        FeatureTypeInfo[] layers = config.getLayers();
        FeatureResults[] results = config.getResults();
        Style[] styles = config.getStyles();

        int nLayers = results.length;
        FeatureTypeInfo layerInfo = null;
        int defMaxDecimals = writer.getMaximunFractionDigits();

        for (int i = 0; i < nLayers; i++) {
            layerInfo = layers[i];
            writer.setMaximunFractionDigits(layerInfo.getNumDecimals());

            FeatureReader featureReader = null;

            try {
                LOGGER.fine("obtaining FeatureReader for "
                    + layerInfo.getName(true));
                featureReader = results[i].reader();
                LOGGER.fine("got FeatureReader, now writing");

                String groupId = null;
                String styleName = null;

                groupId = layerInfo.getName(true);

                if (styles != null) {
                    styleName = styles[i].getName();
                }

                writer.write("<g id=\"" + groupId + "\"");

                if (!styleName.startsWith("#")) {
                    writer.write(" class=\"" + styleName + "\"");
                }

                writer.write(">\n");

                if (config.isWriteHeader()) {
                    writeDefs(layerInfo);
                }

                writer.writeFeatures(featureReader, styleName);
                writer.write("</g>\n");
            } catch (IOException ex) {
                throw ex;
            } catch (AbortedException ae) {
                LOGGER.info("process aborted: " + ae.getMessage());
                throw ae;
            } catch (Throwable t) {
                LOGGER.warning("UNCAUGHT exception: " + t.getMessage());

                IOException ioe = new IOException("UNCAUGHT exception: "
                        + t.getMessage());
                ioe.setStackTrace(t.getStackTrace());
                throw ioe;
            } finally {
                if (featureReader != null) {
                    featureReader.close();
                }
            }
        }
    }
}
