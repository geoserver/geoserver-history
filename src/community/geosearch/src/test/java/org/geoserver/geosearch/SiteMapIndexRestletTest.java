package org.geoserver.geosearch;

import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;

import java.io.InputStream;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;

public class SiteMapIndexRestletTest extends GeoServerTestSupport {
    public void testSiteMapExists() throws Exception {
        QName typename = MockData.BASIC_POLYGONS;
        getFeatureTypeInfo(typename).setIndexingEnabled(true);

        Document d = getAsDOM("/rest/layers/cite:BasicPolygons/sitemap.xml");
        assertEquals(d.getDocumentElement().getTagName(), "urlset");
    }
}
