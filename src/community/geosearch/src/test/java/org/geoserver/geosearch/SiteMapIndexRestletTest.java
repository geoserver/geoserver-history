package org.geoserver.geosearch;

import org.geoserver.test.GeoServerTestSupport;

import org.w3c.dom.Document;

public class SiteMapIndexRestletTest extends GeoServerTestSupport {
    public void testSiteMapExists() throws Exception {
        Document d = getAsDOM("/rest/sitemap.xml");
        assertEquals(d.getDocumentElement().getTagName(), "sitemapindex");
    }
}
