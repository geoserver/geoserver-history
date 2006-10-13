package org.geoserver.wfs.v1_1_0;

import net.opengis.ows.v1_0_0.OWSFactory;
//import net.opengis.wfs.v1_1_0.GetCapabilitiesType;
//import net.opengis.wfs.v1_1_0.WFSFactory;

import org.geoserver.ows.Service;
import org.geoserver.wfs.WFSCapsTransformer;
import org.geoserver.wfs.WFSTestSupport;
import org.geotools.xml.transform.TransformerBase;

public class VersionNegotiationTest extends WFSTestSupport {

	public void test() {
		
	}
//	GetCapabilities getCaps;
//	WFSFactory factory;
//	OWSFactory owsFactory;
//	
//	protected void setUp() throws Exception {
//		super.setUp();
//		
//		Service wfs1_0 = new Service( "WFS", wfs, "1.0.0" );
//		Service wfs1_1 = new Service( "WFS", wfs, "1.1.0" );
//		
//		context.getBeanFactory().registerSingleton( "wfs1.0", wfs1_0 );
//		context.getBeanFactory().registerSingleton( "wfs1.1", wfs1_1 );
//	
//		getCaps = new GetCapabilities( wfs, catalog );
//		getCaps.setApplicationContext( context );
//		
//		factory = WFSFactory.eINSTANCE;
//		owsFactory = OWSFactory.eINSTANCE;
//		
//	}
//	
//	
//	public void test0() throws Exception {
//		//test when provided and accepted match up
//		GetCapabilitiesType request = factory.createGetCapabilitiesType();
//		request.setAcceptVersions( owsFactory.createAcceptVersionsType() );
//		request.getAcceptVersions().getVersion().add( "1.0.0");
//		request.getAcceptVersions().getVersion().add( "1.1.0");
//	
//		TransformerBase tx = getCaps.getCapabilities( request );
//		assertTrue( tx instanceof WFSCapabilitiesTransformer );
//	}
//	
//	public void test1() throws Exception {
//		//test accepted only 1.0
//		GetCapabilitiesType request = factory.createGetCapabilitiesType();
//		request.setAcceptVersions( owsFactory.createAcceptVersionsType() );
//		request.getAcceptVersions().getVersion().add( "1.0.0");
//		
//		TransformerBase tx = getCaps.getCapabilities( request );
//		assertTrue( tx instanceof WFSCapsTransformer );
//	}
//	
//	public void test2() throws Exception {
//		//test accepted only 1.1
//		GetCapabilitiesType request = factory.createGetCapabilitiesType();
//		request.setAcceptVersions( owsFactory.createAcceptVersionsType() );
//		request.getAcceptVersions().getVersion().add( "1.1.0");
//		
//		TransformerBase tx = getCaps.getCapabilities( request );
//		assertTrue( tx instanceof WFSCapabilitiesTransformer );
//	}
//	
//	public void test3() throws Exception {
//		// test accepted = 1.0, provided = 1.1
//		context.getBeanFactory().destroySingletons();
//		context.getBeanFactory().registerSingleton( "wfs1.1", new Service( "WFS", wfs, "1.1.0" ) );
//		
//		GetCapabilitiesType request = factory.createGetCapabilitiesType();
//		request.setAcceptVersions( owsFactory.createAcceptVersionsType() );
//		request.getAcceptVersions().getVersion().add( "1.0.0");
//		
//		TransformerBase tx = getCaps.getCapabilities( request );
//		assertTrue( tx instanceof WFSCapabilitiesTransformer );
//	}
//	
//	public void test4() throws Exception {
//		//test accepted = 1.1, provided = 1.0
//		context.getBeanFactory().destroySingletons();
//		context.getBeanFactory().registerSingleton( "wfs1.1", new Service( "WFS", wfs, "1.0.0" ) );
//		
//		GetCapabilitiesType request = factory.createGetCapabilitiesType();
//		request.setAcceptVersions( owsFactory.createAcceptVersionsType() );
//		request.getAcceptVersions().getVersion().add( "1.1.0");
//		
//		TransformerBase tx = getCaps.getCapabilities( request );
//		assertTrue( tx instanceof WFSCapsTransformer );
//	}
//	
//	public void test5() throws Exception {
//		//test accepted = 0.0.0
//		
//		GetCapabilitiesType request = factory.createGetCapabilitiesType();
//		request.setAcceptVersions( owsFactory.createAcceptVersionsType() );
//		request.getAcceptVersions().getVersion().add( "0.0.0");
//		
//		TransformerBase tx = getCaps.getCapabilities( request );
//		assertTrue( tx instanceof WFSCapsTransformer );
//	}
//	
//	public void test6() throws Exception {
//		//test accepted = 1.1.1
//		
//		GetCapabilitiesType request = factory.createGetCapabilitiesType();
//		request.setAcceptVersions( owsFactory.createAcceptVersionsType() );
//		request.getAcceptVersions().getVersion().add( "1.1.1");
//		
//		TransformerBase tx = getCaps.getCapabilities( request );
//		assertTrue( tx instanceof WFSCapabilitiesTransformer );
//	}
//	
//	public void test7() throws Exception {
//		//test accepted = 1.0.5
//		GetCapabilitiesType request = factory.createGetCapabilitiesType();
//		request.setAcceptVersions( owsFactory.createAcceptVersionsType() );
//		request.getAcceptVersions().getVersion().add( "1.0.5");
//		
//		TransformerBase tx = getCaps.getCapabilities( request );
//		assertTrue( tx instanceof WFSCapsTransformer );
//	}
	
}
