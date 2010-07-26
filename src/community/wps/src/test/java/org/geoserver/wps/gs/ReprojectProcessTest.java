package org.geoserver.wps.gs;

import java.util.Arrays;

import org.geoserver.data.test.MockData;
import org.geoserver.wps.WPSTestSupport;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.w3c.dom.Document;

public class ReprojectProcessTest extends WPSTestSupport {

	public void testForce() throws Exception {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">\n" + 
				"  <ows:Identifier>gs:Reproject</ows:Identifier>\n" + 
				"  <wps:DataInputs>\n" + 
				"    <wps:Input>\n" + 
				"      <ows:Identifier>features</ows:Identifier>\n" + 
				"      <wps:Reference mimeType=\"text/xml; subtype=wfs-collection/1.0\" xlink:href=\"http://geoserver/wfs\" method=\"POST\">\n" + 
				"        <wps:Body>\n" + 
				"          <wfs:GetFeature service=\"WFS\" version=\"1.0.0\" outputFormat=\"GML2\">\n" + 
				"            <wfs:Query typeName=\"" + getLayerId(MockData.BASIC_POLYGONS) + "\"/>\n" + 
				"          </wfs:GetFeature>\n" + 
				"        </wps:Body>\n" + 
				"      </wps:Reference>\n" + 
				"    </wps:Input>\n" + 
				"    <wps:Input>\n" + 
				"      <ows:Identifier>forcedCRS</ows:Identifier>\n" + 
				"      <wps:Data>\n" + 
				"        <wps:LiteralData>EPSG:4269</wps:LiteralData>\n" + 
				"      </wps:Data>\n" + 
				"    </wps:Input>\n" + 
				"  </wps:DataInputs>\n" + 
				"  <wps:ResponseForm>\n" + 
				"    <wps:RawDataOutput mimeType=\"text/xml; subtype=wfs-collection/1.0\">\n" + 
				"      <ows:Identifier>result</ows:Identifier>\n" + 
				"    </wps:RawDataOutput>\n" + 
				"  </wps:ResponseForm>\n" + 
				"</wps:Execute>";
		
		Document response = postAsDOM(root(), xml);
		
		// TODO: actually check the results, atm they are broken due to http://jira.codehaus.org/browse/GEOS-4072
		print(response);
	}
	
	public void testReproject() throws Exception {
		// note, we use 3395 instead of 900913 because it's more accurate, the referencing subsystem
		// with assertions enabled will flip if we use the latter due to small reprojection errors
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">\n" + 
				"  <ows:Identifier>gs:Reproject</ows:Identifier>\n" + 
				"  <wps:DataInputs>\n" + 
				"    <wps:Input>\n" + 
				"      <ows:Identifier>features</ows:Identifier>\n" + 
				"      <wps:Reference mimeType=\"text/xml; subtype=wfs-collection/1.0\" xlink:href=\"http://geoserver/wfs\" method=\"POST\">\n" + 
				"        <wps:Body>\n" + 
				"          <wfs:GetFeature service=\"WFS\" version=\"1.0.0\" outputFormat=\"GML2\">\n" + 
				"            <wfs:Query typeName=\"" + getLayerId(MockData.BASIC_POLYGONS) + "\"/>\n" + 
				"          </wfs:GetFeature>\n" + 
				"        </wps:Body>\n" + 
				"      </wps:Reference>\n" + 
				"    </wps:Input>\n" + 
				"    <wps:Input>\n" + 
				"      <ows:Identifier>targetCRS</ows:Identifier>\n" + 
				"      <wps:Data>\n" + 
				"        <wps:LiteralData>EPSG:3395</wps:LiteralData>\n" + 
				"      </wps:Data>\n" + 
				"    </wps:Input>\n" + 
				"  </wps:DataInputs>\n" + 
				"  <wps:ResponseForm>\n" + 
				"    <wps:RawDataOutput mimeType=\"text/xml; subtype=wfs-collection/1.1\">\n" + 
				"      <ows:Identifier>result</ows:Identifier>\n" + 
				"    </wps:RawDataOutput>\n" + 
				"  </wps:ResponseForm>\n" + 
				"</wps:Execute>";
		
		Document response = postAsDOM(root(), xml);
		
		// TODO: actually check the results, atm they are broken due to http://jira.codehaus.org/browse/GEOS-4072
		print(response);
	}
	
}
