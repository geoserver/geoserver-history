package org.geoserver.wps;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.geotools.gml3.GMLConfiguration;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class ExecuteTest extends WPSTestSupport {

    public void testDataInline() throws Exception {
        String xml =  
          "<wps:Execute service='WPS' version='1.0.0' xmlns:wps='http://www.opengis.net/wps/1.0.0' " + 
              "xmlns:ows='http://www.opengis.net/ows/1.1'>" + 
            "<ows:Identifier>gt:buffer</ows:Identifier>" + 
             "<wps:DataInputs>" + 
                "<wps:Input>" + 
                    "<ows:Identifier>geom1</ows:Identifier>" + 
                    "<wps:Data>" +
                      "<wps:ComplexData>" + 
                        "<gml:Polygon xmlns:gml='http://www.opengis.net/gml'>" +
                          "<gml:exterior>" + 
                            "<gml:LinearRing>" + 
                              "<gml:coordinates>1 1 2 1 2 2 1 2 1 1</gml:coordinates>" + 
                            "</gml:LinearRing>" + 
                          "</gml:exterior>" + 
                        "</gml:Polygon>" +
                      "</wps:ComplexData>" + 
                    "</wps:Data>" +     
                "</wps:Input>" + 
                "<wps:Input>" + 
                   "<ows:Identifier>buffer</ows:Identifier>" + 
                   "<wps:Data>" + 
                     "<wps:LiteralData>1</wps:LiteralData>" + 
                   "</wps:Data>" + 
                "</wps:Input>" + 
           "</wps:DataInputs>" +
           "<wps:ResponseForm>" +  
             "<wps:ResponseDocument storeExecuteResponse='false'>" + 
               "<wps:Output>" +
                 "<ows:Identifier>geom-buffered</ows:Identifier>" +
               "</wps:Output>" + 
             "</wps:ResponseDocument>" +
           "</wps:ResponseForm>" + 
         "</wps:Execute>";
        
        Document d = postAsDOM( "wps", xml );
        checkValidationErrors(d);
        
        assertEquals( "wps:ExecuteResponse", d.getDocumentElement().getNodeName() );
        
        assertXpathExists( "/wps:ExecuteResponse/wps:Status/wps:ProcessSucceeded", d);
        assertXpathExists( 
            "/wps:ExecuteResponse/wps:ProcessOutputs/wps:Output/wps:Data/wps:ComplexData/gml:Polygon", d);
    }
    
    public void testDataInlineRawOutput() throws Exception {
        String xml =  
          "<wps:Execute service='WPS' version='1.0.0' xmlns:wps='http://www.opengis.net/wps/1.0.0' " + 
              "xmlns:ows='http://www.opengis.net/ows/1.1'>" + 
            "<ows:Identifier>gt:buffer</ows:Identifier>" + 
             "<wps:DataInputs>" + 
                "<wps:Input>" + 
                    "<ows:Identifier>geom1</ows:Identifier>" + 
                    "<wps:Data>" +
                      "<wps:ComplexData>" + 
                        "<gml:Polygon xmlns:gml='http://www.opengis.net/gml'>" +
                          "<gml:exterior>" + 
                            "<gml:LinearRing>" + 
                              "<gml:coordinates>1 1 2 1 2 2 1 2 1 1</gml:coordinates>" + 
                            "</gml:LinearRing>" + 
                          "</gml:exterior>" + 
                        "</gml:Polygon>" +
                      "</wps:ComplexData>" + 
                    "</wps:Data>" +     
                "</wps:Input>" + 
                "<wps:Input>" + 
                   "<ows:Identifier>buffer</ows:Identifier>" + 
                   "<wps:Data>" + 
                     "<wps:LiteralData>1</wps:LiteralData>" + 
                   "</wps:Data>" + 
                "</wps:Input>" + 
           "</wps:DataInputs>" +
           "<wps:ResponseForm>" + 
           "    <wps:RawDataOutput>" + 
           "        <ows:Identifier>geom-buffered</ows:Identifier>" + 
           "    </wps:RawDataOutput>" + 
           "  </wps:ResponseForm>" +
           "</wps:Execute>";
        
        Document d = postAsDOM( "wps", xml );
        checkValidationErrors(d, new GMLConfiguration());
        
        assertEquals( "gml:Polygon", d.getDocumentElement().getNodeName() );
    }

    public void testFeatureCollectionInline() throws Exception {
        String xml = "<wps:Execute service='WPS' version='1.0.0' xmlns:wps='http://www.opengis.net/wps/1.0.0' " + 
              "xmlns:ows='http://www.opengis.net/ows/1.1'>" + 
              "<ows:Identifier>gt:BufferFeatureCollection</ows:Identifier>" + 
               "<wps:DataInputs>" + 
                  "<wps:Input>" + 
                      "<ows:Identifier>features</ows:Identifier>" + 
                      "<wps:Data>" +
                        "<wps:ComplexData>" + 
                             readFileIntoString("states-FeatureCollection.xml") + 
                        "</wps:ComplexData>" + 
                      "</wps:Data>" +     
                  "</wps:Input>" + 
                  "<wps:Input>" + 
                     "<ows:Identifier>buffer</ows:Identifier>" + 
                     "<wps:Data>" + 
                       "<wps:LiteralData>10</wps:LiteralData>" + 
                     "</wps:Data>" + 
                  "</wps:Input>" + 
                 "</wps:DataInputs>" +
                 "<wps:ResponseForm>" +  
                   "<wps:ResponseDocument storeExecuteResponse='false'>" + 
                     "<wps:Output>" +
                       "<ows:Identifier>geom-buffered</ows:Identifier>" +
                     "</wps:Output>" + 
                   "</wps:ResponseDocument>" +
                 "</wps:ResponseForm>" + 
               "</wps:Execute>";
        
        Document d = postAsDOM( "wps", xml );
        print(d);
        checkValidationErrors(d);
        
        assertEquals( "wps:ExecuteResponse", d.getDocumentElement().getNodeName() );
        
        assertXpathExists( "/wps:ExecuteResponse/wps:Status/wps:ProcessSucceeded", d);
        assertXpathExists( 
            "/wps:ExecuteResponse/wps:ProcessOutputs/wps:Output/wps:Data/wps:ComplexData/wfs:FeatureCollection", d);
    }
    
    String readFileIntoString( String filename ) throws IOException {
        BufferedReader in = 
            new BufferedReader( new InputStreamReader(getClass().getResourceAsStream( filename ) ) );
        StringBuffer sb = new StringBuffer();
        String line = null;
        while( (line = in.readLine() ) != null ) {
            sb.append( line );
        }
        in.close();
        return sb.toString();
    }
    
    public void testPlainAddition() throws Exception {
        String xml = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\r\n" + 
            "<wps:Execute service=\"WPS\" version=\"1.0.0\"\r\n" + 
            "        xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\"\r\n" + 
            "        xmlns:xlink=\"http://www.w3.org/1999/xlink\">\r\n" + 
            "        <ows:Identifier>gt:DoubleAddition</ows:Identifier>\r\n" + 
            "        <wps:DataInputs>\r\n" + 
            "                <wps:Input>\r\n" + 
            "                        <ows:Identifier>input_a</ows:Identifier>\r\n" + 
            "                        <wps:Data>\r\n" + 
            "                                <wps:LiteralData>7</wps:LiteralData>\r\n" + 
            "                        </wps:Data>\r\n" + 
            "                </wps:Input>\r\n" + 
            "                <wps:Input>\r\n" + 
            "                        <ows:Identifier>input_b</ows:Identifier>\r\n" + 
            "                        <wps:Data>\r\n" + 
            "                                <wps:LiteralData>7</wps:LiteralData>\r\n" + 
            "                        </wps:Data>\r\n" + 
            "                </wps:Input>\r\n" + 
            "        </wps:DataInputs>\r\n" + 
            "        <wps:ResponseForm>\r\n" + 
            "                <wps:RawDataOutput>\r\n" + 
            "                        <ows:Identifier>result</ows:Identifier>\r\n" + 
            "                </wps:RawDataOutput>\r\n" + 
            "        </wps:ResponseForm>\r\n" + 
            "</wps:Execute>";
        
         MockHttpServletResponse response = postAsServletResponse(root(), xml);
         assertEquals("text/plain", response.getContentType());
         assertEquals("14.0", response.getOutputStreamContent());
    }
}
