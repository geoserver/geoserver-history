/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests;

import java.util.Map;
import java.util.logging.Logger;

import org.geoserver.wcs.WCSInfo;

import com.vividsolutions.jts.geom.Envelope;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public class CoverageRequest extends WCSRequest {
    /** Standard logging instance for class */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.requests");

    /**
     *
     * @uml.property name="coverage" multiplicity="(0 1)"
     */
    protected String coverage = null;

    /**
     *
     * @uml.property name="outputFormat" multiplicity="(0 1)"
     */
    protected String outputFormat = null;
    protected String CRS = null;
    protected String ResponseCRS = null;

    /**
     *
     * @uml.property name="envelope"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    protected Envelope envelope = null;

    /**
     *
     * @uml.property name="interpolation" multiplicity="(0 1)"
     */
    protected String interpolation = null;

    /**
     *
     * @uml.property name="handle" multiplicity="(0 1)"
     */
    protected String handle = null;
    protected String coverageVersion = null;

    /**
     *
     * @uml.property name="gridDimension" multiplicity="(0 1)"
     */
    private int gridDimension;

    /**
     *
     * @uml.property name="gridLow" multiplicity="(0 1)"
     */
    private Double[] gridLow;

    /**
     *
     * @uml.property name="gridHigh" multiplicity="(0 1)"
     */
    private Double[] gridHigh;

    /**
     *
     * @uml.property name="gridOrigin" multiplicity="(0 1)"
     */
    private Double[] gridOrigin;
    private Map parameters;

    public CoverageRequest(WCSInfo wcs) {
        super("GetCoverage", wcs);
    }

    /**
     *
     * @uml.property name="outputFormat"
     */
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
     *
     * @uml.property name="outputFormat"
     */
    public String getOutputFormat() {
        return this.outputFormat;
    }

    /**
     *
     * @uml.property name="handle"
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     *
     * @uml.property name="handle"
     */
    public String getHandle() {
        return this.handle;
    }

    public void setCoverageVersion(String version) {
        this.version = version;
    }

    public String getCoverageVersion() {
        return this.version;
    }

    public String toString() {
        StringBuffer returnString = new StringBuffer("\nRequest");
        returnString.append(": ").append(handle);
        returnString.append("\n coverage:").append(coverage);
        returnString.append("\n output format:").append(outputFormat);
        returnString.append("\n version:").append(version);
        returnString.append("\n envelope:").append(envelope);
        returnString.append("\n interpolation:").append(interpolation);

        return returnString.toString();
    }

    public boolean equals(Object obj) {
        super.equals(obj);

        if (!(obj instanceof CoverageRequest)) {
            return false;
        }

        CoverageRequest request = (CoverageRequest) obj;
        boolean isEqual = true;

        if ((this.coverage == null) && (request.getCoverage() == null)) {
            isEqual = isEqual && true;
        } else if ((this.coverage == null) || (request.getCoverage() == null)) {
            isEqual = false;
        } else if (request.getCoverage().equals(coverage)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        if ((this.version == null) && (request.getVersion() == null)) {
            isEqual = isEqual && true;
        } else if ((this.version == null) || (request.getVersion() == null)) {
            isEqual = false;
        } else if (request.getVersion().equals(version)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        if ((this.handle == null) && (request.getHandle() == null)) {
            isEqual = isEqual && true;
        } else if ((this.handle == null) || (request.getHandle() == null)) {
            isEqual = false;
        } else if (request.getHandle().equals(handle)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        if ((this.outputFormat == null) && (request.getOutputFormat() == null)) {
            isEqual = isEqual && true;
        } else if ((this.outputFormat == null) || (request.getOutputFormat() == null)) {
            isEqual = false;
        } else if (request.getOutputFormat().equals(outputFormat)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        if ((this.envelope == null) && (request.getEnvelope() == null)) {
            isEqual = isEqual && true;
        } else if ((this.envelope == null) || (request.getEnvelope() == null)) {
            isEqual = false;
        } else if (request.getEnvelope().equals(envelope)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        if ((this.interpolation == null) && (request.getInterpolation() == null)) {
            isEqual = isEqual && true;
        } else if ((this.interpolation == null) || (request.getInterpolation() == null)) {
            isEqual = false;
        } else if (request.getInterpolation().equals(interpolation)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        return isEqual;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = (23 * result) + ((handle == null) ? 0 : handle.hashCode());
        result = (23 * result) + ((coverage == null) ? 0 : coverage.hashCode());

        return result;
    }

    /**
     * @return Returns the envelope.
     *
     * @uml.property name="envelope"
     */
    public Envelope getEnvelope() {
        return envelope;
    }

    /**
     * @param envelope
     *            The envelope to set.
     *
     * @uml.property name="envelope"
     */
    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public void setEnvelope(String envelope) {
        String[] coords = envelope.split(",");

        try {
            double arg0 = Double.parseDouble(coords[0]);
            double arg1 = Double.parseDouble(coords[1]);
            double arg2 = Double.parseDouble(coords[2]);
            double arg3 = Double.parseDouble(coords[3]);

            this.envelope = new Envelope(arg0, arg2, arg1, arg3);
        } catch (NumberFormatException e) {
            this.envelope = null;
        }
    }

    /**
     * @return Returns the interpolation.
     *
     * @uml.property name="interpolation"
     */
    public String getInterpolation() {
        return interpolation;
    }

    /**
     * @param interpolation
     *            The interpolation to set.
     *
     * @uml.property name="interpolation"
     */
    public void setInterpolation(String interpolation) {
        this.interpolation = interpolation;
    }

    /**
     * @return Returns the coverage.
     *
     * @uml.property name="coverage"
     */
    public String getCoverage() {
        return coverage;
    }

    /**
     * @param coverage
     *            The coverage to set.
     *
     * @uml.property name="coverage"
     */
    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    /**
     * @return Returns the gridDimension.
     *
     * @uml.property name="gridDimension"
     */
    public int getGridDimension() {
        return gridDimension;
    }

    /**
     * @param gridDimension
     *            The gridDimension to set.
     *
     * @uml.property name="gridDimension"
     */
    public void setGridDimension(int gridDimension) {
        this.gridDimension = gridDimension;
    }

    /**
     * @param value
     *            The gridDimension to set.
     */
    public void setGridDimension(String value) {
        this.gridDimension = Integer.parseInt(value);
    }

    /**
     * @param offsetVector
     */
    public void setOffsetVector(Double[] offsetVector) {
        if (this.envelope != null) {
            final double envWidth = Math.abs(envelope.getMaxX() - envelope.getMinX());
            final double envHeight = Math.abs(envelope.getMaxY() - envelope.getMinY());
            final double width = envWidth / Math.abs(offsetVector[0].doubleValue());
            final double height = envHeight / Math.abs(offsetVector[1].doubleValue());
            setGridOrigin(new Double[] { new Double(0.0), new Double(0.0) });
            setGridLow(new Double[] { new Double(0.0), new Double(0.0) });
            setGridHigh(new Double[] { new Double(width), new Double(height) });
        }
    }

    /**
     * @param origin
     *
     * @uml.property name="gridOrigin"
     */
    public void setGridOrigin(Double[] origin) {
        this.gridOrigin = origin;
    }

    /**
     * @param highers
     *
     * @uml.property name="gridHigh"
     */
    public void setGridHigh(Double[] highers) {
        this.gridHigh = highers;
    }

    /**
     * @param lowers
     *
     * @uml.property name="gridLow"
     */
    public void setGridLow(Double[] lowers) {
        this.gridLow = lowers;
    }

    /**
     * @return Returns the gridHigh.
     *
     * @uml.property name="gridHigh"
     */
    public Double[] getGridHigh() {
        return gridHigh;
    }

    /**
     * @return Returns the gridLow.
     *
     * @uml.property name="gridLow"
     */
    public Double[] getGridLow() {
        return gridLow;
    }

    /**
     * @return Returns the gridOrigin.
     *
     * @uml.property name="gridOrigin"
     */
    public Double[] getGridOrigin() {
        return gridOrigin;
    }

    public String getCRS() {
        return CRS;
    }

    public void setCRS(String crs) {
        CRS = crs;
    }

    public String getResponseCRS() {
        return ResponseCRS;
    }

    public void setResponseCRS(String responseCRS) {
        ResponseCRS = responseCRS;
    }

    /**
     * @param kvpPairs
     */
    public void setParameters(Map kvpPairs) {
        this.parameters = kvpPairs;
    }

    public Map getParameters() {
        return parameters;
    }
}
