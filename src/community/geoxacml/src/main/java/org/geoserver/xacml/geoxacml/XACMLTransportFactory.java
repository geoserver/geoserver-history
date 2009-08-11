/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.xacml.geoxacml;

import org.geotools.xacml.transport.XACMLTransport;

/**
 * Inteface for creating transpoert objects
 * 
 * @author Christian Mueller
 *
 */
public interface XACMLTransportFactory {
    XACMLTransport getXACMLTransport();
}
