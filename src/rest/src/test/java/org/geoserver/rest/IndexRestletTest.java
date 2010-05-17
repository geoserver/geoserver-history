package org.geoserver.rest;

import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;

public class IndexRestletTest extends GeoServerTestSupport {

    public void test() throws Exception {
        getAsDOM( "/rest");
    }
}
