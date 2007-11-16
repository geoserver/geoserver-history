package org.geoserver.wfs.v1_1;

import org.geoserver.wfs.WFSTestSupport;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geoserver.wfs.xml.v1_1_0.WFS;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Parser;
import org.vfny.geoserver.global.Data;

public class WFSXmlTest extends WFSTestSupport {

    WFSConfiguration configuration() {
        Data catalog = getCatalog();
        FeatureTypeSchemaBuilder sb = 
            new FeatureTypeSchemaBuilder.GML3(getWFS(), catalog, getResourceLoader());
        return new WFSConfiguration(catalog,sb,new WFS(sb));
    }

    public void testValid() throws Exception {
        Parser parser = new Parser(configuration());
        parser.parse(getClass().getResourceAsStream("GetFeature.xml"));

        assertEquals(0, parser.getValidationErrors().size());
    }

    public void testInvalid() throws Exception {
        Parser parser = new Parser(configuration());
        parser.setValidating(true);
        parser.parse(getClass().getResourceAsStream("GetFeature-invalid.xml"));

        assertTrue(parser.getValidationErrors().size() > 0);
    }

}
