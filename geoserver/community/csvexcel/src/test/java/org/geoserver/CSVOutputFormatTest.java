package org.geoserver;

import java.io.InputStream;

import org.geoserver.wfs.WFSTestSupport;


public class CSVOutputFormatTest extends WFSTestSupport {

    public void testOutputFormat() throws Exception {
        InputStream in = get( "wfs?request=GetFeature&typeName=sf:PrimitiveGeoFeature" +
        "&outputFormat=csv");
        print( in );

        //make assertions here
    }
}
