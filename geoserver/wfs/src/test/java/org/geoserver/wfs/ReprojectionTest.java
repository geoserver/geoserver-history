/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import org.geoserver.data.test.MockData;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.StringTokenizer;


public class ReprojectionTest extends WFSTestSupport {
    MathTransform tx;

    protected void setUp() throws Exception {
        super.setUp();

        CoordinateReferenceSystem epsg4326 = CRS.decode("EPSG:900913");
        CoordinateReferenceSystem epsg32615 = CRS.decode("EPSG:32615");

        tx = CRS.findMathTransform(epsg32615, epsg4326);
    }

    public void testGetFeatureGet() throws Exception {
        Document dom1 = getAsDOM("wfs?request=getfeature&service=wfs&version=1.0.0&typename="
                + MockData.POLYGONS.getLocalPart());
        Document dom2 = getAsDOM("wfs?request=getfeature&service=wfs&version=1.0.0&typename="
                + MockData.POLYGONS.getLocalPart() + "&srsName=epsg:900913");

        print(dom1);
        print(dom2);

        runTest(dom1, dom2);
    }

    public void testGetFeaturePost() throws Exception {
        String xml = "<wfs:GetFeature " + "service=\"WFS\" " + "version=\"1.0.0\" "
            + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
            + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
            + "xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> " + "<wfs:Query typeName=\""
            + MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart() + "\"> "
            + "<wfs:PropertyName>cgf:polygonProperty</wfs:PropertyName> " + "</wfs:Query> "
            + "</wfs:GetFeature>";

        Document dom1 = postAsDOM("wfs", xml);

        xml = "<wfs:GetFeature " + "service=\"WFS\" " + "version=\"1.0.0\" "
            + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
            + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
            + "xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> "
            + "<wfs:Query srsName=\"epsg:900913\" typeName=\"" + MockData.POLYGONS.getPrefix()
            + ":" + MockData.POLYGONS.getLocalPart() + "\"> "
            + "<wfs:PropertyName>cgf:polygonProperty</wfs:PropertyName> " + "</wfs:Query> "
            + "</wfs:GetFeature>";

        Document dom2 = postAsDOM("wfs", xml);

        runTest(dom1, dom2);
    }

    public void testGetFeatureWithProjectedBoxGet() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.0&typeName="
            + MockData.POLYGONS.getLocalPart();
        Document dom = getAsDOM(q);

        Element envelope = getFirstElementByTagName(dom, "gml:Box");
        String coordinates = getFirstElementByTagName(envelope, "gml:coordinates").getFirstChild()
                                 .getNodeValue();
        String lc = coordinates.split(" ")[0];
        String uc = coordinates.split(" ")[1];

        double[] c = new double[] {
                Double.parseDouble(lc.split(",")[0]), Double.parseDouble(lc.split(",")[1]),
                Double.parseDouble(uc.split(",")[0]), Double.parseDouble(uc.split(",")[1])
            };
        double[] cr = new double[4];
        tx.transform(c, 0, cr, 0, 2);

        q += ("&bbox=" + cr[0] + "," + cr[1] + "," + cr[2] + "," + cr[3] + ",epsg:900913");
        dom = getAsDOM(q);

