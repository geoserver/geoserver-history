/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;


/**
 * Copy of the exception class defined in the SVG producer.
 * @author Simone Giannecchini - GeoSolutions
 * @TODO move up the package hiarachy and use single version from KML and SVG
 */
public class AbortedException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 7163372493076251709L;

    public AbortedException(String msg) {
        super(msg);
    }
}
