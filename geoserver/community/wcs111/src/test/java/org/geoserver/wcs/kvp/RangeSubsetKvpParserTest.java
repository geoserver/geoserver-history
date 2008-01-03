package org.geoserver.wcs.kvp;

import junit.framework.TestCase;
import net.opengis.wcs.v1_1_1.RangeSubsetType;

import org.geoserver.wcs.kvp.RangeSubsetKvpParser;

public class RangeSubsetKvpParserTest extends TestCase {
    RangeSubsetKvpParser parser = new RangeSubsetKvpParser();
    
    public void testSimpleFields() throws Exception {
        RangeSubsetType rs = (RangeSubsetType) parser.parse("radiance;temperature");
        assertNotNull(rs);
        assertEquals(2, rs.getFieldSubset().size());
    }
    
    public void testInterpolation() throws Exception {
        RangeSubsetType rs = (RangeSubsetType) parser.parse("radiance:linear;temperature:nearest");
        assertNotNull(rs);
        assertEquals(2, rs.getFieldSubset().size());
    }
}
