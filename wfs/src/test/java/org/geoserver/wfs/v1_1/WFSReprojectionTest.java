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
    /**
     * TODO: replace this back to 900913 when bug GEOT-1563 is solved
     */
    private static final String TARGET_CRS_CODE = "EPSG:3395";
    MathTransform tx;
    
    protected void setUp() throws Exception {
        super.setUp();
    
        CoordinateReferenceSystem epsgTarget = CRS.decode(TARGET_CRS_CODE);
        CoordinateReferenceSystem epsg32615 = CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:32615");
        
        tx = CRS.findMathTransform(epsg32615, epsgTarget);
    }
    
    public void testGetFeatureGet() throws Exception {
        
        Document dom1 = getAsDOM("wfs?request=getfeature&service=wfs&version=1.0.0&typename=" + 
            MockData.POLYGONS.getLocalPart());
        Document dom2 = getAsDOM("wfs?request=getfeature&service=wfs&version=1.0.0&typename=" + 
            MockData.POLYGONS.getLocalPart() + "&srsName=" + TARGET_CRS_CODE);
        
        runTest(dom1,dom2);
    }
    
    public void testGetFeaturePost() throws Exception {
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
        print(dom1);
        
        xml = "<wfs:GetFeature " + "service=\"WFS\" "
        + "version=\"1.0.0\" "
        + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
        + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
        + "xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> "
        + "<wfs:Query srsName=\"" + TARGET_CRS_CODE + "\" typeName=\"" + 
            MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart() + "\"> "
        + "<wfs:PropertyName>cgf:polygonProperty</wfs:PropertyName> "
        + "</wfs:Query> " + "</wfs:GetFeature>";
        Document dom2 = postAsDOM("wfs", xml);
        print(dom2);
        
        runTest(dom1, dom2);
    }
    
    public void testGetFeatureWithProjectedBoxGet() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.1&typeName=" + 
            MockData.POLYGONS.getLocalPart();
        Document dom = getAsDOM( q );
        print(dom);
        Element envelope = getFirstElementByTagName(dom, "gml:Envelope" );
        String lc = getFirstElementByTagName(envelope, "gml:lowerCorner" )
            .getFirstChild().getNodeValue();
        String uc = getFirstElementByTagName(envelope, "gml:upperCorner" )
            .getFirstChild().getNodeValue();
        double[] c = new double[]{
            Double.parseDouble(lc.split( " " )[0]), Double.parseDouble(lc.split( " " )[1]),
            Double.parseDouble(uc.split( " " )[0]), Double.parseDouble(uc.split( " " )[1]) 
        };
        double[] cr = new double[4];
        tx.transform(c, 0, cr, 0, 2);
        
        q += "&bbox=" + cr[0] + "," + cr[1] + "," + cr[2] + "," + cr[3] + "," + TARGET_CRS_CODE;
        dom = getAsDOM( q );
        
        assertEquals( 1, dom.getElementsByTagName( MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart()).getLength() );
    }
    
    public void testGetFeatureWithProjectedBoxPost() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.1&typeName=" + 
            MockData.POLYGONS.getLocalPart();
        Document dom = getAsDOM( q );
        Element envelope = getFirstElementByTagName(dom, "gml:Envelope" );
        String lc = getFirstElementByTagName(envelope, "gml:lowerCorner" )
            .getFirstChild().getNodeValue();
        String uc = getFirstElementByTagName(envelope, "gml:upperCorner" )
            .getFirstChild().getNodeValue();
        double[] c = new double[]{
            Double.parseDouble(lc.split( " " )[0]), Double.parseDouble(lc.split( " " )[1]),
            Double.parseDouble(uc.split( " " )[0]), Double.parseDouble(uc.split( " " )[1]) 
        };
        double[] cr = new double[4];
        tx.transform(c, 0, cr, 0, 2);
        
        String xml = "<wfs:GetFeature service=\"WFS\" version=\"1.1.0\""
            + " xmlns:" + MockData.POLYGONS.getPrefix() + "=\"" + MockData.POLYGONS.getNamespaceURI() + "\""
            + " xmlns:ogc=\"http://www.opengis.net/ogc\" "
            + " xmlns:gml=\"http://www.opengis.net/gml\" "
            + " xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> "
            + "<wfs:Query typeName=\"" + MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart() + "\">"
            + "<wfs:PropertyName>cgf:polygonProperty</wfs:PropertyName> "
            + "<ogc:Filter>" 
            +  "<ogc:BBOX>"
            +   "<ogc:PropertyName>polygonProperty</ogc:PropertyName>" 
            +   "<gml:Envelope srsName=\"" + TARGET_CRS_CODE + "\">"
            +      "<gml:lowerCorner>" + cr[0] + " " + cr[1] + "</gml:lowerCorner>"
            +      "<gml:upperCorner>" + cr[2] + " " + cr[3] + "</gml:upperCorner>"
            +   "</gml:Envelope>"  
            +  "</ogc:BBOX>" 
            + "</ogc:Filter>"
            + "</wfs:Query> " + "</wfs:GetFeature>";
        
        dom = postAsDOM( "wfs", xml );
        
        assertEquals( 1, dom.getElementsByTagName( MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart()).getLength() );
    }
    
    public void testInsertSrsName() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.1&typeName=" + 
            MockData.POLYGONS.getLocalPart();
        Document dom = getAsDOM( q );
        print(dom);
        assertEquals( 1, dom.getElementsByTagName( MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart()).getLength() );
        
        Element polygonProperty = getFirstElementByTagName(dom, "cgf:polygonProperty");
        Element posList = getFirstElementByTagName( polygonProperty, "gml:posList");
        
        double[] c = posList(posList.getFirstChild().getNodeValue());
        double[] cr = new double[c.length];
        tx.transform(c, 0, cr, 0, cr.length/2);
        
        String xml = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" "
        + " xmlns:wfs=\"http://www.opengis.net/wfs\" "
        + " xmlns:gml=\"http://www.opengis.net/gml\" "
        + " xmlns:cgf=\"" + MockData.CGF_URI + "\">"
        + "<wfs:Insert handle=\"insert-1\" srsName=\"" + TARGET_CRS_CODE + "\">"
        + " <cgf:Polygons>"
        +    "<cgf:polygonProperty>"
        +      "<gml:Polygon >" 
        +       "<gml:exterior>"
        +          "<gml:LinearRing>" 
        +             "<gml:posList>";
        for ( int i = 0; i < cr.length; i++ ) {
            xml += cr[i];
            if ( i < cr.length - 1 ) {
                xml += " ";
            }
        }
        xml +=        "</gml:posList>"
        +        "</gml:LinearRing>"
        +      "</gml:exterior>"
        +    "</gml:Polygon>"
        +   "</cgf:polygonProperty>"
        + " </cgf:Polygons>"
        + "</wfs:Insert>"
        + "</wfs:Transaction>";
        postAsDOM( "wfs", xml );
        
        dom = getAsDOM( q );
        print(dom);
        assertEquals( 2, dom.getElementsByTagName( MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart()).getLength() );
        
    }
    
    public void testInsertGeomSrsName() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.1&typeName=" + 
            MockData.POLYGONS.getLocalPart();
        Document dom = getAsDOM( q );
        
        Element polygonProperty = getFirstElementByTagName(dom, "cgf:polygonProperty");
        Element posList = getFirstElementByTagName( polygonProperty, "gml:posList");
        
        double[] c = posList(posList.getFirstChild().getNodeValue());
        double[] cr = new double[c.length];
        tx.transform(c, 0, cr, 0, cr.length/2);
        
        String xml = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" "
        + " xmlns:wfs=\"http://www.opengis.net/wfs\" "
        + " xmlns:gml=\"http://www.opengis.net/gml\" "
        + " xmlns:cgf=\"" + MockData.CGF_URI + "\">"
        + "<wfs:Insert handle=\"insert-1\">"
        + " <cgf:Polygons>"
        +    "<cgf:polygonProperty>"
        +      "<gml:Polygon srsName=\"" + TARGET_CRS_CODE + "\">" 
        +       "<gml:exterior>"
        +          "<gml:LinearRing>" 
        +             "<gml:posList>";
        for ( int i = 0; i < cr.length; i++ ) {
            xml += cr[i];
            if ( i < cr.length - 1 ) {
                xml += " ";
            }
        }
        xml +=        "</gml:posList>"
        +        "</gml:LinearRing>"
        +      "</gml:exterior>"
        +    "</gml:Polygon>"
        +   "</cgf:polygonProperty>"
        + " </cgf:Polygons>"
        + "</wfs:Insert>"
        + "</wfs:Transaction>";
        postAsDOM( "wfs", xml );
        
        dom = getAsDOM( q );
        
        assertEquals( 2, dom.getElementsByTagName( MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart()).getLength() );
        
    }
    
    public void testUpdate() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.1&typeName=" + 
        MockData.POLYGONS.getLocalPart();
        
        Document dom = getAsDOM( q );
        print(dom);
        
        Element polygonProperty = getFirstElementByTagName(dom, "cgf:polygonProperty");
        Element posList = getFirstElementByTagName( polygonProperty, "gml:posList");
        
        double[] c = posList(posList.getFirstChild().getNodeValue());
        double[] cr = new double[c.length];
        tx.transform(c, 0, cr, 0, cr.length/2);
        
        // perform an update
        String xml = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" "
                + "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\"> "
                + "<wfs:Update typeName=\"cgf:Polygons\" > " + "<wfs:Property>"
                + "<wfs:Name>polygonProperty</wfs:Name>" 
                + "<wfs:Value>" 
                +      "<gml:Polygon srsName=\"" + TARGET_CRS_CODE + "\">" 
                +       "<gml:exterior>"
                +          "<gml:LinearRing>" 
                +             "<gml:posList>";
                for ( int i = 0; i < cr.length; i++ ) {
                    xml += cr[i];
                    if ( i < cr.length - 1 ) {
                        xml += " ";
                    }
                }
                xml +=        "</gml:posList>"
                +        "</gml:LinearRing>"
                +      "</gml:exterior>"
                +    "</gml:Polygon>"
        		+ "</wfs:Value>" 
                + "</wfs:Property>" 
                + "<ogc:Filter>"
                + "<ogc:PropertyIsEqualTo>"
                + "<ogc:PropertyName>id</ogc:PropertyName>"
                + "<ogc:Literal>t0002</ogc:Literal>"
                + "</ogc:PropertyIsEqualTo>" + "</ogc:Filter>"
                + "</wfs:Update>" + "</wfs:Transaction>";
                
        dom = postAsDOM( "wfs", xml );
        assertEquals( "wfs:TransactionResponse", dom.getDocumentElement().getNodeName() );
        Element totalUpdated = getFirstElementByTagName(dom, "wfs:totalUpdated" );
        assertEquals( "1", totalUpdated.getFirstChild().getNodeValue() );
        
        dom = getAsDOM(q);
        polygonProperty = getFirstElementByTagName(dom, "cgf:polygonProperty");
        posList = getFirstElementByTagName( polygonProperty, "gml:posList");
        double[] c1 = posList(posList.getFirstChild().getNodeValue());

        assertEquals( c.length, c1.length );
        for ( int i = 0; i < c.length; i++ ) {
            int x = (int)(c[i] + 0.5);
            int y = (int)(c1[i] + 0.5);
            
            assertEquals(x,y);
        }
        
    }
    
    public void testUpdateReprojectFilter() throws Exception {
        testUpdateReprojectFilter("srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\"");
    }
    
    public void testUpdateReprojectFilterDefaultCRS() throws Exception {
        testUpdateReprojectFilter("");
    }
    
    private void testUpdateReprojectFilter(String envelopeSRS) throws Exception {
        // slightly adapted from CITE WFS 1.1, "Test wfs:wfs-1.1.0-LockFeature-tc3.1"
        
        // perform an update
        String xml = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" "
                + "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\"> "
                + "<wfs:Update handle=\"upd-1\" typeName=\"sf:GenericEntity\">" 
                + "<wfs:Property>"
                + "  <wfs:Name>sf:description</wfs:Name>"
                + "  <wfs:Value>bla bla</wfs:Value>"
                + "</wfs:Property>"
                + "  <ogc:Filter>"
                + "    <ogc:BBOX>"
                + "      <ogc:PropertyName>sf:attribut.geom</ogc:PropertyName>"
                + "        <gml:Envelope " + envelopeSRS + ">"
                + "          <gml:lowerCorner>34.5 -10.0</gml:lowerCorner>"
                + "          <gml:upperCorner>72.0 32.0</gml:upperCorner>"
                + "        </gml:Envelope>"
                + "    </ogc:BBOX>"
                + "  </ogc:Filter>"
                + "</wfs:Update> </wfs:Transaction>";
        
        Document dom = postAsDOM( "wfs", xml );
        assertEquals( "wfs:TransactionResponse", dom.getDocumentElement().getNodeName() );
        Element totalUpdated = getFirstElementByTagName(dom, "wfs:totalUpdated" );
        assertEquals( "3", totalUpdated.getFirstChild().getNodeValue() );
    }
    
    public void testDeleteReprojectFilter() throws Exception{
        testDeleteReprojectFilter("srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\"");
    }
    
    public void testDeleteReprojectFilterDefaultCRS() throws Exception{
        testDeleteReprojectFilter("");
    }
    
    private void testDeleteReprojectFilter(String envelopeSRS) throws Exception {
        // slightly adapted from CITE WFS 1.1, "Test wfs:wfs-1.1.0-LockFeature-tc3.1"
        
        // perform an update
        String xml = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" "
                + "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\"> "
                + "<wfs:Delete typeName=\"sf:GenericEntity\">" 
                + "  <ogc:Filter>"
                + "    <ogc:BBOX>"
                + "      <ogc:PropertyName>sf:attribut.geom</ogc:PropertyName>"
                + "        <gml:Envelope " + envelopeSRS + ">"
                + "          <gml:lowerCorner>34.5 -10.0</gml:lowerCorner>"
                + "          <gml:upperCorner>72.0 32.0</gml:upperCorner>"
                + "        </gml:Envelope>"
                + "    </ogc:BBOX>"
                + "  </ogc:Filter>"
                + "</wfs:Delete> </wfs:Transaction>";
        
        Document dom = postAsDOM( "wfs", xml );
        assertEquals( "wfs:TransactionResponse", dom.getDocumentElement().getNodeName() );
        Element totalUpdated = getFirstElementByTagName(dom, "wfs:totalDeleted" );
        assertEquals( "3", totalUpdated.getFirstChild().getNodeValue() );
    }
    
    public void testLockReprojectFilter() throws Exception {
        testLockReprojectFilter("srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\"");
    }
    
    public void testLockReprojectFilterDefaultCRS() throws Exception {
        testLockReprojectFilter("");
    }

    
    private void testLockReprojectFilter(String envelopeSRS) throws Exception {
        // slightly adapted from CITE WFS 1.1, "Test wfs:wfs-1.1.0-LockFeature-tc3.1"
        
        // perform a lock
        String xml = "<wfs:LockFeature xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + "xmlns:sf=\"http://cite.opengeospatial.org/gmlsf\" "
                + "xmlns:myparsers=\"http://teamengine.sourceforge.net/parsers\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\" "
                + "xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                + "expiry=\"5\" "
                + "handle=\"LockFeature-tc3\" "
                + "lockAction=\"ALL\" "
                + "service=\"WFS\" "
                + "version=\"1.1.0\"> "
                + "<wfs:Lock handle=\"lock-1\" typeName=\"sf:GenericEntity\"> "
                + "<ogc:Filter>"
                + "<ogc:BBOX>"
                + " <ogc:PropertyName>sf:attribut.geom</ogc:PropertyName>"
                + " <gml:Envelope " + envelopeSRS + ">"
                + "    <gml:lowerCorner>34.5 -10.0</gml:lowerCorner>"
                + "    <gml:upperCorner>72.0 32.0</gml:upperCorner>"
                + " </gml:Envelope>"
                + "</ogc:BBOX>"
                + "</ogc:Filter>"
                + "</wfs:Lock>"
                + "</wfs:LockFeature>";
        System.out.println(xml);
        
        Document dom = postAsDOM( "wfs", xml );
        assertEquals( "wfs:LockFeatureResponse", dom.getDocumentElement().getNodeName() );
        assertEquals(3, dom.getElementsByTagName("ogc:FeatureId").getLength());
    }
    
    
    public void runTest( Document dom1, Document dom2 ) throws Exception {
        Element box = getFirstElementByTagName(dom1.getDocumentElement(), "gml:Box");
        Element coordinates = getFirstElementByTagName(box, "gml:coordinates");
        double[] d1 = coordinates(coordinates.getFirstChild().getNodeValue());
        
        box = getFirstElementByTagName(dom2.getDocumentElement(), "gml:Box");
        coordinates = getFirstElementByTagName(box, "gml:coordinates");
        double[] d2 = coordinates(coordinates.getFirstChild().getNodeValue());
    
        double[] d3 = new double[d1.length];
        tx.transform(d1, 0, d3, 0, d1.length/2);
        
        for ( int i = 0; i < d2.length; i++ ) {
            assertEquals( d2[i], d3[i], 0.001 );
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
    
    double[] posList(String string) {
        StringTokenizer st = new StringTokenizer(string, " ");
        double[] coordinates = new double[st.countTokens()];
        int i = 0;
        while(st.hasMoreTokens()) {
            coordinates[i++] = Double.parseDouble(st.nextToken());
        }
        
        return coordinates;
    }
    
}
