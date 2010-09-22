/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.request.DescribeLayerRequest;
import org.geoserver.wms.response.DescribeLayerTransformer;

/**
 * DescribeLayer WMs operation.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public interface DescribeLayer {

    public DescribeLayerTransformer run(DescribeLayerRequest request) throws ServiceException;

}
