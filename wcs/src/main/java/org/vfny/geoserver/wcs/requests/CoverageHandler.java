/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.wcs.WCSInfo;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import com.vividsolutions.jts.geom.Envelope;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class CoverageHandler extends XMLFilterImpl implements ContentHandler {
    /** Class logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.requests.wcs");

    /** Service handling the request */
    private CoverageRequest request = null;
    private String currentTag = new String();
    private Double[] coordinates = new Double[4];
    private Double[] lowers = new Double[2];
    private Double[] highers = new Double[2];
    private Double[] origin = new Double[2];
    private Double[] offsetVector = new Double[2];
    private boolean insideEnvelope = false;
    private boolean insideGrid = false;
    private boolean insideRange = false;
    private int paramNum = -1;
    private ArrayList paramNames = new ArrayList();
    private HashMap params = new HashMap();
    private int minTmp;

    /**
     * Empty constructor.
     */
    public CoverageHandler(WCSInfo wcs) {
        super();
        request = new CoverageRequest(wcs);
    }

    public CoverageRequest getRequest(HttpServletRequest req) {
        request.setHttpServletRequest(req);

        return request;
    }

    /**
     * Notes the start of the element and sets type names and query attributes.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @param atts Element attributes.
     * @throws SAXException When the XML is not well formed.
     */
    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
        throws SAXException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("at start element: " + localName);
        }

        // at start of element, set inside flag to whatever tag we are inside
        currentTag = localName;

        if (currentTag.equals("GetCoverage")) {
            final int length = atts.getLength();
            String curAtt;

            for (int i = 0; i < length; i++) {
                curAtt = atts.getLocalName(i);

                if (curAtt.equals("service")) {
                    request.setService(atts.getValue(i));
                } else if (curAtt.equals("version")) {
                    request.setVersion(atts.getValue(i));
                }
            }
        } else if (currentTag.equals("Envelope")) {
            insideEnvelope = true;

            final int length = atts.getLength();
            String curAtt;

            for (int i = 0; i < length; i++) {
                curAtt = atts.getLocalName(i);

                if (curAtt.equals("srsName")) {
                    request.setCRS(atts.getValue(i));

                    if (request.getResponseCRS() == null) {
                        request.setResponseCRS(atts.getValue(i));
                    }
                }
            }
        } else if (currentTag.equals("Grid") || currentTag.equals("RectifiedGrid")) {
            insideGrid = true;

            final int length = atts.getLength();
            String curAtt;

            for (int i = 0; i < length; i++) {
                curAtt = atts.getLocalName(i);

                if (curAtt.equals("dimension")) {
                    request.setGridDimension(atts.getValue(i));
                }
            }
        } else if (currentTag.equals("rangeSubset")) {
            insideRange = true;
        } else if (currentTag.equals("axisSubset") && insideRange) {
            final int length = atts.getLength();
            String curAtt;

            for (int i = 0; i < length; i++) {
                curAtt = atts.getLocalName(i);

                if (curAtt.equals("name")) {
                    paramNames.add(atts.getValue(i));
                    paramNum++;
                }
            }
        }
    }

    /**
     * Notes the end of the element exists query or bounding box.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @throws SAXException When the XML is not well formed.
     */
    public void endElement(String namespaceURI, String localName, String rawName)
        throws SAXException {
        LOGGER.finer("at end element: " + localName);
        currentTag = localName;

        if (currentTag.equals("Envelope")) {
            insideEnvelope = false;
        } else if (currentTag.equals("Grid") || currentTag.equals("RectifiedGrid")) {
            insideGrid = false;
        } else if (currentTag.equals("rangeSubset")) {
            insideRange = false;
            request.setParameters(params);
        }

        currentTag = "";
    }

    /**
     * Checks if inside parsed element and adds its contents to the appropriate variable.
     *
     * @param ch URI for namespace appended to element.
     * @param start Local name of element.
     * @param length Raw name of element.
     * @throws SAXException When the XML is not well formed.
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        String s = new String(ch, start, length);
        s = s.trim();
        
        // if inside a property element, add the element
        if (currentTag.equals("sourceCoverage")) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Coverage name: ").append(s).toString());
            }

            request.setCoverage(s);
        } else if (currentTag.equals("interpolationMethod")) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Interpolation Method: ").append(s).toString());
            }

            request.setInterpolation(s);
        } else if (currentTag.equals("crs")) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Output CRS: ").append(s).toString());
            }

            request.setCRS(s);

            if (request.getResponseCRS() == null) {
                request.setResponseCRS(s);
            }
        } else if (currentTag.equals("responseCrs")) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Output CRS: ").append(s).toString());
            }

            request.setResponseCRS(s);
        } else if (currentTag.equals("format")) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Output Format: ").append(s).toString());
            }

            request.setOutputFormat(s);
        } else if (currentTag.equals("pos") && insideEnvelope) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Envelope Coordinates: ").append(s).toString());
            }

            if (coordinates[0] == null) {
                String[] coords = s.split(" ");

                try {
                    double arg0 = Double.parseDouble(coords[0]);
                    double arg1 = Double.parseDouble(coords[1]);

                    coordinates[0] = new Double(arg0);
                    coordinates[1] = new Double(arg1);
                } catch (NumberFormatException e) {
                    coordinates[0] = null;
                    coordinates[1] = null;
                }
            } else if (coordinates[2] == null) {
                String[] coords = s.split(" ");

                try {
                    double arg0 = Double.parseDouble(coords[0]);
                    double arg1 = Double.parseDouble(coords[1]);

                    coordinates[2] = new Double(arg0);
                    coordinates[3] = new Double(arg1);

                    Envelope env = new Envelope(coordinates[0].doubleValue(),
                            coordinates[2].doubleValue(), coordinates[1].doubleValue(),
                            coordinates[3].doubleValue());
                    request.setEnvelope(env);
                } catch (NumberFormatException e) {
                    coordinates[2] = null;
                    coordinates[3] = null;
                }
            }
        } else if (currentTag.equals("low") && insideGrid) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Grid Lowers: ").append(s).toString());
            }

            String[] coords = s.split(" ");

            try {
                double arg0 = Double.parseDouble(coords[0]);
                double arg1 = Double.parseDouble(coords[1]);

                lowers[0] = new Double(arg0);
                lowers[1] = new Double(arg1);

                request.setGridLow(lowers);
            } catch (NumberFormatException e) {
                lowers[0] = null;
                lowers[1] = null;
            }
        } else if (currentTag.equals("high") && insideGrid) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Grid Highers: ").append(s).toString());
            }

            String[] coords = s.split(" ");

            try {
                double arg0 = Double.parseDouble(coords[0]);
                double arg1 = Double.parseDouble(coords[1]);

                highers[0] = new Double(arg0);
                highers[1] = new Double(arg1);

                request.setGridHigh(highers);
            } catch (NumberFormatException e) {
                highers[0] = null;
                highers[1] = null;
            }
        } else if (currentTag.equals("pos") && insideGrid) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Grid Origin: ").append(s).toString());
            }

            String[] coords = s.split(" ");

            try {
                double arg0 = Double.parseDouble(coords[0]);
                double arg1 = Double.parseDouble(coords[1]);

                origin[0] = new Double(arg0);
                origin[1] = new Double(arg1);

                request.setGridOrigin(origin);
            } catch (NumberFormatException e) {
                origin[0] = null;
                origin[1] = null;
            }
        } else if (currentTag.equals("offsetVector") && insideGrid) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found Grid Offset-Vector: ").append(s).toString());
            }

            String[] coords = s.split(" ");

            try {
                double arg0 = Double.parseDouble(coords[0]);
                double arg1 = Double.parseDouble(coords[1]);

                if (offsetVector[0] == null) {
                    offsetVector[0] = new Double(arg0);
                } else {
                    offsetVector[1] = new Double(arg1);
                    request.setOffsetVector(offsetVector);
                }
            } catch (NumberFormatException e) {
                offsetVector[0] = null;
                offsetVector[1] = null;
            }
        } else if (currentTag.equals("singleValue") && insideRange
                && (paramNum == (paramNames.size() - 1))) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found axisSubset{" + paramNames.get(paramNum)
                        + "} > singleValue: ").append(s).toString());
            }

            final String key = (String) paramNames.get(paramNum);

            if (params.get(key) == null) {
                params.put(key, s);
            }
        } else if (currentTag.equals("min") && insideRange && (paramNum == (paramNames.size() - 1))) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found axisSubset{" + paramNames.get(paramNum)
                        + "} > min: ").append(s).toString());
            }

            minTmp = (int) Math.round(Double.parseDouble(s));
        } else if (currentTag.equals("max") && insideRange && (paramNum == (paramNames.size() - 1))) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest(new StringBuffer("found axisSubset{" + paramNames.get(paramNum)
                        + "} > max: ").append(s).toString());
            }

            final String key = (String) paramNames.get(paramNum);

            if (params.get(key) == null) {
                int maxTmp = (int) Math.round(Double.parseDouble(s));
                params.put(key, minTmp + "/" + maxTmp);
                minTmp = 0;
            }
        }

        currentTag = "";
    }
}
