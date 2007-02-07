/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.adapters;

import java.io.Reader;
import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.geoserver.ows.HttpServletRequestAware;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;


public class XmlRequestReaderAdapter extends org.geoserver.ows.XmlRequestReader
    implements HttpServletRequestAware {
    Class delegateClass;
    AbstractService service;
    HttpServletRequest request;

    public XmlRequestReaderAdapter(QName element, AbstractService service) {
        super(element);
        this.service = service;
    }

    public XmlRequestReaderAdapter(String namespace, String local, AbstractService service) {
        this( new QName( namespace, local ), service );
    }
    
    public void setHttpRequest(HttpServletRequest request) {
        this.request = request;
    }

    public Object read(Reader reader) throws Exception {
        //create an instance of the delegate
        Constructor c = delegateClass.getConstructor(new Class[] { AbstractService.class });
        XmlRequestReader delegate = (XmlRequestReader) c.newInstance(new Object[] { service });

        //TODO: simulate the behaviour of the old dispatcher, xml encoding 
        // madness
        return delegate.read(reader, request);
    }
}
