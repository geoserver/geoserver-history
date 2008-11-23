package org.geoserver.wfs.response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Test;
import junit.textui.TestRunner;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.geoserver.wfs.WFSTestSupport;

public class GeoJSONTest extends WFSTestSupport {
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GeoJSONTest());
    }
	
    public void testGet() throws Exception {	
    	InputStream is = get("wfs?request=GetFeature&version=1.0.0&typename=sf:PrimitiveGeoFeature&maxfeatures=1&outputformat=json");
    	BufferedReader in = new BufferedReader(new InputStreamReader(is));
    	StringBuffer buffer = new StringBuffer();
    	String line;
    	while (( line = in.readLine()) != null) {
    		    buffer.append(line);
    	}
    	String out = buffer.toString();
    	
    	JSONObject rootObject = JSONObject.fromObject( out );
    	assertEquals(rootObject.get("type"),"FeatureCollection");
    	JSONArray featureCol = rootObject.getJSONArray("features");
    	JSONObject aFeature = featureCol.getJSONObject(0);
    	assertEquals(aFeature.getString("geometry_name"),"surfaceProperty");
    }

    public void testPost() throws Exception {
        String xml = "<wfs:GetFeature " + "service=\"WFS\" " + "outputFormat=\"json\" "
                + "version=\"1.0.0\" "
                + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> "
                + "<wfs:Query typeName=\"sf:PrimitiveGeoFeature\"> "
                + "</wfs:Query> " + "</wfs:GetFeature>";

        InputStream is = post( "wfs", xml );
    	BufferedReader in = new BufferedReader(new InputStreamReader(is));
    	StringBuffer buffer = new StringBuffer();
    	String line;
    	while (( line = in.readLine()) != null) {
    		    buffer.append(line);
    	}
    	String out = buffer.toString();
    	
    	JSONObject rootObject = JSONObject.fromObject( out );
    	assertEquals(rootObject.get("type"),"FeatureCollection");
    	JSONArray featureCol = rootObject.getJSONArray("features");
    	JSONObject aFeature = featureCol.getJSONObject(0);
    	assertEquals(aFeature.getString("geometry_name"),"surfaceProperty");
    }

    public void testGeometryCollection() throws Exception {
    	InputStream is = get("wfs?request=GetFeature&version=1.0.0&typename=sf:AggregateGeoFeature&maxfeatures=3&outputformat=json");
    	BufferedReader in = new BufferedReader(new InputStreamReader(is));
    	StringBuffer buffer = new StringBuffer();
    	String line;
    	while (( line = in.readLine()) != null) {
    		    buffer.append(line);
    	}
    	
    	String out = buffer.toString();
    	
    	JSONObject rootObject = JSONObject.fromObject( out );
    	assertEquals(rootObject.get("type"),"FeatureCollection");
    	JSONArray featureCol = rootObject.getJSONArray("features");
    	JSONObject aFeature = featureCol.getJSONObject(1);
    	JSONObject aGeometry = aFeature.getJSONObject("geometry");
    	assertEquals(aGeometry.getString("type"),"MultiLineString");
    	JSONArray geomArray = aGeometry.getJSONArray("coordinates");
    	geomArray = geomArray.getJSONArray(0);
    	geomArray = geomArray.getJSONArray(0);
    	assertEquals(geomArray.getString(0), "55.174");
    }
    
    public void testMixedCollection() throws Exception {
        String xml = "<wfs:GetFeature " + "service=\"WFS\" " + "outputFormat=\"json\" "
        + "version=\"1.0.0\" "
        + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
        + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
        + "xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> "
        + "<wfs:Query typeName=\"sf:PrimitiveGeoFeature\" /> "
        + "<wfs:Query typeName=\"sf:AggregateGeoFeature\" /> "
        + "</wfs:GetFeature>";
        //System.out.println("\n" + xml + "\n");
        
        InputStream is = post( "wfs", xml );
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line;
        while (( line = in.readLine()) != null) {
                    buffer.append(line);
        }
        
        String out = buffer.toString();
        //System.out.println("\n" + out + "\n");

        JSONObject rootObject = JSONObject.fromObject( out );
        //System.out.println(rootObject.get("type"));
        assertEquals(rootObject.get("type"),"FeatureCollection");
        
        JSONArray featureCol = rootObject.getJSONArray("features");
        
        // Check that there are at least two different types of features in here
        JSONObject aFeature = featureCol.getJSONObject(1);
        //System.out.println(aFeature.getString("id").substring(0,19));
        assertTrue(aFeature.getString("id").substring(0,19).equalsIgnoreCase("PrimitiveGeoFeature"));          
        aFeature = featureCol.getJSONObject(6);
        //System.out.println(aFeature.getString("id").substring(0,19));
        assertTrue(aFeature.getString("id").substring(0,19).equalsIgnoreCase("AggregateGeoFeature"));
               
        // Check that a feature has the expected attributes
        JSONObject aGeometry = aFeature.getJSONObject("geometry");
        //System.out.println(aGeometry.getString("type"));
        assertEquals(aGeometry.getString("type"),"MultiLineString");
    }
    
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        runner.run(GeoJSONTest.class);
    }
}
