/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.geoxacml;

import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.xacml.transport.XACMLHttpTransport;
import org.geotools.xacml.transport.XACMLTransport;

/**
 * Factory creating transport objects for a remote PDP using http POST requests
 * 
 * @author Mueller Christian
 * 
 */
public class HttpXACMLTransportFactory implements XACMLTransportFactory {

    XACMLTransport transport;

    public HttpXACMLTransportFactory(String urlString, boolean multithreaded) {

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        transport = new XACMLHttpTransport(url, multithreaded);
    }

    public XACMLTransport getXACMLTransport() {
        return transport;
    }

}
