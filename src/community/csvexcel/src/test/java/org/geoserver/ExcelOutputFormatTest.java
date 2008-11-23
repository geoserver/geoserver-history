package org.geoserver;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.geoserver.wfs.WFSTestSupport;


public class ExcelOutputFormatTest extends WFSTestSupport {

    public void testOutputFormat() throws Exception {
        InputStream in = get( "wfs?request=GetFeature&typeName=sf:PrimitiveGeoFeature" +
        "&outputFormat=excel");
        
        //write to file for testing purposes
        FileOutputStream file = new FileOutputStream("excelOutputFormatTest.xls");        
        while(in.available() > 0){
        	file.write(in.read());
        }        
        file.flush();
        file.close();

    }
}
