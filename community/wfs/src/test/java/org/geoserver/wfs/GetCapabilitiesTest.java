package org.geoserver.wfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import net.opengis.ows.v1_0_0.OWSFactory;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.WFSFactory;

import org.geoserver.util.ReaderUtils;
import org.geotools.xml.transform.TransformerBase;
import org.w3c.dom.Element;

public class GetCapabilitiesTest extends WFSTestSupport {

	public void testGetCapabilities() throws Exception {
		
		GetCapabilitiesType request = WFSFactory.eINSTANCE.createGetCapabilitiesType();
		request.setAcceptVersions( OWSFactory.eINSTANCE.createAcceptVersionsType() );
		request.getAcceptVersions().getVersion().add( "1.0.0" );
		
		TransformerBase tx = webFeatureService.getCapabilities( request );
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		tx.transform( null, outputStream );
		
		Element capsElement = ReaderUtils.parse( 
			new InputStreamReader( new ByteArrayInputStream( outputStream.toByteArray() ) )
		);
		assertEquals( "WFS_Capabilities", capsElement.getNodeName() );
	}
	
}
 