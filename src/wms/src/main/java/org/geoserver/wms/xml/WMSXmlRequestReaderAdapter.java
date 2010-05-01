/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.xml;

import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.ows.HttpServletRequestAware;
import org.geoserver.wms.WMS;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;

/**
 * Bridge towards the old the old xml readers that injects the proper service info object in the
 * superclass
 * 
 * @author Andrea Aime
 * 
 */
public class WMSXmlRequestReaderAdapter extends org.geoserver.ows.XmlRequestReader implements
        HttpServletRequestAware {

    private Class delegateClass;

    private WMS service;

    private HttpServletRequest httpRequest;

    public WMSXmlRequestReaderAdapter(String namespace, String local, WMS config, Class delegate) {
        super(namespace, local);
        this.service = config;
        this.delegateClass = delegate;
    }

    public void setHttpRequest(HttpServletRequest request) {
        this.httpRequest = request;
    }

    public Object read(Object request, Reader reader, Map kvp) throws Exception {
        // look for a constructor, may have to walk up teh class hierachy
        Class clazz = WMS.class;
        Constructor constructor = null;

        while (clazz != null && constructor == null) {
            try {
                constructor = delegateClass.getConstructor(new Class[] { clazz });
            } catch (NoSuchMethodException e) {
                Class[] classes = clazz.getInterfaces();
                for (Class c : classes) {
                    try {
                        constructor = delegateClass.getConstructor(new Class[] { c });
                    } catch (NoSuchMethodException e2) {
                        // no harm done
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }

        if (constructor == null) {
            throw new IllegalStateException("No appropriate constructor");
        }

        XmlRequestReader delegate = (XmlRequestReader) constructor
                .newInstance(new Object[] { service });

        return delegate.read(reader, httpRequest);
    }
}
