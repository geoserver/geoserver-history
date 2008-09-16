package org.geoserver;

import java.io.InputStream;

import org.geoserver.wfs.WFSTestSupport;
import org.w3c.dom.Document;


public class CSVOutputFormatTest extends WFSTestSupport {

    public void testOutputFormat() throws Exception {
        InputStream doc = get( "wfs?request=GetFeature&typeName=sf:PrimitiveGeoFeature" +
        "&outputFormat=csv");
        //print( doc );

        //make assertions here
    }
}
