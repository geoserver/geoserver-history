/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


/**
 * Defines a describe feature type request.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: DescribeRequest.java,v 1.4 2003/09/12 21:04:25 cholmesny Exp $
 */
public class DescribeRequest extends Request {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Flags whether or not all feature types were requested */
    protected boolean allRequested = true;

    /** Stores all feature types */
    protected List featureTypes = new ArrayList();
    protected String outputFormat = "XMLSCHEMA";

    /**
     * Empty constructor.
     */
    public DescribeRequest() {
        super();
    }

    /**
     * Return request type.
     *
     * @return DOCUMENT ME!
     */
    public String getRequest() {
        return "DESCRIBEFATURETYPE";
    }

    /**
     * Return boolean for all requested types.
     *
     * @return DOCUMENT ME!
     */
    public boolean allRequested() {
        return this.allRequested;
    }

    /**
     * Set requested feature types.
     *
     * @param featureTypes DOCUMENT ME!
     */
    public void setFeatureTypes(List featureTypes) {
        this.featureTypes = featureTypes;
        this.allRequested = false;
    }

    /**
     * Adds a requested feature types to the list.
     *
     * @param featureTypes DOCUMENT ME!
     */
    public void addFeatureType(String featureTypes) {
        this.featureTypes.add(featureTypes);
        this.allRequested = false;
    }

    /**
     * Return requested feature types.
     *
     * @return DOCUMENT ME!
     */
    public List getFeatureTypes() {
        return this.featureTypes;
    }

    /**
     * Sets the outputFormat.  Right now XMLSCHEMA is the only allowed format.
     *
     * @param outputFormat the new outputFormat
     */
    public void setOutputFormat(String outputFormat) {
        if (!((outputFormat == null) || outputFormat.equals(""))) {
            this.outputFormat = outputFormat;
        }
    }

    /**
     * Returns the format for printing the feature type.
     *
     * @return DOCUMENT ME!
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /* ***********************************************************************
     * OVERRIDES OF toString AND equals METHODS.                             *
     * ***********************************************************************/

    /**
     * Returns a string representation of the describe request.
     *
     * @return A string of this request.
     */
    public String toString() {
        String returnString = "DescribeFeatureType Request [outputFormat: ";
        returnString = returnString + outputFormat + " [feature types: ";
        LOGGER.finest("all req: " + allRequested());

        if (this.allRequested()) {
            return returnString + " ALL ]";
        } else {
            Iterator i = featureTypes.listIterator();

            while (i.hasNext()) {
                returnString = returnString + i.next();

                if (i.hasNext()) {
                    returnString = returnString + ", ";
                }
            }

            return returnString + "]";
        }
    }

    /**
     * Standard over-ride of equals.
     *
     * @param o DOCUMENT ME!
     *
     * @return <tt>true</tt> if the object is equal to this.
     */
    public boolean equals(Object o) {
        super.equals(o);

        if (!(o instanceof DescribeRequest)) {
            return false;
        }

        DescribeRequest request = (DescribeRequest) o;
        boolean isEqual = true;
        Iterator internal = featureTypes.listIterator();
        Iterator compare = request.getFeatureTypes().listIterator();

        if (request.allRequested()) {
            isEqual = this.allRequested() && isEqual;

            return isEqual;
        } else {
            while (internal.hasNext()) {
                if (compare.hasNext()) {
                    isEqual = internal.next().equals(compare.next()) && isEqual;
                } else {
                    internal.next();
                    isEqual = false;
                }
            }

            if (compare.hasNext()) {
                return false;
            } else {
                return isEqual;
            }
        }
    }

    public int hashCode() {
        int result = super.hashCode();
        result = (23 * result)
            + ((featureTypes == null) ? 0 : featureTypes.hashCode());

        return result;
    }
}
