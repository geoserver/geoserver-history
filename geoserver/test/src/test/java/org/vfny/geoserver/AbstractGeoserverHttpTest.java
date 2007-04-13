/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import junit.framework.TestCase;
import java.io.FileInputStream;
import java.util.PropertyResourceBundle;


public abstract class AbstractGeoserverHttpTest extends TestCase {
    /** test fixture properties **/
    PropertyResourceBundle properties;

    protected void setUp() throws Exception {
        properties = new PropertyResourceBundle(new FileInputStream("httptest.properties"));
    }

    public String getProtocol() {
        return (properties.getString("protocol") != null) ? properties.getString("protocol") : "http";
    }

    public String getServer() {
        return (properties.getString("server") != null) ? properties.getString("server") : "localhost";
    }

    public String getPort() {
        return (properties.getString("port") != null) ? properties.getString("port") : "8080";
    }

    public String getContext() {
        return (properties.getString("context") != null) ? properties.getString("context")
                                                         : "geoserver";
    }

    public String getBaseUrl() {
        return getProtocol() + "://" + getServer() + ":" + getPort() + "/" + getContext();
    }

    protected boolean isOffline() {
        try {
            WebConversation conversation = new WebConversation();
            WebRequest request = new GetMethodWebRequest(getBaseUrl()
                    + "/wfs?request=getCapabilities");

            WebResponse response = conversation.getResponse(request);
        } catch (Exception e) {
            return true;
        }

        return false;
    }
}
