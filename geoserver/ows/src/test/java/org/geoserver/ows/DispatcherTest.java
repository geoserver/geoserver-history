/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;
import com.mockobjects.servlet.MockServletInputStream;
import com.mockobjects.servlet.MockServletOutputStream;
import junit.framework.TestCase;
import org.geoserver.platform.Service;
import org.geotools.util.Version;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class DispatcherTest extends TestCase {
    public void testReadOpContext() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setupGetContextPath("/geoserver");
        request.setupGetRequestURI("/geoserver/hello");
        request.setupGetMethod("get");

        Dispatcher dispatcher = new Dispatcher();
        Map map = dispatcher.readOpContext(request);

        assertEquals("hello", map.get("service"));
        assertNull(map.get("request"));

        request = new MockHttpServletRequest();
        request.setupGetContextPath("/geoserver");
        request.setupGetRequestURI("/geoserver/hello/Hello");
        request.setupGetMethod("get");
        map = dispatcher.readOpContext(request);

        request = new MockHttpServletRequest();
        request.setupGetContextPath("/geoserver");
        request.setupGetRequestURI("/geoserver/hello/Hello/");

        request.setupGetMethod("get");
        map = dispatcher.readOpContext(request);

        assertEquals("hello", map.get("service"));
        assertEquals("Hello", map.get("request"));
    }

    public void testReadOpPost() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setupGetContextPath("/geoserver");
        request.setupGetRequestURI("/geoserver/hello");
        request.setupGetMethod("post");

        String body = "<Hello service=\"hello\"/>";

        MockServletInputStream input = new MockServletInputStream();
        input.setupRead(body.getBytes());

        request.setupGetInputStream(input);

        Dispatcher dispatcher = new Dispatcher();

        BufferedReader buffered = new BufferedReader(new InputStreamReader(
                    input));
        buffered.mark(2048);

        Map map = dispatcher.readOpPost(buffered);

        assertNotNull(map);
        assertEquals("Hello", map.get("request"));
        assertEquals("hello", map.get("service"));
    }

    public void testParseKVP() throws Exception {
        URL url = getClass().getResource("applicationContext.xml");

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(url
                .toString());

        Dispatcher dispatcher = (Dispatcher) context.getBean("dispatcher");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setupGetContextPath("/geoserver");

        Map params = new HashMap();
        params.put("service", "hello");
        params.put("request", "Hello");
        params.put("message", "Hello world!");

        request.setupGetParameterMap(params);
        request.setupQueryString(
            "service=hello&request=hello&message=Hello World!");

        Dispatcher.Request req = new Dispatcher.Request();
        req.httpRequest = request;

        dispatcher.parseKVP(req);

        Message message = (Message) dispatcher.parseRequestKVP(Message.class,
                req);
        assertEquals(new Message("Hello world!"), message);
    }

    public void testParseXML() throws Exception {
        URL url = getClass().getResource("applicationContext.xml");

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(url
                .toString());

        Dispatcher dispatcher = (Dispatcher) context.getBean("dispatcher");

        String body = "<Hello service=\"hello\" message=\"Hello world!\"/>";
        File file = File.createTempFile("geoserver", "req");
        file.deleteOnExit();

        FileOutputStream output = new FileOutputStream(file);
        output.write(body.getBytes());
        output.flush();
        output.close();

        BufferedReader input = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));

        input.mark(8192);

        Dispatcher.Request req = new Dispatcher.Request();
        req.input = input;

        Object object = dispatcher.parseRequestXML(null, input, req);
        assertEquals(new Message("Hello world!"), object);
    }

    public void testHelloOperationGet() throws Exception {
        URL url = getClass().getResource("applicationContext.xml");

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(url
                .toString());

        Dispatcher dispatcher = (Dispatcher) context.getBean("dispatcher");

        MockHttpServletRequest request = new MockHttpServletRequest() {
                String encoding;

                public int getServerPort() {
                    return 8080;
                }

                public String getCharacterEncoding() {
                    return encoding;
                }

                public void setCharacterEncoding(String encoding) {
                    this.encoding = encoding;
                }
            };

        request.setupScheme("http");
        request.setupServerName("localhost");

        request.setupGetContextPath("/geoserver");
        request.setupGetMethod("GET");

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setupOutputStream(new MockServletOutputStream());

        Map params = new HashMap();
        params.put("service", "hello");
        params.put("request", "Hello");
        params.put("version", "1.0.0");
        params.put("message", "Hello world!");

        request.setupGetParameterMap(params);
        request.setupGetInputStream(null);
        request.setupGetRequestURI(
            "http://localhost/geoserver/ows?service=hello&request=hello&message=HelloWorld");
        request.setupQueryString(
            "service=hello&request=hello&message=HelloWorld");
        dispatcher.handleRequest(request, response);
        assertEquals(params.get("message"), response.getOutputStreamContents());
    }

    public void testHelloOperationPost() throws Exception {
        URL url = getClass().getResource("applicationContext.xml");

        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(url
                .toString());

        Dispatcher dispatcher = (Dispatcher) context.getBean("dispatcher");

        MockHttpServletRequest request = new MockHttpServletRequest() {
                String encoding;

                public int getServerPort() {
                    return 8080;
                }

                public String getCharacterEncoding() {
                    return encoding;
                }

                public void setCharacterEncoding(String encoding) {
                    this.encoding = encoding;
                }
            };

        request.setupScheme("http");
        request.setupServerName("localhost");
        request.setupGetContextPath("/geoserver");
        request.setupGetMethod("POST");
        request.setupGetRequestURI("http://localhost/geoserver/ows");
        request.setupGetContentType("application/xml");

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setupOutputStream(new MockServletOutputStream());

        Map params = new HashMap();
        request.setupGetParameterMap(params);

        String body = "<Hello service=\"hello\" message=\"Hello world!\" version=\"1.0.0\" />";
        MockServletInputStream input = new MockServletInputStream();
        input.setupRead(body.getBytes());

        request.setupGetInputStream(input);

        dispatcher.handleRequest(request, response);
        assertEquals("Hello world!", response.getOutputStreamContents());
    }
}
