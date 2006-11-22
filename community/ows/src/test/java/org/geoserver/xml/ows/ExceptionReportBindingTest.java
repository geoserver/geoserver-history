package org.geoserver.xml.ows;

import java.util.logging.Level;

import net.opengis.ows.v1_0_0.ExceptionReportType;
import net.opengis.ows.v1_0_0.ExceptionType;
import net.opengis.ows.v1_0_0.OWSFactory;

import org.geoserver.util.ErrorHandler;
import org.geoserver.util.ReaderUtils;
import org.geoserver.xml.ows.v1_0_0.OWS;
import org.geoserver.xml.ows.v1_0_0.OWSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.test.XMLTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ExceptionReportBindingTest extends XMLTestSupport {

	protected Configuration createConfiguration() {
		return new OWSConfiguration();
	}

	public void testEncode() throws Exception {
		ExceptionType e0 = OWSFactory.eINSTANCE.createExceptionType();
		e0.setExceptionCode( "code0" );
		e0.setLocator( "locator0" );
		e0.getExceptionText().add( "text0" );
		
		ExceptionType e1 = OWSFactory.eINSTANCE.createExceptionType();
		e1.setExceptionCode( "code1" );
		e1.setLocator( "locator1" );
		e1.getExceptionText().add( "text1" );
		
		ExceptionReportType r = OWSFactory.eINSTANCE.createExceptionReportType();
		r.setVersion( "1.0.0" );
		r.getException().add( e0 );
		r.getException().add( e1 );
		
		Document dom = encode( r, OWS.EXCEPTIONREPORT );
		assertEquals( "ows:ExceptionReport", dom.getDocumentElement().getNodeName() );
		assertEquals( 2, dom.getElementsByTagName( "ows:Exception" ).getLength() );
		
		for ( int i = 0; i < dom.getElementsByTagName( "ows:Exception" ).getLength(); i++ ) {
			Element exception = (Element) dom.getElementsByTagName( "ows:Exception" ).item( i );
			assertEquals( "code" + i, exception.getAttribute( "exceptionCode") );
			assertEquals( "locator" + i, exception.getAttribute( "locator") );
			
			NodeList text = exception.getElementsByTagName( "ows:ExceptionText" );
			assertEquals( 1, text.getLength() );
			assertEquals( "text" + i, text.item( 0 ).getFirstChild().getNodeValue() );
		}
		
		Configuration configuration = createConfiguration();
		String schemaLocation = configuration.getSchemaLocationResolver().resolveSchemaLocation( 
			null, OWS.NAMESPACE, "owsExceptionReport.xsd"
		);		
		
		//calidate
		ErrorHandler handler = new ErrorHandler( logger, Level.WARNING );
		ReaderUtils.validate( dom, handler, OWS.NAMESPACE, schemaLocation );
		
		assertTrue( handler.errors.isEmpty() );
		
	}
}
