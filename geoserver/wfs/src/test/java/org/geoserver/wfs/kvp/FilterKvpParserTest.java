/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.kvp;

import junit.framework.TestCase;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import java.net.URLDecoder;
import java.util.List;


public class FilterKvpParserTest extends TestCase {
    public void test() throws Exception {
        String filter = "%3Cogc%3AFilter+xmlns%3Aogc%3D%22http%3A%2F%2Fwww.opengis.net"
            + "%2Fogc%22+xmlns%3Acdf%3D%22http%3A%2F%2Fwww.opengis.net%2Fcite%2Fdata%22"
            + "%3E%3Cogc%3APropertyIsEqualTo%3E%3Cogc%3APropertyName%3Ecdf%3Aintegers%3C"
            + "%2Fogc%3APropertyName%3E%3Cogc%3AAdd%3E%3Cogc%3ALiteral%3E4%3C%2Fogc%3A"
            + "Literal%3E%3Cogc%3ALiteral%3E3%3C%2Fogc%3ALiteral%3E%3C%2Fogc%3AAdd%3E%3C"
            + "%2Fogc%3APropertyIsEqualTo%3E%3C%2Fogc%3AFilter%3E";
        filter = URLDecoder.decode(filter, "UTF-8");

        List filters = (List) new FilterKvpParser().parse(filter);
        assertNotNull(filters);
        assertEquals(1, filters.size());

        Filter f = (Filter) filters.get(0);
        assertTrue(f instanceof PropertyIsEqualTo);
    }
}
