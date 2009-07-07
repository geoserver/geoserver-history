package org.geoserver.sldservice;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.w3c.dom.Document;

public class ListAttributesTest extends CatalogRESTTestSupport {

    public void testGetAsXML() throws Exception {
        Document dom = getAsDOM( "/rest/sldservice/cite:Buildings/attributes.xml");
        
        assertEquals( "Attributes", dom.getDocumentElement().getNodeName() );
        assertXpathEvaluatesTo("cite:Buildings", "/Attributes/@layer", dom );
        assertXpathEvaluatesTo("3", "count(//Attribute)", dom );
    }
    
    public void testGetAsHTML() throws Exception {
        getAsDOM("/rest/sldservice/cite:Buildings/attributes.html" );
    }

    public void testGetAsJSON() throws Exception {
        JSON json = getAsJSON( "/rest/sldservice/cite:Buildings/attributes.json");
        assertTrue( json instanceof JSONObject );
        
        Object attributes = ((JSONObject)json).getJSONObject("Attributes").get("Attribute");
        assertNotNull( attributes );
        
        if( attributes instanceof JSONArray ) {
            assertEquals( 3 , ((JSONArray)attributes).size() );    
        }
    }

}