package org.geoserver.wps.jts;

import org.geoserver.wps.WPSTestSupport;

import com.mockrunner.mock.web.MockHttpServletResponse;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryProcessWPSTest extends WPSTestSupport {

	public void testBuffer() throws Exception {
        String xml =  
            "<wps:Execute service='WPS' version='1.0.0' xmlns:wps='http://www.opengis.net/wps/1.0.0' " + 
                "xmlns:ows='http://www.opengis.net/ows/1.1'>" + 
              "<ows:Identifier>JTS:buffer</ows:Identifier>" + 
               "<wps:DataInputs>" + 
                  "<wps:Input>" + 
                      "<ows:Identifier>geom</ows:Identifier>" + 
                      "<wps:Data>" +
                        "<wps:ComplexData mimeType=\"application/wkt\">" +
                          "<![CDATA[POINT(0 0)]]>" +
                        "</wps:ComplexData>" + 
                      "</wps:Data>" +     
                  "</wps:Input>" + 
                  "<wps:Input>" + 
                     "<ows:Identifier>distance</ows:Identifier>" + 
                     "<wps:Data>" + 
                       "<wps:LiteralData>1</wps:LiteralData>" + 
                     "</wps:Data>" + 
                  "</wps:Input>" + 
             "</wps:DataInputs>" +
             "<wps:ResponseForm>" + 
             "    <wps:RawDataOutput mimeType=\"application/wkt\">" + 
             "        <ows:Identifier>result</ows:Identifier>" + 
             "    </wps:RawDataOutput>" + 
             "  </wps:ResponseForm>" +
             "</wps:Execute>";
          
          MockHttpServletResponse response = postAsServletResponse( "wps", xml );
          // System.out.println(response.getOutputStreamContent());
          assertEquals("application/wkt", response.getContentType());
          Geometry g = new WKTReader().read(response.getOutputStreamContent());
          assertTrue(g instanceof Polygon);

	}
}
