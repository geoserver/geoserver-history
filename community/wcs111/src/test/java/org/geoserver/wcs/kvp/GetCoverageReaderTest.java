package org.geoserver.wcs.kvp;

import java.util.HashMap;
import java.util.Map;

import net.opengis.wcs.v1_1_1.GetCoverageType;

import org.geoserver.wcs.test.WCSTestSupport;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wcs.WcsException;

public class GetCoverageReaderTest extends WCSTestSupport {

    GetCoverageRequestReader reader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Data catalog = (Data) applicationContext.getBean("catalog");
        reader = new GetCoverageRequestReader(catalog);
    }

    public void testMissingParams() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        
        try { 
            reader.read(reader.createRequest(), parseKvp(raw), raw);
            fail("Hey, format is missing, this should have failed");
        } catch(WcsException e) {
            assertEquals("MissingParameterValue", e.getCode());
        }

        final String layerId = layerId(WCSTestSupport.TASMANIA_BM);
        raw.put("identifier", layerId);
        try { 
            reader.read(reader.createRequest(), parseKvp(raw), raw);
            fail("Hey, format is missing, this should have failed");
        } catch(WcsException e) {
            assertEquals("MissingParameterValue", e.getCode());
        }
        
        raw.put("format", "GeoTiff");
        try { 
            reader.read(reader.createRequest(), parseKvp(raw), raw);
        } catch(Exception e) {
            fail("This time all mandatory params where provided?");
        }
        
    }
    
    public void testWrongFormatParams() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String layerId = layerId(WCSTestSupport.TASMANIA_BM);
        raw.put("identifier", layerId);
        raw.put("format", "SuperCoolFormat");
        try { 
            reader.read(reader.createRequest(), parseKvp(raw), raw);
            fail("When did we learn to encode SuperCoolFormat?");
        } catch(Exception e) {
            // ok, fine
        }
    }
    
    public void testUnknownCoverageParams() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String layerId = "fairyTales:rumpelstilskin";
        raw.put("identifier", layerId);
        raw.put("format", "SuperCoolFormat");
        try { 
            reader.read(reader.createRequest(), parseKvp(raw), raw);
            fail("That coverage is not registered???");
        } catch(WcsException e) {
            assertEquals(layerId, e.getLocator());
        }
    }
    
    public void testBasic() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String layerId = layerId(WCSTestSupport.TASMANIA_BM);
        raw.put("identifier", layerId);
        raw.put("format", "GeoTiff");

        GetCoverageType getCoverage =  (GetCoverageType) reader.read(reader.createRequest(), parseKvp(raw), raw);
        assertEquals(layerId, getCoverage.getIdentifier().getValue());
        assertEquals("GEOTIFF", getCoverage.getOutput().getFormat());
        assertFalse(getCoverage.getOutput().isStore());
    }
}
