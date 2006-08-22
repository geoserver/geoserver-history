package org.geoserver.http.util;

import junit.framework.TestCase;

public class ResponseUtilsTest extends TestCase {

	public void testAppendQueryString() throws Exception {
		String url = ResponseUtils.appendQueryString( "base", "query" );
		assertEquals( "base?query", url );
				
		url = ResponseUtils.appendQueryString( "base?", "query" );
		assertEquals( "base?query", url );
		
		url = ResponseUtils.appendQueryString( "base?query1", "query2" );
		assertEquals( "base?query1&query2", url );
		
		url = ResponseUtils.appendQueryString( "base?query1&", "query2" );
		assertEquals( "base?query1&query2", url );
		
	}
	
	public void testAppendPath() throws Exception {
		String url = ResponseUtils.appendPath( "base", "path" );
		assertEquals( "base/path", url );
		
		url = ResponseUtils.appendPath( "base/", "path" );
		assertEquals( "base/path", url );
		
		url = ResponseUtils.appendPath( "base", "/path" );
		assertEquals( "base/path", url );
		
		url = ResponseUtils.appendPath( "base/", "/path" );
		assertEquals( "base/path", url );
	}
}
