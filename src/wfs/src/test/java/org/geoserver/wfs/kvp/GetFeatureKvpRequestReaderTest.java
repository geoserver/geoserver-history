package org.geoserver.wfs.kvp;

import java.util.HashMap;
import java.util.Map;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.WfsFactory;

import org.geoserver.test.ows.KvpRequestReaderTestSupport;
import org.geoserver.wfs.WFSException;
import org.geotools.factory.CommonFactoryFinder;

public class GetFeatureKvpRequestReaderTest extends KvpRequestReaderTestSupport {

    private GetFeatureKvpRequestReader reader;

    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        reader = new GetFeatureKvpRequestReader(GetFeatureType.class,
                getCatalog(), CommonFactoryFinder.getFilterFactory(null));
    }
    
    /**
     * http://jira.codehaus.org/browse/GEOS-1875
     */
    public void testInvalidTypeNameBbox() throws Exception {
        Map raw = new HashMap();
        raw.put("service", "WFS");
        raw.put("version", "1.1.0");
        raw.put("method", "GetFeature");
        raw.put("bbox", "-80.4864795578115,25.6176257083275,-80.3401307394915,25.7002737069969");
        raw.put("typeName", "cite:InvalidTypeName");
        
        Map parsed = parseKvp(raw);
        
        try {
            // before fix for GEOS-1875 this would bomb out with an NPE instead of the proper exception
            reader.read(WfsFactory.eINSTANCE.createGetFeatureType(), parsed, raw);
        } catch(WFSException e) {
            assertEquals("InvalidParameterValue", e.getCode());
            assertEquals("typeName", e.getLocator());
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("cite:InvalidTypeName"));
        }
    }

    /**
     * Same as GEOS-1875, but let's check without bbox and without name prefix
     * @throws Exception
     */
    public void testInvalidTypeName() throws Exception {
        Map raw = new HashMap();
        raw.put("service", "WFS");
        raw.put("version", "1.1.0");
        raw.put("method", "GetFeature");
        raw.put("typeName", "InvalidTypeName");
        
        Map parsed = parseKvp(raw);
        
        try {
            reader.read(WfsFactory.eINSTANCE.createGetFeatureType(), parsed, raw);
        } catch(WFSException e) {
            assertEquals("InvalidParameterValue", e.getCode());
            assertEquals("typeName", e.getLocator());
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("InvalidTypeName"));
        }
    }
}
