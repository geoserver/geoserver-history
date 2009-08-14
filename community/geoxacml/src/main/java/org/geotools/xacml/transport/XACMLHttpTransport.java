/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.xacml.transport;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.sun.xacml.Indenter;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;

/**
 * Transport Object for a remote PDP reachable by an http POST request. Since XACML requests are
 * independent of each other, it is possible to start each request of a request list as a single
 * thread. This class itself is threadsafe
 * 
 * @author Christian Mueller
 * 
 */
public class XACMLHttpTransport implements XACMLTransport {
    /**
     * Thread class for evaluating a XACML request
     * 
     * @author Christian Mueller
     * 
     */
    public class HttpThread extends Thread {
        private RequestCtx requestCtx = null;
        private ResponseCtx responseCtx = null;
        private RuntimeException runtimeException =null;

        public RuntimeException getRuntimeException() {
            return runtimeException;
        }

        public ResponseCtx getResponseCtx() {
            return responseCtx;
        }

        HttpThread(RequestCtx requestCtx) {
            this.requestCtx = requestCtx;
        }

        @Override
        public void run() {
            try {
                responseCtx = sendHttpPost(requestCtx);
            } catch (RuntimeException ex) {
                this.runtimeException=ex;
            }
        }

    }

    private URL pdpURL;
    private boolean multiThreaded = false;

    public XACMLHttpTransport(URL pdpURL, boolean multiThreaded) {
        this.multiThreaded = multiThreaded;
        this.pdpURL=pdpURL;
    }

    public ResponseCtx evaluateRequestCtx(RequestCtx request) {
        return sendHttpPost(request);
    }

    public List<ResponseCtx> evaluateRequestCtxList(List<RequestCtx> requests) {
        if (multiThreaded)
            return evaluateRequestCtxListMultiThreaded(requests);
        else
            return evaluateRequestCtxListSerial(requests);
    }

    private List<ResponseCtx> evaluateRequestCtxListSerial(List<RequestCtx> requests) {
        List<ResponseCtx> resultList = new ArrayList<ResponseCtx>();
        for (RequestCtx request : requests) {
            resultList.add(sendHttpPost(request));
        }
        return resultList;
    }

    private List<ResponseCtx> evaluateRequestCtxListMultiThreaded(List<RequestCtx> requests) {
        List<ResponseCtx> resultList = new ArrayList<ResponseCtx>(requests.size());
        List<HttpThread> threadList = new ArrayList<HttpThread>(requests.size());
        for (RequestCtx request : requests) {
            HttpThread t = new HttpThread(request);
            t.start();
            threadList.add(t);
        }
        for (HttpThread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (t.getRuntimeException()==null)
                resultList.add(t.getResponseCtx());
            else 
                throw t.getRuntimeException();
        }
        return resultList;

    }

    private ResponseCtx sendHttpPost(RequestCtx requestCtx) {
        try {
            HttpURLConnection conn = (HttpURLConnection) pdpURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "text/xml, application/xml");            
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            requestCtx.encode(out,new Indenter(0),true);
            out.close();
            InputStream in = conn.getInputStream();
            ResponseCtx result = ResponseCtx.getInstance(in);
            in.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
