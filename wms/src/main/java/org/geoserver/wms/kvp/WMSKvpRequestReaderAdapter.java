/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.wms.kvp;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.impl.GeoServerImpl;
import org.geoserver.ows.HttpServletRequestAware;
import org.geoserver.ows.KvpRequestReader;
import org.geoserver.ows.adapters.KvpRequestReaderAdapter;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSInfo;

/**
 * Bridge towards the old the old kvp readers that injects the proper service info object in the
 * superclass
 * 
 * @author Andrea Aime
 * @author Gabriel Roldan
 */
public class WMSKvpRequestReaderAdapter extends KvpRequestReader implements HttpServletRequestAware {

    private org.vfny.geoserver.util.requests.readers.KvpRequestReader delegate;
    Class<? extends org.vfny.geoserver.util.requests.readers.KvpRequestReader> delegateClass;

    HttpServletRequest request;

    private WMS wms;

    public WMSKvpRequestReaderAdapter(final Class requestBean, final Class delegateClass,
            final WMS wms) {
        super(requestBean);
        this.delegateClass = delegateClass;
        this.wms = wms;
    }

    public void setHttpRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Object createRequest() throws Exception {
        // simulate the old kvp processin
        Map kvp = new HashMap();
        String paramName;
        String paramValue;

        for (Enumeration pnames = request.getParameterNames(); pnames.hasMoreElements();) {
            paramName = (String) pnames.nextElement();
            paramValue = request.getParameter(paramName);
            kvp.put(paramName.toUpperCase(), paramValue);
        }

        // look for a constructor, may have to walk up teh class hierachy
        Constructor constructor = findConstructor();

        // create an instance of the delegate
        this.delegate = (org.vfny.geoserver.util.requests.readers.KvpRequestReader) constructor
                .newInstance(new Object[] { kvp, wms });

        // create the request object
        return delegate.getRequest(request);
    }

    @Override
    public Object read(Object request, Map kvp, Map rawKvp) throws Exception {
        Constructor constructor = findConstructor();

        // create an instance of the delegate
        this.delegate = (org.vfny.geoserver.util.requests.readers.KvpRequestReader) constructor
                .newInstance(new Object[] { kvp, wms });

        // create the request object
        return delegate.getRequest(this.request);
    }

    private Constructor findConstructor() {
        // look for a constructor, may have to walk up teh class hierachy
        Class clazz = WMS.class;
        Constructor constructor = null;

        while (clazz != null && constructor == null) {
            try {
                constructor = delegateClass.getConstructor(new Class[] { Map.class, clazz });
            } catch (NoSuchMethodException e) {
                Class[] classes = clazz.getInterfaces();
                for (Class c : classes) {
                    try {
                        constructor = delegateClass.getConstructor(new Class[] { Map.class, c });
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
        return constructor;
    }
}
