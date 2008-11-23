/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package com.orci.geoserver.wfs.getnearest;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import java.io.IOException;
import java.io.OutputStream;


/**
 * After a succeful execution of a GetNearest or GetNearestWithLock request,
 * FeatureResponse will instantiate an implementation of this interface to
 * take care of encoding the resulting set of features in the requested output
 * format.
 *
 * <p>
 * An implementation of this interface reports wich output formats can produce
 * through its <code>canProduce()</code> method. So, when FeatureResponse
 * finds a capable implementation based on the requested output format, it
 * will pass the results of the query to the <code>prepare(String
 * outputFormat, GetNearestResults results)</code> method, and later will ask
 * for the traslation of that results to the desired outputformat using
 * <code>encode(OutputStream)</code>
 * </p>
 *
 * @author Gabriel Rold?n
 * @version $Id: FeatureResponseDelegate.java,v 1.2 2004/03/12 10:19:44 cholmesny Exp $
 */
public interface GetNearestResponseDelegate {
    boolean canProduce(String outputFormat);

    void prepare(String outputFormat, GetNearestResults results)
        throws IOException;

    String getContentType(GeoServer gs);

    String getContentEncoding();

    void encode(OutputStream output) throws ServiceException, IOException;

    /**
     * Returns the content disposition header, or null if not needed.<br>
     * It is advised that the returned file name, if present, matches the
     * feature type name. See <a
     * href="http://www.ietf.org/rfc/rfc1806.txt">rfc1806</a> for details.
     *
     * @param featureTypeName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String getContentDisposition(String featureTypeName);
}
