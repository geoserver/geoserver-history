/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import java.io.*;

import org.vfny.geoserver.*;
import org.vfny.geoserver.global.*;

/**
 * After a succeful execution of a GetFeature or GetFeatureWithLock request,
 * FeatureResponse will instantiate an implementation of this interface to take
 * care of encoding the resulting set of features in the requested output format.
 * <p>
 * An implementation of this interface reports wich output formats can produce
 * through its <code>canProduce()</code> method. So, when FeatureResponse finds
 * a capable implementation based on the requested output format, it will pass
 * the results of the query to the <code>prepare(String outputFormat,
 * GetFeatureResults results)</code> method, and later will ask for the
 * traslation of that results to the desired outputformat using
 * <code>encode(OutputStream)</code>
 * </p>
 *
 * @author Gabriel Roldán
 * @version $Id: FeatureResponseDelegate.java,v 1.1 2004/03/10 23:39:06 groldan Exp $
 */
public interface FeatureResponseDelegate {

  boolean canProduce(String outputFormat);

  void prepare(String outputFormat, GetFeatureResults results) throws IOException;

  String getContentType(GeoServer gs);

  String getContentEncoding();

  void encode(OutputStream output) throws ServiceException, IOException;
}
