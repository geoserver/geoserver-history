/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.styling.Style;
import org.vfny.geoserver.global.FeatureTypeInfo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: SVGEncoder.java,v 1.5 2004/01/15 21:53:06 dmzwiers Exp $
 */
public class SVGEncoder {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map");

    /** the XML and SVG header */
    private static final String SVG_HEADER =
        "<?xml version=\"1.0\" standalone=\"no\"?>\n\t"
        + "<!DOCTYPE svg \n\tPUBLIC \"-//W3C//DTD SVG 20001102//EN\" \n\t\"http://www.w3.org/TR/2000/CR-SVG-20001102/DTD/svg-20001102.dtd\">\n"
        + "<svg \n\tstroke=\"green\" \n\tfill=\"none\" \n\tstroke-width=\"0.001%\" \n\twidth=\"_width_\" \n\theight=\"_height_\" \n\tviewBox=\"_viewBox_\" \n\tpreserveAspectRatio=\"xMidYMid meet\">\n";

    /** the SVG closing element */
    private static final String SVG_FOOTER = "</svg>\n";

    /**
     * the referene space is an Envelope object wich is used to translate Y
     * coordinates to an SVG viewbox space. It is necessary due to the
     * different origin of Y coordinates in SVG space and in Coordinates space
     */
    private Envelope referenceSpace = null;

    /** temporary holding of the Feature currently being encoded */
    private Feature currentFeature = null;

    /** temporary holding of the geometry currently being encoded */
    private Geometry currentGeometry = null;

    /** temporary holding of the FeatureTypeInfo currently being encoded */
    private FeatureType featureType;

    /** the writer used to wrap the output stream */
    private SVGWriter writer;

    /** SVG canvas width */
    private String width = "100%";

    /** svg canvas height */
    private String height = "100%";

    /**
     * tells whether all the geometries in FeatureResults will be written as a
     * single SVG graphic element. If this field is set to true, overrides the
     * writting of attributes and ids. It is usefull to significantly reduce
     * the size of the resulting SVG content when no other data than the
     * graphics is needed for a single layer
     */
    private boolean collectGeometries = false;
    private boolean abortProcess = false;

    /**
     * a factor for wich the referenceSpace will be divided to to obtain the
     * minimun distance beteen encoded points
     */
    private double minCoordDistance = 0;

    /**
     * global counter of coordinates skipped from writing due to a proximity
     * lower than <code>minCoordDistance</code> to the last written coordinate
     */
    int coordsSkipCount = 0;

    /** counter of coordinates actually written */
    int coordsWriteCount = 0;

    /**
     * defaults to <code>true</code>, and means if the xml header and svg
     * element should be printed. Some applications may need to not get that
     * encoded to directly parse and add the content to a working client. This
     * field is setted thru the SVGHEADER custom request parameter
     */
    private boolean writeHeader = true;

    /**
     * Creates a new SVGEncoder object.
     */
    public SVGEncoder() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAborted() {
        return abortProcess;
    }

    /**
     * DOCUMENT ME!
     */
    public void abort() {
        abortProcess = true;
    }

    /**
     * sets the SVG canvas width
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * sets the SVG canvas height
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * If <code>collect == true</code>, then all the geometries will be grouped
     * in a single SVG element by FeatureTypeInfo requested. The effect is like a
     * union operation upon the geometries of the whole FeatureResults
     * resulting in a single geometry collection.
     * 
     * <p>
     * NOTE: if this field is set, then writting of ids and attributes will be
     * ignored, since a single <code>&lt;path/%gt;</code> svg element will be
     * printed with it's <code>"d"</code> attribute holding all the geometries
     * from a single FeatureResults.
     * </p>
     *
     * @param collect wether to collect geometries into a single svg element
     *        for each FeatureResults
     */
    public void setCollect(boolean collect) {
        this.collectGeometries = collect;
    }

    /**
     * sets the "viewBox" of the generated SVG with a blur factor of
     * <code>5000</code>
     *
     * @param env DOCUMENT ME!
     *
     * @see setReferenceSpace(Envelope, float)
     */
    public void setReferenceSpace(Envelope env) {
        setReferenceSpace(env, 5000);
    }

