/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.geoxacml;

import org.geotools.xacml.transport.XACMLLocalTransport;
import org.geotools.xacml.transport.XACMLTransport;

import com.sun.xacml.PDP;

/**
 * Default factory creating transport objects for a local PDP
 * 
 * @author Mueller Christian
 * 
 */
public class DefaultXACMLTransportFactory implements XACMLTransportFactory {

    XACMLTransport transport;

    public DefaultXACMLTransportFactory(PDP pdp, boolean multithreaded) {

        transport = new XACMLLocalTransport(pdp, multithreaded);
    }

    public XACMLTransport getXACMLTransport() {
        return transport;
    }

}