        assertEquals(1,
            dom.getElementsByTagName(MockData.POLYGONS.getPrefix() + ":"
                + MockData.POLYGONS.getLocalPart()).getLength());
    }

    public void testGetFeatureWithProjectedBoxPost() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.0&typeName="
            + MockData.POLYGONS.getLocalPart();
        Document dom = getAsDOM(q);
        Element envelope = getFirstElementByTagName(dom, "gml:Box");
        String coordinates = getFirstElementByTagName(envelope, "gml:coordinates").getFirstChild()
                                 .getNodeValue();
        String lc = coordinates.split(" ")[0];
        String uc = coordinates.split(" ")[1];

        double[] c = new double[] {
                Double.parseDouble(lc.split(",")[0]), Double.parseDouble(lc.split(",")[1]),
                Double.parseDouble(uc.split(",")[0]), Double.parseDouble(uc.split(",")[1])
            };
        double[] cr = new double[4];
        tx.transform(c, 0, cr, 0, 2);

        String xml = "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\"" + " xmlns:"
            + MockData.POLYGONS.getPrefix() + "=\"" + MockData.POLYGONS.getNamespaceURI() + "\""
            + " xmlns:ogc=\"http://www.opengis.net/ogc\" "
            + " xmlns:gml=\"http://www.opengis.net/gml\" "
            + " xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> " + "<wfs:Query typeName=\""
            + MockData.POLYGONS.getPrefix() + ":" + MockData.POLYGONS.getLocalPart() + "\">"
            + "<wfs:PropertyName>cgf:polygonProperty</wfs:PropertyName> " + "<ogc:Filter>"
            + "<ogc:BBOX>" + "<ogc:PropertyName>polygonProperty</ogc:PropertyName>"
            + "<gml:Box srsName=\"epsg:900913\">" + "<gml:coord>" + "<gml:X>" + cr[0] + "</gml:X>"
            + "<gml:Y>" + cr[1] + "</gml:Y>" + "</gml:coord>" + "<gml:coord>" + "<gml:X>" + cr[2]
            + "</gml:X>" + "<gml:Y>" + cr[3] + "</gml:Y>" + "</gml:coord>" + "</gml:Box>"
            + "</ogc:BBOX>" + "</ogc:Filter>" + "</wfs:Query> " + "</wfs:GetFeature>";

        dom = postAsDOM("wfs", xml);

        assertEquals(1,
            dom.getElementsByTagName(MockData.POLYGONS.getPrefix() + ":"
                + MockData.POLYGONS.getLocalPart()).getLength());
    }

    public void testInsertSrsName() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.0.0&typeName="
            + MockData.POLYGONS.getLocalPart();
        Document dom = getAsDOM(q);

        Element polygonProperty = getFirstElementByTagName(dom, "cgf:polygonProperty");
        Element posList = getFirstElementByTagName(polygonProperty, "gml:coordinates");

        double[] c = coordinates(posList.getFirstChild().getNodeValue());
        double[] cr = new double[c.length];
        tx.transform(c, 0, cr, 0, cr.length / 2);

        String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\" "
            + " xmlns:wfs=\"http://www.opengis.net/wfs\" "
            + " xmlns:gml=\"http://www.opengis.net/gml\" " + " xmlns:cgf=\"" + MockData.CGF_URI
            + "\">" + "<wfs:Insert handle=\"insert-1\" srsName=\"epsg:900913\">"
            + " <cgf:Polygons>" + "<cgf:polygonProperty>" + "<gml:Polygon >"
            + "<gml:outerBoundaryIs>" + "<gml:LinearRing>" + "<gml:coordinates>";

        for (int i = 0; i < cr.length;) {
            xml += (cr[i++] + "," + cr[i++]);

            if (i < (cr.length - 1)) {
                xml += " ";
            }
        }

        xml += ("</gml:coordinates>" + "</gml:LinearRing>" + "</gml:outerBoundaryIs>"
        + "</gml:Polygon>" + "</cgf:polygonProperty>" + " </cgf:Polygons>" + "</wfs:Insert>"
        + "</wfs:Transaction>");
        postAsDOM("wfs", xml);

        dom = getAsDOM(q);

        assertEquals(2,
            dom.getElementsByTagName(MockData.POLYGONS.getPrefix() + ":"
                + MockData.POLYGONS.getLocalPart()).getLength());
    }

    public void testInsertGeomSrsName() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.0&typeName="
            + MockData.POLYGONS.getLocalPart();
        Document dom = getAsDOM(q);

        Element polygonProperty = getFirstElementByTagName(dom, "cgf:polygonProperty");
        Element posList = getFirstElementByTagName(polygonProperty, "gml:coordinates");

        double[] c = coordinates(posList.getFirstChild().getNodeValue());
        double[] cr = new double[c.length];
        tx.transform(c, 0, cr, 0, cr.length / 2);

        String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\" "
            + " xmlns:wfs=\"http://www.opengis.net/wfs\" "
            + " xmlns:gml=\"http://www.opengis.net/gml\" " + " xmlns:cgf=\"" + MockData.CGF_URI
            + "\">" + "<wfs:Insert handle=\"insert-1\">" + " <cgf:Polygons>"
            + "<cgf:polygonProperty>" + "<gml:Polygon srsName=\"epsg:900913\">"
            + "<gml:outerBoundaryIs>" + "<gml:LinearRing>" + "<gml:coordinates>";

        for (int i = 0; i < cr.length;) {
            xml += (cr[i++] + "," + cr[i++]);

            if (i < (cr.length - 1)) {
                xml += " ";
            }
        }

        xml += ("</gml:coordinates>" + "</gml:LinearRing>" + "</gml:outerBoundaryIs>"
        + "</gml:Polygon>" + "</cgf:polygonProperty>" + " </cgf:Polygons>" + "</wfs:Insert>"
        + "</wfs:Transaction>");
        postAsDOM("wfs", xml);

        dom = getAsDOM(q);

        assertEquals(2,
            dom.getElementsByTagName(MockData.POLYGONS.getPrefix() + ":"
                + MockData.POLYGONS.getLocalPart()).getLength());
    }

    public void testUpdate() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.0&typeName="
            + MockData.POLYGONS.getLocalPart();

        Document dom = getAsDOM(q);

        Element polygonProperty = getFirstElementByTagName(dom, "cgf:polygonProperty");
        Element posList = getFirstElementByTagName(polygonProperty, "gml:coordinates");

        double[] c = coordinates(posList.getFirstChild().getNodeValue());
        double[] cr = new double[c.length];
        tx.transform(c, 0, cr, 0, cr.length / 2);

        // perform an update
        String xml = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\" "
            + "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" "
            + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
            + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
            + "xmlns:gml=\"http://www.opengis.net/gml\"> "
            + "<wfs:Update typeName=\"cgf:Polygons\" > " + "<wfs:Property>"
            + "<wfs:Name>polygonProperty</wfs:Name>" + "<wfs:Value>"
            + "<gml:Polygon srsName=\"epsg:900913\">" + "<gml:outerBoundaryIs>"
            + "<gml:LinearRing>" + "<gml:coordinates>";

        for (int i = 0; i < cr.length;) {
            xml += (cr[i++] + "," + cr[i++]);

            if (i < (cr.length - 1)) {
                xml += " ";
            }
        }

        xml += ("</gml:coordinates>" + "</gml:LinearRing>" + "</gml:outerBoundaryIs>"
        + "</gml:Polygon>" + "</wfs:Value>" + "</wfs:Property>" + "<ogc:Filter>"
        + "<ogc:PropertyIsEqualTo>" + "<ogc:PropertyName>id</ogc:PropertyName>"
        + "<ogc:Literal>t0002</ogc:Literal>" + "</ogc:PropertyIsEqualTo>" + "</ogc:Filter>"
        + "</wfs:Update>" + "</wfs:Transaction>");

        dom = postAsDOM("wfs", xml);

        assertEquals("wfs:WFS_TransactionResponse", dom.getDocumentElement().getNodeName());

        Element success = getFirstElementByTagName(dom, "wfs:SUCCESS");
        assertNotNull(success);

        dom = getAsDOM(q);

        polygonProperty = getFirstElementByTagName(dom, "cgf:polygonProperty");
        posList = getFirstElementByTagName(polygonProperty, "gml:coordinates");

        double[] c1 = coordinates(posList.getFirstChild().getNodeValue());

        assertEquals(c.length, c1.length);

        for (int i = 0; i < c.length; i++) {
            int x = (int) (c[i] + 0.5);
            int y = (int) (c1[i] + 0.5);

            assertEquals(x, y);
        }
    }

    public void runTest(Document dom1, Document dom2) throws Exception {
        Element box = getFirstElementByTagName(dom1.getDocumentElement(), "gml:Box");
        Element coordinates = getFirstElementByTagName(box, "gml:coordinates");
        double[] d1 = coordinates(coordinates.getFirstChild().getNodeValue());

        box = getFirstElementByTagName(dom2.getDocumentElement(), "gml:Box");
        coordinates = getFirstElementByTagName(box, "gml:coordinates");

        double[] d2 = coordinates(coordinates.getFirstChild().getNodeValue());

        double[] d3 = new double[d1.length];
        tx.transform(d1, 0, d3, 0, d1.length / 2);

        for (int i = 0; i < d2.length; i++) {
            assertEquals(d2[i], d3[i], 0.001);
        }
    }

    double[] coordinates(String string) {
        StringTokenizer st = new StringTokenizer(string, " ");
        double[] coordinates = new double[st.countTokens() * 2];
        int i = 0;

        while (st.hasMoreTokens()) {
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

        while (st.hasMoreTokens()) {
            coordinates[i++] = Double.parseDouble(st.nextToken());
        }

        return coordinates;
    }
}
