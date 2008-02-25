package org.geoserver.wfs.response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.textui.TestRunner;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.geoserver.wfs.WFSTestSupport;

public class GeoJSONTest extends WFSTestSupport {
    
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
    
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        runner.run(GeoJSONTest.class);
    }
}
