package org.geoserver.filter.function;

import static org.custommonkey.xmlunit.XMLAssert.*;

import org.geoserver.wfs.WFSTestSupport;
import org.w3c.dom.Document;

public class WFSFilteringTest extends WFSTestSupport {

    static final String QUERY_SINGLE = //
    "<wfs:GetFeature xmlns:wfs=\"http://www.opengis.net/wfs\"\n" + //
            "                xmlns:cite=\"http://www.opengis.net/cite\"\n" + //
            "                xmlns:ogc=\"http://www.opengis.net/ogc\"\n" + //
            "                service=\"WFS\" version=\"1.0.0\">\n" + //
            "  <wfs:Query typeName=\"cite:Buildings\">\n" + //
            "    <ogc:Filter>\n" + //
            "      <ogc:DWithin>\n" + // 
            "        <ogc:PropertyName>the_geom</ogc:PropertyName>\n" + // 
            "        <ogc:Function name=\"querySingle\">\n" + //
            "           <ogc:Literal>cite:Streams</ogc:Literal>\n" + // 
            "           <ogc:Literal>the_geom</ogc:Literal>\n" + // 
            "           <ogc:Literal>INCLUDE</ogc:Literal>\n" + //
            "        </ogc:Function>\n" + //
            "        <ogc:Distance units=\"meter\">${distance}</ogc:Distance>\n" + // 
            "      </ogc:DWithin>\n" + //
            "    </ogc:Filter>\n" + //
            "  </wfs:Query>\n" + //
            "</wfs:GetFeature>";

    static final String QUERY_MULTI = //
    "<wfs:GetFeature xmlns:wfs=\"http://www.opengis.net/wfs\"\n" + // 
            "  xmlns:cite=\"http://www.opengis.net/cite\" " + //
            "  xmlns:ogc=\"http://www.opengis.net/ogc\"\n" + //
            "  service=\"WFS\" version=\"1.0.0\">\n" + //
            "  <wfs:Query typeName=\"cite:Buildings\">\n" + // 
            "    <ogc:Filter>\n" + //
            "      <ogc:DWithin>\n" + //
            "        <ogc:PropertyName>the_geom</ogc:PropertyName>\n" + // 
            "        <ogc:Function name=\"collectGeometries\">\n" + //
            "          <ogc:Function name=\"queryCollection\">\n" + //
            "            <ogc:Literal>cite:RoadSegments</ogc:Literal>\n" + // 
            "            <ogc:Literal>the_geom</ogc:Literal>\n" + //
            "            <ogc:Literal>NAME = 'Route 5'</ogc:Literal>\n" + // 
            "          </ogc:Function>\n" + //
            "        </ogc:Function>\n" + //
            "        <ogc:Distance units=\"meter\">0.001</ogc:Distance>\n" + // 
            "      </ogc:DWithin>\n" + //
            "    </ogc:Filter>\n" + //
            "  </wfs:Query>\n" + //
            "</wfs:GetFeature>";

    public void testSingleSmallDistance() throws Exception {
        String request = QUERY_SINGLE.replace("${distance}", "0.00000001");
        Document doc = postAsDOM("wfs", request);
        // print(doc);

        assertXpathEvaluatesTo("1", "count(//wfs:FeatureCollection)", doc);
        assertXpathEvaluatesTo("0", "count(cite:Buildings)", doc);
    }

    public void testSingleLargeDistance() throws Exception {
        String request = QUERY_SINGLE.replace("${distance}", "0.001");
        Document doc = postAsDOM("wfs", request);
        // print(doc);

        assertXpathEvaluatesTo("1", "count(//wfs:FeatureCollection)", doc);
        assertXpathEvaluatesTo("1", "count(//cite:Buildings)", doc);
    }
    
    public void testMultiple() throws Exception {
        Document doc = postAsDOM("wfs", QUERY_MULTI);
        // print(doc);

        assertXpathEvaluatesTo("1", "count(//wfs:FeatureCollection)", doc);
        assertXpathEvaluatesTo("2", "count(//cite:Buildings)", doc);
    }

}
