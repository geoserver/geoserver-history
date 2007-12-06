/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.requests;

import org.vfny.geoserver.catalog.servlets.CATALOGService;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class AddCoverageRequest extends CATALOGRequest {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.catalog");

    /**
     * Catalog Data.
     */
    protected String data;

    /**
     * Empty constructor.
     */
    public AddCoverageRequest(CATALOGService service) {
        super("AddCoverage", service);
    }

    /**
     * Return request type.
     *
     * @return DOCUMENT ME!
     */
    public String getRequest() {
        return "ADDCOVERAGE";
    }

    /**
     * Sets the Catalog Data.
     *
     * @param data
     *            the input Catalog Data
     */
    public void setData(String data) {
        if (!((data == null) || data.equals(""))) {
            this.data = data;
        }
    }

    /**
     * Returns the Catalog Data provided.
     *
     * @return DOCUMENT ME!
     */
    public String getData() {
        return data;
    }

    /***************************************************************************
     * OVERRIDES OF toString AND equals METHODS. *
     **************************************************************************/

    /**
     * Returns a string representation of the describe request.
     *
     * @return A string of this request.
     */
    public String toString() {
        StringBuffer returnString = new StringBuffer(
                "AddCoverage Request [data: ").append(data);

        return returnString.toString();
    }

    /**
     * Standard over-ride of equals.
     *
     * @param o
     *            DOCUMENT ME!
     *
     * @return <tt>true</tt> if the object is equal to this.
     */
    public boolean equals(Object o) {
        super.equals(o);

        if (!(o instanceof AddCoverageRequest)) {
            return false;
        }

        AddCoverageRequest request = (AddCoverageRequest) o;

        return this.data.equals(request.data);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = (23 * result);

        if (data != null) {
            result *= data.hashCode();
        }

        return result;
    }
}
