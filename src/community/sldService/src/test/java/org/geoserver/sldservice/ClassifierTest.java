package org.geoserver.sldservice;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.w3c.dom.Document;

public class ClassifierTest extends CatalogRESTTestSupport {

    public void testUniqueIntervalGetAsXML() throws Exception {
        Document dom = getAsDOM( "/rest/sldservice/cite:Buildings/classify.xml?attribute=ADDRESS&method=uniqueInterval&intervals=1&open=true&ramp=random");
        
        assertEquals( "Rules", dom.getDocumentElement().getNodeName() );
        assertXpathEvaluatesTo("2", "count(//Rule)", dom );
    }
    
    public void testUniqueIntervalGetAsHTML() throws Exception {
        getAsDOM("/rest/sldservice/cite:Buildings/classify.xml?attribute=ADDRESS&method=uniqueInterval&intervals=1&open=true&ramp=random" );
    }

    public void testUniqueIntervalGetAsJSON() throws Exception {
        JSON json = getAsJSON( "/rest/sldservice/cite:Buildings/classify.json?attribute=ADDRESS&method=uniqueInterval&intervals=1&open=true&ramp=random");
        assertTrue( json instanceof JSONObject );
        
        Object rules = ((JSONObject)json).getJSONObject("Rules").get("Rule");
        assertNotNull( rules );
        
        if( rules instanceof JSONArray ) {
            assertEquals( 2 , ((JSONArray)rules).size() );    
        }
    }

    public void testQuantileGetAsXML() throws Exception {
        Document dom = getAsDOM( "/rest/sldservice/cite:Buildings/classify.xml?attribute=ADDRESS&method=quantile&intervals=1&open=true&ramp=random");
        
        assertEquals( "Rules", dom.getDocumentElement().getNodeName() );
        assertXpathEvaluatesTo("1", "count(//Rule)", dom );
    }

}