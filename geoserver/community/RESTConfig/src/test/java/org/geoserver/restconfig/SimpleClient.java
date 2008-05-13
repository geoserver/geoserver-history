/**
 * 
 */
package org.geoserver.restconfig;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author Alessio
 *
 */
public class SimpleClient {
	
	public static void main(String[] args) {
		final String styleName = "topp:states1_style";
		System.out.println(styleName.substring(0, styleName.lastIndexOf("_")));
		
		final String testSLD = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><StyledLayerDescriptor version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><!-- a named layer is the basic building block of an sld document --><NamedLayer><Name>Default Line</Name><UserStyle><!-- they have names, titles and abstracts --><Title>A boring default style</Title><Abstract>A sample style that just prints out a green line</Abstract><!-- FeatureTypeStyles describe how to render different features --><!-- a feature type for lines --><FeatureTypeStyle><!--FeatureTypeName>Feature</FeatureTypeName--><Rule><Name>Rule 1</Name><Title>Green Line</Title><Abstract>A green line with a 2 pixel width</Abstract><!-- like a polygonsymbolizer --><LineSymbolizer><Stroke><CssParameter name=\"stroke\">#0000FF</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
		
		final SimpleClient client = new SimpleClient();
		URL dssURL;
		try {
			dssURL = new URL("http://localhost:8080/geoserver/rest/styles/" + styleName.replaceAll(":", "_"));
			if(client.put(dssURL, testSLD)) {
				System.out.println("OK");
//				dssURL = new URL("http://localhost:8080/geoserver/rest/folders/states_shapefile/layers/states.xml");
//				client.put(dssURL, "<FeatureType><Style>" + styleName + "</Style></FeatureType>");
				dssURL = new URL("http://localhost:8080/geoserver/rest/sldservice/updateStyle/topp:states");
				client.put(dssURL, "<LayerConfig><Style>" + styleName.replaceAll(":", "_") + "</Style></LayerConfig>");
			} else
				System.out.println("KO");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private boolean put(URL url, String content) throws IOException, ProtocolException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestMethod("PUT");

		OutputStreamWriter outReq = new OutputStreamWriter(con.getOutputStream());
		outReq.write(content);
		outReq.flush();
		outReq.close();
		System.out.println(con.getResponseCode());
		System.out.println(con.getResponseMessage());

		if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStreamReader is = new InputStreamReader(con.getInputStream());
			String response = readIs(is);
			is.close();
			System.out.println(response);
			return true;
		} else {
			System.out.println(con.getResponseCode());
			System.out.println(con.getResponseMessage());
			return false;
		}
	}
	
	private String readIs(InputStreamReader is) {

		char[] inCh = new char[1024];
		StringBuffer input = new StringBuffer();
		int r;
		try {
			while((r = is.read(inCh)) > 0) {
				input.append(inCh, 0, r);
			}
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return input.toString();
	}
}
