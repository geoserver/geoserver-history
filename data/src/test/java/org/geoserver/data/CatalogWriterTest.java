package org.geoserver.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import junit.framework.TestCase;

public class CatalogWriterTest extends TestCase {

	Document catalog;
	
	protected void setUp() throws Exception {
	
		CatalogWriter writer = new CatalogWriter();
		
		Map params = new HashMap();
		params.put( "p1", "v1" );
		params.put( "p2", "v2" );
		
		Map dataStores = new HashMap();
		dataStores.put( "ds1", params );
		dataStores.put( "ds2", params );
		
		writer.dataStores( dataStores );
		
		Map namespaces = new HashMap();
		namespaces.put( "nsp1", "ns1" );
		namespaces.put( "nsp2", "ns2" );
		namespaces.put( "", "ns1" );
		writer.namespaces( namespaces );
		
		Map styles = new HashMap();
		styles.put( "style1", "style1.sld" );
		styles.put( "style2", "style2.sld" );
		writer.styles( styles );
		
		File catalog = File.createTempFile( "catalog", "xml" );
		catalog.deleteOnExit();
		writer.write( catalog );
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware( false );
		factory.setValidating( false );
		
		this.catalog = factory.newDocumentBuilder().parse( catalog );
		
	}
	
	public void testWrite() throws Exception {
		assertEquals( "catalog", catalog.getDocumentElement().getNodeName() );
		assertEquals( 1, catalog.getElementsByTagName( "datastores" ).getLength() );
		assertEquals( 1, catalog.getElementsByTagName( "namespaces" ).getLength() );
		assertEquals( 1, catalog.getElementsByTagName( "styles" ).getLength() );
	}
	
	public void testWriteDataStores() throws Exception {
		assertEquals( 2, catalog.getElementsByTagName( "datastore" ).getLength() );
		
		NodeList datastoreNodes = catalog.getElementsByTagName( "datastore" );
		Element ds1 = (Element) datastoreNodes.item( 0 );
		Element ds2 = (Element) datastoreNodes.item( 1 );
		
		assertTrue( ds1.hasAttribute( "id" ) );
		assertTrue( 
			"ds1".equals( ds1.getAttribute( "id" ) ) || "ds2".equals( ds1.getAttribute( "id" ) ) 
		) ;
		if ( "ds2".equals( ds1.getAttribute( "id" ) ) ) {
			ds1 = (Element) datastoreNodes.item( 1 );
			ds2 = (Element) datastoreNodes.item( 0 );
		}
		
		assertEquals( "ds1", ds1.getAttribute( "id" ) );
		assertEquals( 1, ds1.getElementsByTagName( "connectionParams" ).getLength() );
		assertEquals( 2, ds1.getElementsByTagName( "parameter" ).getLength() );
		
		assertEquals( "ds2", ds2.getAttribute( "id" ) );
		assertEquals( 1, ds2.getElementsByTagName( "connectionParams" ).getLength() );
		assertEquals( 2, ds2.getElementsByTagName( "parameter" ).getLength() );
		
	}
	
	public void testWriteNamespaces() throws Exception {
		assertEquals( 2, catalog.getElementsByTagName( "namespace" ).getLength() );
		
		NodeList namespacesNodes = catalog.getElementsByTagName( "namespace" );
		
		Element ns1 = (Element) namespacesNodes.item( 0 );
		Element ns2 = (Element) namespacesNodes.item( 1 );
		
		assertTrue( ns1.hasAttribute( "uri" ) );
		assertTrue( 
			"ns1".equals( ns1.getAttribute( "uri" ) ) || "ns2".equals( ns1.getAttribute( "uri" ) ) 
		);
		
		if ( "ns1".equals( ns1.getAttribute( "uri" ) ) ) {
			assertEquals( "nsp1", ns1.getAttribute( "prefix" ) );
			assertEquals( "true", ns1.getAttribute( "default" ) );
		
			assertEquals( "nsp2", ns2.getAttribute( "prefix") );
			assertEquals( "ns2", ns2.getAttribute( "uri") );
		}
		else {
			assertEquals( "nsp1", ns2.getAttribute( "prefix" ) );
			assertEquals( "true", ns2.getAttribute( "default" ) );
		
			assertEquals( "nsp2", ns1.getAttribute( "prefix") );
			assertEquals( "ns2", ns1.getAttribute( "uri") );
		}
	}
}

