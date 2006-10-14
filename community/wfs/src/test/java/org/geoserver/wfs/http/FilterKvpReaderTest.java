package org.geoserver.wfs.http;

import java.net.URLDecoder;
import java.util.List;

import junit.framework.TestCase;

import org.geoserver.wfs.http.kvp.FilterKvpReader;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;

public class FilterKvpReaderTest extends TestCase {

	public void test() throws Exception {
		String filter = "%3Cogc%3AFilter+xmlns%3Aogc%3D%22http%3A%2F%2Fwww.opengis.net" +
			"%2Fogc%22+xmlns%3Acdf%3D%22http%3A%2F%2Fwww.opengis.net%2Fcite%2Fdata%22" +
			"%3E%3Cogc%3APropertyIsEqualTo%3E%3Cogc%3APropertyName%3Ecdf%3Aintegers%3C" +
			"%2Fogc%3APropertyName%3E%3Cogc%3AAdd%3E%3Cogc%3ALiteral%3E4%3C%2Fogc%3A" +
			"Literal%3E%3Cogc%3ALiteral%3E3%3C%2Fogc%3ALiteral%3E%3C%2Fogc%3AAdd%3E%3C" +
			"%2Fogc%3APropertyIsEqualTo%3E%3C%2Fogc%3AFilter%3E";
		filter = URLDecoder.decode( filter, "UTF-8" );
		
		List filters = (List) new FilterKvpReader().parse( filter );
		assertNotNull( filters );
		assertEquals( 1, filters.size() );
		
		Filter f = (Filter) filters.get( 0 );
		assertTrue( f instanceof PropertyIsEqualTo );
	}
	
}
