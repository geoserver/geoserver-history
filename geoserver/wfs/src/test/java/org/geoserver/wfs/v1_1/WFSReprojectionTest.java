package org.geoserver.wfs.v1_1;

import java.util.StringTokenizer;

import org.geoserver.data.test.MockData;
import org.geoserver.wfs.WFSTestSupport;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WFSReprojectionTest extends WFSTestSupport {

    public void testReprojectionGet() throws Exception {
        
        Document dom1 = getAsDOM("wfs?request=getfeature&service=wfs&version=1.0.0&typename=" + 
            MockData.POLYGONS.getLocalPart());
        Document dom2 = getAsDOM("wfs?request=getfeature&service=wfs&version=1.0.0&typename=" + 
            MockData.POLYGONS.getLocalPart() + "&srsName=epsg:4326");
        
        runTest(dom1,dom2);
        
        
    }
    
    public void testReprojectionPost() throws Exception {
        String xml = "<wfs:GetFeature " + "service=\"WFS\" "
        + "version=\"1.0.0\" "
        + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
        + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
        + "xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> "
        + "<wfs:Query typeName=\"" + 
            MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart() + "\"> "
        + "<wfs:PropertyName>cgf:polygonProperty</wfs:PropertyName> "
        + "</wfs:Query> " + "</wfs:GetFeature>";
        
        Document dom1 = postAsDOM("wfs", xml);
        
        xml = "<wfs:GetFeature " + "service=\"WFS\" "
        + "version=\"1.0.0\" "
        + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
        + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
        + "xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> "
        + "<wfs:Query srsName=\"epsg:4326\" typeName=\"" + 
            MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart() + "\"> "
        + "<wfs:PropertyName>cgf:polygonProperty</wfs:PropertyName> "
        + "</wfs:Query> " + "</wfs:GetFeature>";
        Document dom2 = postAsDOM("wfs", xml);
        
        runTest(dom1, dom2);
    }
    
    public void runTest( Document dom1, Document dom2 ) throws Exception {
        Element box = getFirstElementByTagName(dom1.getDocumentElement(), "gml:Box");
        Element coordinates = getFirstElementByTagName(box, "gml:coordinates");
        double[] d1 = coordinates(coordinates.getFirstChild().getNodeValue());
        
        box = getFirstElementByTagName(dom2.getDocumentElement(), "gml:Box");
        coordinates = getFirstElementByTagName(box, "gml:coordinates");
        double[] d2 = coordinates(coordinates.getFirstChild().getNodeValue());
    
        double[] d3 = new double[d1.length];
    
        CoordinateReferenceSystem epsg4326 = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem epsg32615 = CRS.decode("EPSG:32615");
        
        MathTransform tx = CRS.findMathTransform(epsg32615, epsg4326);
        tx.transform(d1, 0, d3, 0, d1.length/2);
        
        for ( int i = 0; i < d2.length; i++ ) {
            assertEquals( d2[i], d3[i], 0.000001 );
        }
    }
    
    double[] coordinates(String string) {
        StringTokenizer st = new StringTokenizer(string, " ");
        double[] coordinates = new double[st.countTokens()*2];
        int i = 0;
        while(st.hasMoreTokens()) {
            String tuple = st.nextToken();
            coordinates[i++] = Double.parseDouble(tuple.split(",")[0]);
            coordinates[i++] = Double.parseDouble(tuple.split(",")[1]);
        }
        
        return coordinates;
    }
    
}
