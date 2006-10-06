package org.geoserver.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import junit.framework.TestCase;

public class CatalogReaderTest extends TestCase {

	
	CatalogReader reader;
	
	public void setUp() throws Exception {
		
		File catalog = File.createTempFile( "catalog", "xml" );
		catalog.deleteOnExit();
		
		BufferedWriter writer = new BufferedWriter( new FileWriter( catalog ) );
		writer.write( "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" );
		writer.write( "<catalog>" );
		
		writer.write(   "<datastores>" );
		writer.write(   	"<datastore id=\"ds1\" namespace=\"nsp1\" enabled=\"true\">" );
		writer.write(   		"<connectionParams>" );
		writer.write(   			"<parameter value=\"v1\" name=\"p1\"/>" );
		writer.write(   			"<parameter value=\"v2\" name=\"p2\"/>" );
		writer.write(   		"</connectionParams>" );
		writer.write(   	"</datastore>" );
		writer.write(   	"<datastore id=\"ds2\" namespace=\"nsp2\" enabled=\"true\">" );
		writer.write(   		"<connectionParams>" );
		writer.write(   			"<parameter value=\"v1\" name=\"p1\"/>" );
		writer.write(   			"<parameter value=\"v2\" name=\"p2\"/>" );
		writer.write(   		"</connectionParams>" );
		writer.write(   	"</datastore>" );
		writer.write(   "</datastores>" );
		
		writer.write(   "<namespaces>" );
		writer.write(   	"<namespace uri=\"ns1\" prefix=\"nsp1\" default=\"true\"/>" );
		writer.write(   	"<namespace uri=\"ns2\" prefix=\"nsp2\" />" );
		writer.write(   "</namespaces>" );
		
		writer.write(   "<styles>" );
		writer.write(   	"<style filename=\"style1.sld\" id=\"style1\" />" );
		writer.write(   	"<style filename=\"style2.sld\" id=\"style2\" />" );
		writer.write(   "</styles>" );
		
		writer.write( "</catalog>" );
		writer.flush();
		writer.close();
		
		reader = new CatalogReader();
		reader.read( catalog );
	}
	
	public void testReadDataStores() throws Exception {
		Map dataStores = reader.dataStores();
		assertNotNull( dataStores );
		
		assertTrue( dataStores.containsKey( "ds1" ) );
		assertTrue( dataStores.containsKey( "ds2" ) );
	
		Map params = (Map) dataStores.get( "ds1" );
		assertTrue( params.containsKey( "p1" ) );
		assertTrue( params.containsKey( "p2" ) );
		
		assertEquals( "v1", params.get( "p1" ) );
		assertEquals( "v2", params.get( "p2" ) );
	}
	
	public void testReadNamespaces() throws Exception {
		Map namespaces = reader.namespaces();
		assertNotNull( namespaces );
		
		assertTrue( namespaces.containsKey( "nsp1" ) );
		assertTrue( namespaces.containsKey( "nsp2" ) );
		assertTrue( namespaces.containsKey( "" ) );
		
		assertEquals( "ns1", namespaces.get( "nsp1") );
		assertEquals( "ns2", namespaces.get( "nsp2") );
		assertEquals( "ns1", namespaces.get( "" ) );
	}
	
	public void testReadStyles() throws Exception {
		Map styles = reader.styles();
		assertNotNull( styles );
		
		assertTrue( styles.containsKey( "style1" ) );
		assertTrue( styles.containsKey( "style2" ) );
		
		assertEquals( "style1.sld", styles.get( "style1") );
		assertEquals( "style2.sld", styles.get( "style2") );
	}
}
