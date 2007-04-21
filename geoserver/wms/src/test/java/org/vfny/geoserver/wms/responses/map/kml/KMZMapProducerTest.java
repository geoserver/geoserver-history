package org.vfny.geoserver.wms.responses.map.kml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.namespace.QName;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;

public class KMZMapProducerTest extends WMSTestSupport {

    KMZMapProducer mapProducer;
    
    protected void setUp() throws Exception {
        super.setUp();
 
        //create a map context
        WMSMapContext mapContext = new WMSMapContext();
        mapContext.addLayer( createMapLayer( MockData.BASIC_POLYGONS ) );
        mapContext.addLayer( createMapLayer( MockData.BUILDINGS ) );
        mapContext.setMapHeight( 256 );
        mapContext.setMapWidth( 256 );
        
        GetMapRequest getMapRequest = createGetMapRequest(
            new QName[]{ MockData.BASIC_POLYGONS, MockData.BUILDINGS }
        );
        mapContext.setRequest( getMapRequest );
        
        //create hte map producer
        mapProducer = 
            (KMZMapProducer) new KMZMapProducerFactory().createMapProducer( null, getWMS() ); 
    
        mapProducer.produceMap( mapContext );
    }
    
    public void test() throws Exception {
        
        //create the kmz
        File temp = File.createTempFile("test", "kmz");
        temp.delete();
        temp.mkdir();
        temp.deleteOnExit();
        
        File zip = new File( temp, "kmz.zip" );
        zip.deleteOnExit();
        
        FileOutputStream output = new FileOutputStream( zip );
        mapProducer.writeTo( output );
        
        output.flush();
        output.close();
        
        assertTrue( zip.exists() );
        
        //unzip and test it
        ZipFile zipFile = new ZipFile( zip );
        
        assertNotNull( zipFile.getEntry( "wms.kml" ) );
        assertNotNull( zipFile.getEntry( "layer_0.png" ) );
        assertNotNull( zipFile.getEntry( "layer_1.png" ) );
        
        zipFile.close();
    }
}
