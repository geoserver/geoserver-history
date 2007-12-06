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
import org.geoserver.platform.ServiceException;
import org.geotools.util.Version;
import org.w3c.dom.Document;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;


public class DefaultServiceExceptionHandlerTest extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testHandleServiceException() throws Exception {
        HelloWorld helloWorld = new HelloWorld();
        Service service = new Service("hello", helloWorld, new Version("1.0.0"));

        MockHttpServletRequest request = new MockHttpServletRequest() {
                public int getServerPort() {
                    return 8080;
                }
            };

        request.setupScheme("http");
        request.setupServerName("localhost");

        request.setupGetContextPath("geoserver");

        MockServletOutputStream output = new MockServletOutputStream();
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setupOutputStream(output);

        ServiceException exception = new ServiceException(
                "hello service exception");
        exception.setCode("helloCode");
        exception.setLocator("helloLocator");
        exception.getExceptionText().add("helloText");

        DefaultServiceExceptionHandler handler = new DefaultServiceExceptionHandler();
        handler.handleServiceException(exception, service, request, response);

        InputStream input = new ByteArrayInputStream(output.getContents()
                                                           .getBytes());

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
            .newInstance();
        docBuilderFactory.setNamespaceAware(true);

        Document doc = docBuilderFactory.newDocumentBuilder().parse(input);

        assertEquals("ows:ExceptionReport",
            doc.getDocumentElement().getNodeName());
    }
}
