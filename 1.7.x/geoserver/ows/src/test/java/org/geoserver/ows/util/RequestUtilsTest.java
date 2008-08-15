package org.geoserver.ows.util;

import org.geotools.util.Version;

import junit.framework.TestCase;

public class RequestUtilsTest extends TestCase {

    public void testVersion() throws Exception {

        assertEquals( new Version( "1.0.0"), RequestUtils.version( "1" ) );
        assertEquals( new Version( "1.0.0"), RequestUtils.version( "1." ) );
        assertEquals( new Version( "1.0.0"), RequestUtils.version( "1.0" ) );
        assertEquals( new Version( "1.0.0"), RequestUtils.version( "1.0." ) );
        assertEquals( new Version( "1.0.0"), RequestUtils.version( "1.0.0" ) );

        try {
            RequestUtils.version( "1.0.0." );
            fail( "x.y.z. should throw exception" );
        }
        catch( Exception e ) {}
        
        try {
            RequestUtils.version( "1.0.0.0" );
            fail( "x.y.z.a should throw exception" );
        }
        catch( Exception e ) {}
    }
}
