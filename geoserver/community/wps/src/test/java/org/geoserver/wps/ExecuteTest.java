package org.geoserver.wps;

import org.w3c.dom.Document;

import static org.custommonkey.xmlunit.XMLAssert.*;

public class ExecuteTest extends WPSTestSupport {

    public void testDataInline() throws Exception {
        String xml =  
          "<wps:Execute service='WPS' version='1.0.0' xmlns:wps='http://www.opengis.net/wps/1.0.0' " + 
              "xmlns:ows='http://www.opengis.net/ows/1.1'>" + 
            "<ows:Identifier>buffer</ows:Identifier>" + 
             "<wps:DataInputs>" + 
                "<wps:Input>" + 
                    "<ows:Identifier>geom1</ows:Identifier>" + 
                    "<wps:Data>" +
                      "<wps:ComplexData>" + 
                        "<gml:Polygon xmlns:gml='http://www.opengis.net/gml'>" +
                          "<gml:outerBoundaryIs>" + 
                            "<gml:LinearRing>" + 
                              "<gml:coordinates>1,1 2,1 2,2 1,2 1,1</gml:coordinates>" + 
                            "</gml:LinearRing>" + 
                          "</gml:outerBoundaryIs>" + 
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
        assertEquals( "wps:ExecuteResponse", d.getDocumentElement().getNodeName() );
        
        assertXpathExists( "/wps:ExecuteResponse/wps:Status/wps:ProcessSucceeded", d);
        assertXpathExists( 
            "/wps:ExecuteResponse/wps:ProcessOutputs/wps:Output/wps:Data/wps:ComplexData/gml:Polygon", d);
    }


}
