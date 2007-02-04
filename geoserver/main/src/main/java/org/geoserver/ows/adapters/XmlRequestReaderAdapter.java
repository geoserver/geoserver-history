/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.adapters;

import org.geoserver.ows.HttpServletRequestAware;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;


public class XmlRequestReaderAdapter extends org.geoserver.ows.XmlRequestReader
    implements HttpServletRequestAware {
    Class delegateClass;
    AbstractService service;
    HttpServletRequest request;

    public XmlRequestReaderAdapter(QName element, AbstractService service) {
        super(element);
    }

    public void setHttpRequest(HttpServletRequest request) {
        this.request = request;
    }

    public Object read(InputStream input) throws Exception {
        //create an instance of the delegate
        Constructor c = delegateClass.getConstructor(new Class[] { AbstractService.class });
        XmlRequestReader delegate = (XmlRequestReader) c.newInstance(new Object[] { service });

        //TODO: simulate the behaviour of the old dispatcher, xml encoding 
        // madness
        Reader reader = new InputStreamReader(input);

        return delegate.read(reader, request);
    }
}
