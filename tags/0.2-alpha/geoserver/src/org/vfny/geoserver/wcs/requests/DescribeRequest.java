/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;



/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class DescribeRequest extends WCSRequest {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.wcs");

    /** Flags whether or not all coverages were requested */
    protected boolean allRequested = true;

    /** Stores all coverages */
    protected List coverages = new ArrayList();
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
        return "DESCRIBECOVERAGE";
    }

    /**
     * Return boolean for all requested coverages.
     *
     * @return DOCUMENT ME!
     */
    public boolean allRequested() {
        return this.allRequested;
    }

    /**
     * Set requested coverages.
     *
     * @param coverages DOCUMENT ME!
     */
    public void setCoverages(List coverages) {
        this.coverages = coverages;
        this.allRequested = false;
    }

    /**
     * Adds a requested coverages to the list.
     *
     * @param coverages DOCUMENT ME!
     */
    public void addCoverage(String coverages) {
        this.coverages.add(coverages);
        this.allRequested = false;
    }

    /**
     * Return requested coverages.
     *
     * @return DOCUMENT ME!
     */
    public List getCoverages() {
        return this.coverages;
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
     * Returns the format for printing the coverage.
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
        String returnString = "DescribeCoverage Request [outputFormat: ";
        returnString = returnString + outputFormat + " [coverages: ";
        LOGGER.finest("all req: " + allRequested());

        if (this.allRequested()) {
            return returnString + " ALL ]";
        } else {
            Iterator i = coverages.listIterator();

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
        Iterator internal = coverages.listIterator();
        Iterator compare = request.getCoverages().listIterator();

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
            + ((coverages == null) ? 0 : coverages.hashCode());

        return result;
    }
}
