package org.geoserver.config.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServerFactory;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.impl.GeoServerImpl;
import org.geoserver.config.impl.ServiceInfoImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XStreamPersisterTest extends TestCase {

    GeoServerFactory factory;
    CatalogFactory cfactory;
    XStreamPersister persister;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        factory = new GeoServerImpl().getFactory();
        persister = new XStreamPersister();
    }
    
    public void testGlobal() throws Exception {
        GeoServerInfo g1 = factory.createGlobal();
        g1.setAdminPassword( "foo" );
        g1.setAdminUsername( "bar" );
        g1.setCharset( "ISO-8859-1" );
        
        ContactInfo contact = factory.createContact();
        g1.setContact( contact );
        contact.setAddress( "123" );
        contact.setAddressCity( "Victoria" );
        contact.setAddressCountry( "Canada" );
        contact.setAddressPostalCode( "V1T3T8");
        contact.setAddressState( "BC" );
        contact.setAddressType( "house" );
        contact.setContactEmail( "bob@acme.org" );
        contact.setContactFacsimile("+1 250 123 4567" );
        contact.setContactOrganization( "Acme" );
        contact.setContactPerson( "Bob" );
        contact.setContactPosition( "hacker" );
        contact.setContactVoice( "+1 250 765 4321" );
        
        g1.setLoggingLevel( "CRAZY_LOGGING" );
        g1.setLoggingLocation( "some/place/geoserver.log" );
        g1.setNumDecimals( 2 );
        g1.setOnlineResource( "http://acme.org" );
        g1.setProxyBaseUrl( "http://proxy.acme.org" );
        g1.setSchemaBaseUrl( "http://schemas.acme.org");
        g1.setStdOutLogging( true );
        g1.setTitle( "Acme's GeoServer" );
        g1.setUpdateSequence( 123 );
        g1.setVerbose( true );
        g1.setVerboseExceptions( true );
        
        g1.getMetadata().put( "one", new Integer(1) );
        g1.getMetadata().put( "two", new Double(2.2) );
        
        ByteArrayOutputStream out = out();
        
        persister.save( g1, out );
        
        GeoServerInfo g2 = persister.load(in(out),GeoServerInfo.class);
        assertEquals( g1, g2 );
        
        Document dom = dom( in( out ) );
        assertEquals( "global", dom.getDocumentElement().getNodeName() );
    }
    
    public void testGobalContactDefault() throws Exception {
        GeoServerInfo g1 = factory.createGlobal();
        ContactInfo contact = factory.createContact();
        g1.setContact( contact );
        
        ByteArrayOutputStream out = out();
        persister.save(g1, out);
        
        ByteArrayInputStream in = in( out );
        Document dom = dom( in );
        
        Element e = (Element) dom.getElementsByTagName( "contact" ).item(0);
        e.removeAttribute( "class" );
        in = in( dom );
        
        GeoServerInfo g2 = persister.load( in, GeoServerInfo.class );
        assertEquals( g1, g2 );
    }
   
    static class MyServiceInfo extends ServiceInfoImpl {
        
        String foo;
        
        String getFoo() {
            return foo;
        }
        
        void setFoo( String foo ) {
            this.foo = foo;
        }
        
        public boolean equals(Object obj) {
            if ( !( obj instanceof MyServiceInfo ) ) {
                return false;
            }
            
            MyServiceInfo other = (MyServiceInfo) obj;
            if ( foo == null ) {
                if ( other.foo != null ) {
                    return false;
                }
            }
            else {
                if ( !foo.equals( other.foo ) ) {
                    return false;
                }
            }
            
            return super.equals(other); 
        }
    }

    public void testService() throws Exception {
        MyServiceInfo s1 = new MyServiceInfo();
        s1.setAbstract( "my service abstract" );
        s1.setAccessConstraints( "no constraints" );
        s1.setCiteCompliant( true );
        s1.setEnabled( true );
        s1.setFees( "no fees" );
        s1.setFoo( "bar" );
        s1.setId( "id" );
        s1.setMaintainer( "Bob" );
        s1.setMetadataLink( factory.createMetadataLink() );
        s1.setName( "MS" );
        s1.setOnlineResource( "http://acme.org?service=myservice" );
        s1.setOutputStrategy("FAST");
        s1.setSchemaBaseURL( "http://schemas.acme.org/");
        s1.setTitle( "My Service" );
        s1.setVerbose(true);
        
        ByteArrayOutputStream out = out();
        persister.save( s1, out );
        
        MyServiceInfo s2 = persister.load( in( out ), MyServiceInfo.class );
        assertEquals( s1, s2 );
    }
    
    public void testServiceOmitGlobal() throws Exception {
        MyServiceInfo s1 = new MyServiceInfo();
        s1.setGeoServer( factory.createGlobal() );
        
        ByteArrayOutputStream out = out();
        persister.save( s1, out );
        
        MyServiceInfo s2 = persister.load( in( out ), MyServiceInfo.class );
        
        assertNull( s2.getGeoServer() );
        assertEquals( s1, s2 );
    }
    
    public void testServiceCustomAlias() throws Exception {
        XStreamPersister p = new XStreamPersister();
        p.getXStream().alias( "ms", MyServiceInfo.class );
        
        MyServiceInfo s1 = new MyServiceInfo();
        
        ByteArrayOutputStream out = out();
        p.save( s1, out );
        
        Document dom = dom( in( out ) ) ;
        assertEquals( "ms", dom.getDocumentElement().getNodeName() );
    }
    
    public void testDataStore() throws Exception {
        Catalog catalog = new CatalogImpl();
        CatalogFactory cFactory = catalog.getFactory();
        
        WorkspaceInfo ws = cFactory.createWorkspace();
        ws.setName( "foo" );
        
        DataStoreInfo ds1 = cFactory.createDataStore();
        ds1.setName( "bar" );
        ds1.setWorkspace( ws );
        
        ByteArrayOutputStream out = out();
        persister.save( ds1 , out );
        
        DataStoreInfo ds2 = persister.load( in( out ), DataStoreInfo.class );
        assertEquals( "bar", ds2.getName() );
        
        //TODO: reenable when resolving proxy commited
        //assertNotNull( ds2.getWorkspace() );
        //assertEquals( "foo", ds2.getWorkspace().getId() );
        
        Document dom = dom( in( out ) );
        assertEquals( "dataStore", dom.getDocumentElement().getNodeName() );
    }
    
    public void testCoverageStore() throws Exception {
        Catalog catalog = new CatalogImpl();
        CatalogFactory cFactory = catalog.getFactory();
        
        WorkspaceInfo ws = cFactory.createWorkspace();
        ws.setName( "foo" );
        
        CoverageStoreInfo cs1 = cFactory.createCoverageStore();
        cs1.setName( "bar" );
        cs1.setWorkspace( ws );
        
        ByteArrayOutputStream out = out();
        persister.save( cs1 , out );
        
        CoverageStoreInfo ds2 = persister.load( in( out ), CoverageStoreInfo.class );
        assertEquals( "bar", ds2.getName() );
        
        //TODO: reenable when resolving proxy commited
        //assertNotNull( ds2.getWorkspace() );
        //assertEquals( "foo", ds2.getWorkspace().getId() );
        
        Document dom = dom( in( out ) );
        assertEquals( "coverageStore", dom.getDocumentElement().getNodeName() );
    }
    
    public void testStyle() throws Exception {
        Catalog catalog = new CatalogImpl();
        CatalogFactory cFactory = catalog.getFactory();
        
        StyleInfo s1 = cFactory.createStyle();
        s1.setName( "foo" );
        s1.setFilename( "foo.sld" );
        
        ByteArrayOutputStream out = out();
        persister.save( s1, out );
        
        ByteArrayInputStream in = in( out );
        
        StyleInfo s2 = persister.load(in,StyleInfo.class);
        assertEquals( s1, s2 );
        
        Document dom = dom( in( out ) );
        assertEquals( "style", dom.getDocumentElement().getNodeName() );
    }
    
    public void testCatalog() throws Exception {
        Catalog catalog = new CatalogImpl();
        CatalogFactory cFactory = catalog.getFactory();
        
        WorkspaceInfo ws = cFactory.createWorkspace();
        ws.setName( "foo" );
        catalog.add( ws );
        
        NamespaceInfo ns = cFactory.createNamespace();
        ns.setPrefix( "acme" );
        ns.setURI( "http://acme.org" );
        catalog.add( ns );
        
        DataStoreInfo ds = cFactory.createDataStore();
        ds.setWorkspace( ws );
        ds.setName( "foo" );
        catalog.add( ds );
        
        CoverageStoreInfo cs = cFactory.createCoverageStore();
        cs.setWorkspace( ws );
        cs.setName( "bar" );
        catalog.add( cs );
        
        StyleInfo s = cFactory.createStyle();
        s.setName( "style" );
        s.setFilename( "style.sld" );
        catalog.add(s);
     
        ByteArrayOutputStream out = out();
        persister.save( catalog, out );
    }
    
    ByteArrayOutputStream out() {
        return new ByteArrayOutputStream();
    }
    
    ByteArrayInputStream in( ByteArrayOutputStream in ) {
        return new ByteArrayInputStream( in.toByteArray() );
    }
    
    ByteArrayInputStream in( Document dom ) throws Exception {
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.setOutputProperty( OutputKeys.INDENT, "yes" );
        
        ByteArrayOutputStream out = out();
        tx.transform( new DOMSource( dom ), new StreamResult( out ) );
        
        return in( out );
    }
    
    Document dom( InputStream in ) throws Exception {
        return 
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( in );
    }
    
    void print( InputStream in ) throws Exception {
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.setOutputProperty( OutputKeys.INDENT, "yes" );
        
        tx.transform( new StreamSource( in ), new StreamResult( System.out ) );
    }
}
