/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.test;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;
import junit.framework.TestCase;
import org.geoserver.data.test.MockData;
import org.geoserver.ows.util.ResponseUtils;
import org.geotools.data.FeatureSource;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.xml.namespace.QName;


/**
 * Base test class for GeoServer unit tests.
 * <p>
 * Deriving from this test class provides the test case with preconfigured
 * geoserver and catalog objects.
 * </p>
 * <p>
 * This test case provides a spring application context which loads the
 * application contexts from all modules on the classpath.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class GeoServerTestSupport extends TestCase {
    /**
     * mock GeoServer data directory
     */
    protected MockData dataDirectory;

    /**
     * Application context
     */
    protected GeoServerTestApplicationContext applicationContext;

    /**
     * If subclasses overide they *must* call super.setUp() first.
     */
    protected void setUp() throws Exception {
        super.setUp();

        //set up the data directory
        dataDirectory = new MockData();
        dataDirectory.setUp();

        //copy the service configuration to the data directory
        dataDirectory.copyTo(GeoServerTestSupport.class.getResourceAsStream("services.xml"),
            "services.xml");

        //set up a mock servlet context
        MockServletContext servletContext = new MockServletContext();
        servletContext.setInitParameter("GEOSERVER_DATA_DIR",
            dataDirectory.getDataDirectoryRoot().getAbsolutePath());

        applicationContext = new GeoServerTestApplicationContext(new String[] {
                    "classpath*:/applicationContext.xml",
                    "classpath*:/applicationSecurityContext.xml"
                }, servletContext);

        applicationContext.refresh();
    }

    /**
     * If subclasses overide they *must* call super.tearDown() first.
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        //kill the context
        applicationContext.destroy();

        //kill the data directory
        dataDirectory.tearDown();
        GeoserverDataDirectory.destroy();
    }

    /**
     * Accessor for global catalog instance from the test application context.
     */
    protected Data getCatalog() {
        return (Data) applicationContext.getBean("data");
    }

    /**
     * Accessor for global geoserver instance from the test application context.
     */
    protected GeoServer getGeoServer() {
        return (GeoServer) applicationContext.getBean("geoServer");
    }

    /**
     * Loads a feature source from the catalog.
     *
     * @param typeName The qualified type name of the feature source.
     */
    protected FeatureSource getFeatureSource(QName typeName)
        throws IOException {
        return getCatalog().getFeatureSource(typeName.getPrefix(), typeName.getLocalPart());
    }

    /**
     * Convenience method for subclasses to create mock http servlet requests.
     * <p>
     * Examples of using this method are:
     * <pre>
     * <code>
     *   createRequest( "wfs?request=GetCapabilities" );  //get
     *   createRequest( "wfs" ); //post
     * </code>
     * </pre>
     * </p>
     * @param path The path for the request and optional the query string.
     * @return
     */
    protected MockHttpServletRequest createRequest(String path) {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setScheme("http");
        request.setServerName("localhost");
        request.setContextPath("/geoserver");
        request.setRequestURI(ResponseUtils.stripQueryString(ResponseUtils.appendPath(
                    "/geoserver/", path)));
        request.setQueryString(ResponseUtils.stripQueryString(path));
        request.setRemoteAddr("127.0.0.1");
        request.setServletPath(path);
        kvp(request, path);

        MockHttpSession session = new MockHttpSession();
        session.setupServletContext(new MockServletContext());
        request.setSession(session);

        request.setUserPrincipal(null);

        return request;
    }

    /*
     * Helper method to create the kvp params from the query string.
     */
    private void kvp(MockHttpServletRequest request, String path) {
        int index = path.indexOf('?');

        if (index == -1) {
            return;
        }

        String queryString = path.substring(index + 1);
        StringTokenizer st = new StringTokenizer(queryString, "&");

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String[] keyValuePair = token.split("=");
            request.setupAddParameter(keyValuePair[0], keyValuePair[1]);
        }
    }
}