    /**
     * DOCUMENT ME!
     *
     * @param includeHeader DOCUMENT ME!
     */
    public void setWriteHeader(boolean includeHeader) {
        this.writeHeader = includeHeader;
    }

    /**
     * sets the "viewBox" of the generated SVG and establishes the encoding
     * blur factor to <code>blurFactor</code>
     * 
     * <p>
     * establishing the blur factor means that the greatest dimension between
     * the width and height of the new SVG coordinate space <code>env</code>
     * will be divided by this factor to obtain the minimun distance allowable
     * between two coordinates to actually encode them.
     * </p>
     * 
     * <p>
     * This method updates the <code>minCoordDistance</code> field, wich every
     * coordinate -<b>except the first 3</b> of a <i>path</i> element-, will
     * be compared against the most previously written to decide if such
     * distance is enough to encode such coordinate or it can be just skipped
     * </p>
     * 
     * <p>
     * In a <i>path</i> element, the first 3 coordinates do not get compared
     * against the minimun coordinate separation distance as the easyest way
     * of keeping polygon shapes consistent
     * </p>
     * 
     * <p>
     * NOTE: if you don't want that klind of hacky geometry generalyzation,
     * just pass <code>0</code> (zero) as <code>blurFactor</code>
     * </p>
     *
     * @param env DOCUMENT ME!
     * @param blurFactor DOCUMENT ME!
     */
    public void setReferenceSpace(Envelope env, float blurFactor) {
        this.referenceSpace = env;

        if (blurFactor > 0) {
            double maxDimension = Math.max(env.getWidth(), env.getHeight());
            this.minCoordDistance = maxDimension / blurFactor;
        } else {
            this.minCoordDistance = env.getWidth() + env.getHeight();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param features DOCUMENT ME!
     * @param out DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void encode(final FeatureResults features, final OutputStream out)
        throws IOException {
        FeatureResults[] results = { features };
        encode(null, results, null, out);
    }

    /**
     * DOCUMENT ME!
     *
     * @param layers the outermost SVG<code>g</code> element id wich will group
     *        all the features in the collection
     * @param results the features to be encoded as SVG vectors
     * @param styles DOCUMENT ME!
     * @param out SVG encoder output stream
     *
     * @throws IOException DOCUMENT ME!
     */
    public void encode(final FeatureTypeInfo[] layers,
        final FeatureResults[] results, final Style[] styles,
        final OutputStream out) throws IOException {
        this.writer = new SVGWriter(out);
        abortProcess = false;
        coordsWriteCount = 0;

        long t = System.currentTimeMillis();
        ensureSVGSpace(results);
        writeHeader();
        writeDefs(layers);

        try {
            writeLayers(layers, results, styles);
        } catch (AbortedException ex) {
            return;
        }

        if (writeHeader) {
            writer.write(SVG_FOOTER);
        }

        this.writer.flush();
        t = System.currentTimeMillis() - t;
        LOGGER.info("SVG generated in " + t + " ms, written "
            + this.coordsWriteCount + " coordinates, skipped "
            + this.coordsSkipCount);
    }

    private void writeHeader() throws IOException {
        if (writeHeader) {
            String viewBox = createViewBox();
            String header = SVG_HEADER.replaceAll("_viewBox_", viewBox);
            header = header.replaceAll("_width_", this.width);
            header = header.replaceAll("_height_", this.height);
            writer.write(header);
        }
    }

    private void writeLayers(FeatureTypeInfo[] layers,
        FeatureResults[] results, Style[] styles)
        throws IOException, AbortedException {
        int nLayers = results.length;
        int nConfigs = ((layers != null) && (layers.length >= nLayers))
            ? nLayers : 0;
        FeatureTypeInfo layerConfig = null;
        int defMaxDecimals = writer.getMaximunFractionDigits();

        for (int i = 0; i < nLayers; i++) {
            if (nConfigs == nLayers) {
                layerConfig = layers[i];
                writer.setMaximunFractionDigits(layerConfig.getNumDecimals());
            } else {
                writer.setMaximunFractionDigits(defMaxDecimals);
            }

            FeatureReader featureReader = null;

            try {
                LOGGER.fine("obtaining FeatureReader for "
                    + layerConfig.getName(true));
                featureReader = results[i].reader();
                LOGGER.fine("got FeatureReader, now writing");

                String groupId = null;

                //modified ch = delegate now changes style names to style 
                //objects, so execute takes a style array.
                //if (styles == null) {
                groupId = layerConfig.getName(true);

                //} else {
                //groupId = (String) styles.get(i);
                //}
                writer.write("<g id=\"" + groupId + "\" class=\"" + groupId
                    + "\">\n");
                writeFeatures(featureReader);
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

    private void writeDefs(FeatureTypeInfo[] layers)
        throws IOException {
        if (layers == null) {
            LOGGER.warning(
                "Can't write symbol definitions, no FeatureTypes passed");

            return;
        }

        int nLayers = layers.length;

        for (int i = 0; i < nLayers; i++) {
            Class geometryClass = layers[i].getFeatureType().getDefaultGeometry()
                                           .getType();

            if ((geometryClass == MultiPoint.class)
                    || (geometryClass == Point.class)) {
                writePointDefs();

                break;
            }
        }
    }

    private String createViewBox() {
        String viewBox = (long) getX(referenceSpace.getMinX()) + " "
            + (long) (getY(referenceSpace.getMinY())
            - referenceSpace.getHeight()) + " "
            + (long) referenceSpace.getWidth() + " "
            + (long) referenceSpace.getHeight();

        return viewBox;
    }

    private void ensureSVGSpace(FeatureResults[] results)
        throws IOException {
        if (this.referenceSpace == null) {
            Envelope bounds = new Envelope();
            int nLayers = results.length;

            for (int i = 0; i < nLayers; i++) {
                Envelope layerBounds = results[i].getBounds();

                if (layerBounds == null) {
                    throw new IOException(
                        "Can't obtain the feature result's bounds");
                }

                bounds.expandToInclude(layerBounds);
            }

            LOGGER.fine("no explicit SVG space defined, using bounds " + bounds);
            setReferenceSpace(bounds);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws AbortedException DOCUMENT ME!
     * @throws DataSourceException DOCUMENT ME!
     */
    private void writeFeatures(FeatureReader reader)
        throws IOException, AbortedException {
        Feature ft;

        try {
            this.featureType = reader.getFeatureType();

            int prevSkipCount = coordsSkipCount;
            coordsSkipCount = 0;

            Class gtype = featureType.getDefaultGeometry().getType();
            boolean doCollect = collectGeometries && (gtype != Point.class)
                && (gtype != MultiPoint.class);

            if (doCollect) {
                writer.write("<path ");
                writer.write("d=\"");
            }

            while (reader.hasNext()) {
                if (isAborted()) {
                    throw new AbortedException("writing features");
                }

                ft = reader.next();
                writeGeometry(ft);
            }

            if (doCollect) {
                writer.write("\"/>\n");
            }

            LOGGER.fine("encoded " + featureType.getTypeName() + " skipped "
                + coordsSkipCount + " coordinates");
            coordsSkipCount += prevSkipCount;
        } catch (NoSuchElementException ex) {
            throw new DataSourceException(ex.getMessage(), ex);
        } catch (IllegalAttributeException ex) {
            throw new DataSourceException(ex.getMessage(), ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writePointDefs() throws IOException {
        writer.write(
            "<defs>\n\t<circle id='point' cx='0' cy='0' r='0.01%' fill='blue'/>\n</defs>\n");
    }

    /**
     * writes a closed <i>path</i> element, suitable for a simple polygon shape
     *
     * @param coords DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeClosedPath(Coordinate[] coords) throws IOException {
        writer.write("<path ");
        writer.write("d=\"");
        writePathContent(coords);
        writer.write("Z");

        writer.write("\"/>\n");
    }

    /**
     * Writes the content of the <b>d</b> attribute in a <i>path</i> SVG
     * element
     * 
     * <p>
     * While iterating over the coordinate array passed as parameter, this
     * method performs a kind of very basic path generalization, verifying
     * that the distance between the current coordinate and the last encoded
     * one is greater than the minimun distance expressed by the field
     * <code>minCoordDistance</code> and established by the method {@link
     * #setReferenceSpace(Envelope, float) setReferenceSpace(Envelope,
     * blurFactor)}
     * </p>
     *
     * @param coords
     *
     * @throws IOException
     */
    private void writePathContent(Coordinate[] coords)
        throws IOException {
        writer.write('M');

        Coordinate prev = coords[0];
        Coordinate curr = null;
        writer.write(getX(prev.x));
        writer.write(' ');
        writer.write(getY(prev.y));

        int nCoords = coords.length;
        writer.write('l');

        for (int i = 1; i < nCoords; i++) {
            curr = coords[i];

            //let at least 3 points in case it is a polygon
            if ((i > 3) && (prev.distance(curr) <= minCoordDistance)) {
                ++coordsSkipCount;

                continue;
            }

            ++coordsWriteCount;
            writer.write((getX(curr.x) - getX(prev.x)));
            writer.write(' ');
            writer.write(getY(curr.y) - getY(prev.y));
            writer.write(' ');
            prev = curr;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ft
     *
     * @throws IOException si algo ocurre escribiendo a <code>out</code>
     */
    private void writeGeometry(Feature ft) throws IOException {
        Geometry geom = ft.getDefaultGeometry();

        if ((geom == null) || geom.isEmpty()) {
            return;
        }

        currentFeature = ft;
        currentGeometry = geom;

        if (geom instanceof MultiPolygon) {
            MultiPolygon mp = (MultiPolygon) geom;
            writeMultiPoly(mp);
        } else if (geom instanceof LineString) {
            LineString l = (LineString) geom;
            writePolyLine(l.getCoordinates());
        } else if (geom instanceof MultiLineString) {
            writeMultiLineString((MultiLineString) geom);
        } else if (geom instanceof MultiPoint) {
            writeMultiPoint((MultiPoint) geom);
        } else if (geom instanceof com.vividsolutions.jts.geom.Point) {
            writePoint((com.vividsolutions.jts.geom.Point) geom);
        }
    }

    private void writeAttributes() throws IOException {
        if (currentGeometry != null) {
            Envelope env = currentGeometry.getEnvelopeInternal();
            writer.write("bounds=\"");
            writer.write(getX(env.getMinX()));
            writer.write(" ");
            writer.write(getY(env.getMinY()));
            writer.write(" ");
            writer.write(env.getWidth());
            writer.write(" ");
            writer.write(env.getHeight());
            writer.write("\" ");
        }

        int numAtts = currentFeature.getNumberOfAttributes();
        Object att;

        for (int i = 0; i < numAtts; i++) {
            att = currentFeature.getAttribute(i);

            if (!(att instanceof Geometry)) {
                writer.writeAttribute(featureType.getAttributeType(i).getName(),
                    att);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param mp DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeMultiPoint(MultiPoint mp) throws IOException {
        writer.write("<g ");
        writeId();
        writeAttributes();
        writer.write(">\n");

        int npoints = mp.getNumGeometries();

        for (int i = 0; i < npoints; i++) {
            writer.write("\t");
            writePoint((Point) mp.getGeometryN(i));
        }

        writer.write("</g>");
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writePoint(com.vividsolutions.jts.geom.Point p)
        throws IOException {
        writer.write("<use xlink:href=\"#point\" x=\"");
        writer.write(getX(p.getX()));
        writer.write("\" y=\"");
        writer.write(getY(p.getY()));
        writer.write("\" ");
        writeAttributes();
        writer.write("/>");
        writer.newline();
        ++coordsWriteCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coords DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writePolyLine(Coordinate[] coords) throws IOException {
        writer.write("<path fill=\"none\" ");
        writeAttributes();
        writer.write(" d=\"");
        writePathContent(coords);
        writer.write("\"/>");
        writer.newline();
    }

    /**
     * DOCUMENT ME!
     *
     * @param mls DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeMultiLineString(MultiLineString mls)
        throws IOException {
        writer.write("<path fill=\"none\" ");
        writeAttributes();
        writer.write(" d=\"");

        int n = mls.getNumGeometries();

        for (int i = 0; i < n; i++) {
            writePathContent(mls.getGeometryN(i).getCoordinates());
        }

        writer.write("\"/>");
        writer.newline();
    }

    //

    /**
     * DOCUMENT ME!
     *
     * @param mpoly
     *
     * @throws IOException
     */
    private void writeMultiPoly(com.vividsolutions.jts.geom.MultiPolygon mpoly)
        throws IOException {
        int n = mpoly.getNumGeometries();
        com.vividsolutions.jts.geom.Polygon poly;

        writer.write("<path ");
        writeId();
        writeAttributes();
        writer.write("d=\"");

        for (int i = 0; i < n; i++) {
            poly = (com.vividsolutions.jts.geom.Polygon) mpoly.getGeometryN(i);
            writePolyContent(poly);
        }

        writer.write("\"/>");
        writer.newline();
    }

    //
    private void writeId() throws IOException {
        writer.write("id=\"");
        writer.write(currentFeature.getID());
        writer.write("\" ");
    }

    /**
     * DOCUMENT ME!
     *
     * @param poly
     *
     * @throws IOException
     */
    private void writePoly(com.vividsolutions.jts.geom.Polygon poly)
        throws IOException {
        LineString shell = poly.getExteriorRing();
        int nHoles = poly.getNumInteriorRing();
        writeClosedPath(shell.getCoordinates());

        for (int i = 0; i < nHoles; i++)
            writeClosedPath(poly.getInteriorRingN(i).getCoordinates());
    }

    /**
     * DOCUMENT ME!
     *
     * @param poly DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writePolyContent(com.vividsolutions.jts.geom.Polygon poly)
        throws IOException {
        LineString shell = poly.getExteriorRing();
        int nHoles = poly.getNumInteriorRing();
        writePathContent(shell.getCoordinates());
        writer.write("Z");

        for (int i = 0; i < nHoles; i++) {
            writePathContent(poly.getInteriorRingN(i).getCoordinates());
            writer.write("Z");
        }
    }

    /**
     * if a reference space has been set, returns a translated Y coordinate
     * wich is inverted based on the height of such a reference space,
     * otherwise just returns <code>y</code>
     *
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getY(double y) {
        return (referenceSpace.getMaxY() - y) + referenceSpace.getMinY();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getX(double x) {
        return x;
    }

    private class AbortedException extends Exception {
        public AbortedException(String msg) {
            super(msg);
        }
    }
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
class SVGWriter extends OutputStreamWriter {
    private static DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols(new Locale(
                "en", "US"));

    static {
        decimalSymbols.setDecimalSeparator('.');
    }

    /** DOCUMENT ME! */
    private DecimalFormat formatter;

    /**
     * Creates a new SVGWriter object.
     *
     * @param out DOCUMENT ME!
     */
    public SVGWriter(OutputStream out) {
        super(out);
        formatter = new DecimalFormat();
        formatter.setDecimalFormatSymbols(decimalSymbols);

        //do not group
        formatter.setGroupingSize(0);

        //do not show decimal separator if it is not needed
        formatter.setDecimalSeparatorAlwaysShown(false);
        formatter.setDecimalFormatSymbols(null);

        //set default number of fraction digits
        formatter.setMaximumFractionDigits(5);

        //minimun fraction digits to 0 so they get not rendered if not needed
        formatter.setMinimumFractionDigits(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param numDigits DOCUMENT ME!
     */
    public void setMaximunFractionDigits(int numDigits) {
        formatter.setMaximumFractionDigits(numDigits);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaximunFractionDigits() {
        return formatter.getMaximumFractionDigits();
    }

    /**
     * DOCUMENT ME!
     *
     * @param numDigits DOCUMENT ME!
     */
    public void setMinimunFractionDigits(int numDigits) {
        formatter.setMinimumFractionDigits(numDigits);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMinimunFractionDigits() {
        return formatter.getMinimumFractionDigits();
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void write(double d) throws IOException {
        write(formatter.format(d));
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void write(char c) throws IOException {
        super.write(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void newline() throws IOException {
        super.write('\n');
    }

    /**
     * DOCUMENT ME!
     *
     * @param attName DOCUMENT ME!
     * @param attValue DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeAttribute(String attName, Object attValue)
        throws IOException {
        write(attName);
        write("=\"");

        if (attValue != null) {
            write(java.net.URLEncoder.encode(String.valueOf(attValue)));
        }

        write("\" ");
    }
}


class SVGEncoderHandler {
    protected SVGWriter writer;

    public SVGEncoderHandler(SVGWriter writer) {
        this.writer = writer;
    }
}
