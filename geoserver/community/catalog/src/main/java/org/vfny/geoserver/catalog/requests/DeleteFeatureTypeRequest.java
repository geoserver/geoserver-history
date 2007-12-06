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
public class DeleteFeatureTypeRequest extends CATALOGRequest {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.catalog");

    /**
     * featureTypeId.
     */
    protected String featureTypeId;

    /**
     * Empty constructor.
     */
    public DeleteFeatureTypeRequest(CATALOGService service) {
        super("DeleteFeatureType", service);
    }

    /**
     * Return request type.
     *
     * @return DOCUMENT ME!
     */
    public String getRequest() {
        return "ADDFEATURETYPE";
    }

    /**
     * Sets the featureTypeId.
     *
     * @param featureTypeId
     *            the input featureTypeId
     */
    public void setFeatureTypeId(String featureTypeId) {
        if (!((featureTypeId == null) || featureTypeId.equals(""))) {
            this.featureTypeId = featureTypeId;
        }
    }

    /**
     * Returns the featureTypeId provided.
     *
     * @return DOCUMENT ME!
     */
    public String getFeatureTypeId() {
        return featureTypeId;
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
                "DeleteFeatureType Request [featureTypeId: ").append(featureTypeId);

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

        if (!(o instanceof DeleteFeatureTypeRequest)) {
            return false;
        }

        DeleteFeatureTypeRequest request = (DeleteFeatureTypeRequest) o;

        return this.featureTypeId.equals(request.featureTypeId);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = (23 * result);

        if (featureTypeId != null) {
            result *= featureTypeId.hashCode();
        }

        return result;
    }
}
