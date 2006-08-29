/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;

import java.io.IOException;
import java.io.OutputStream;

import org.geotools.coverage.grid.GridCoverage2D;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;

/** * DOCUMENT ME! *  * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification) * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification) */
public interface CoverageResponseDelegate {
    boolean canProduce(String outputFormat);

    void prepare(String outputFormat, GridCoverage2D coverage)
        throws IOException;

    String getContentType(GeoServer gs);

	/**
	 * 
	 * @uml.property name="contentEncoding" multiplicity="(0 1)"
	 */
	String getContentEncoding();

	/**
	 * 
	 * @uml.property name="contentDisposition" multiplicity="(0 1)"
	 */
	String getContentDisposition();

    void encode(OutputStream output) throws ServiceException, IOException;
}
