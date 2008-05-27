package org.geoserver.geosync;

import java.io.File;
import java.net.URI;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GeoSyncControllerTest extends GeoServerTestSupport {

    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        
        String insert = 
            "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" "
              + "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" "
              + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
              + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
              + "xmlns:gml=\"http://www.opengis.net/gml\"> "
              + "<wfs:Insert srsName=\"EPSG:4326\"> " 
              +   "<cgf:Points>"
                  + "<cgf:pointProperty>"
                      + "<gml:Point>" 
                        + "<gml:pos>1 1</gml:pos>" 
                      + "</gml:Point>"
                  + "</cgf:pointProperty>" 
                  + "<cgf:id>t0001</cgf:id>" 
              + "</cgf:Points>"
              + "</wfs:Insert>" 
              + "<wfs:Insert srsName=\"EPSG:4326\"> " 
              +   "<cgf:Points>"
                  + "<cgf:pointProperty>"
                      + "<gml:Point>" 
                        + "<gml:pos>2 2</gml:pos>" 
                      + "</gml:Point>"
                  + "</cgf:pointProperty>" 
                  + "<cgf:id>t0002</cgf:id>" 
              + "</cgf:Points>"
              + "</wfs:Insert>" 
              + "<wfs:Insert srsName=\"EPSG:4326\"> " 
              +   "<cgf:Points>"
                  + "<cgf:pointProperty>"
                      + "<gml:Point>" 
                        + "<gml:pos>3 3</gml:pos>" 
                      + "</gml:Point>"
                  + "</cgf:pointProperty>" 
                  + "<cgf:id>t0003</cgf:id>" 
              + "</cgf:Points>"
              + "</wfs:Insert>"
          + "</wfs:Transaction>";   
        
        postAsDOM( "wfs", insert );
    }
    
    public void testEmptyFeed() throws Exception {
        Document d = getAsDOM( "history/Points" );
        
        assertEquals( "feed", d.getDocumentElement().getNodeName() );
        
        Element link = getFirstElementByTagName( d, "link" );
        assertNotNull( link );
        
        String href = link.getAttribute( "href" );
        assertTrue( href.endsWith( "Points/opensearch.xml" ) );
        
        assertEquals( "search", link.getAttribute( "rel" ) );
        assertEquals( "application/opensearchdescription+xml", link.getAttribute( "type" ) );
    }

    public void testSearchTemplate() throws Exception {
        Document d = getAsDOM( "history/Points/opensearch.xml" );
        assertEquals( "OpenSearchDescription", d.getDocumentElement().getNodeName() );
        
        Element url = getFirstElementByTagName(d, "Url");
        assertNotNull( url );
    }
    
    public void testFeed() throws Exception {
        String layer = MockData.POINTS.getLocalPart();
        Document d = getAsDOM( "history/" + layer + "?bbox=-180,-90,180,90" );
        print ( d );
        
        assertEquals( "feed", d.getDocumentElement().getNodeName() );
        assertEquals( 3, d.getElementsByTagName( "entry" ).getLength() );
    }
    
    public void testFilterBBOX() throws Exception {
        
        Document d = getAsDOM( "history/Points?bbox=-180,-90,180,90" );
        print( d );
        
        assertEquals( "feed", d.getDocumentElement().getNodeName() );
        assertEquals( 3, d.getElementsByTagName( "entry" ).getLength() );
        
        d = getAsDOM( "history/Points?bbox=2,2,180,90" );
        
        assertEquals( "feed", d.getDocumentElement().getNodeName() );
        assertEquals( 2, d.getElementsByTagName( "entry" ).getLength() );
    
        d = getAsDOM( "history/Points?bbox=10,10,180,90" );
        
        assertEquals( "feed", d.getDocumentElement().getNodeName() );
        assertEquals( 0, d.getElementsByTagName( "entry" ).getLength() );
    }
    
    public void testFilterIndex() throws Exception {
        Document d = getAsDOM( "history/Points?startIndex=1" );
        
        assertEquals( "feed", d.getDocumentElement().getNodeName() );
        assertEquals( 3, d.getElementsByTagName( "entry" ).getLength() );
        
        d = getAsDOM( "history/Points?startIndex=2" );
        
        assertEquals( "feed", d.getDocumentElement().getNodeName() );
        assertEquals( 2, d.getElementsByTagName( "entry" ).getLength() );
    
        d = getAsDOM( "history/Points?startIndex=3" );
        
        assertEquals( "feed", d.getDocumentElement().getNodeName() );
        assertEquals( 1, d.getElementsByTagName( "entry" ).getLength() );
    }
    
    public void testFilterDate() throws Exception {
        Document d = getAsDOM( "history/Points?bbox=-180,-90,180,90" );
        NodeList nodes = d.getElementsByTagName( "updated" );
        
        assertEquals(3, nodes.getLength() );
        Element e = (Element) nodes.item( 0 );
        e.getFirstChild().setTextContent( "2008-03-19T12:00:00Z" );
        
        e = (Element) nodes.item( 1 );
        e.getFirstChild().setTextContent( "2008-03-20T12:00:00Z" );
        
        e = (Element) nodes.item( 2 );
        e.getFirstChild().setTextContent( "2008-03-21T12:00:00Z" );
        
        File data = getTestData().getDataDirectoryRoot();
        File feed = new File( new File( data, "geosync"), "Points-history.xml" );
        
        assertTrue( feed.exists() );
        TransformerFactory.newInstance().newTransformer()
            .transform( new DOMSource( d ), new StreamResult( feed ) );
    
        d = getAsDOM( "history/Points?dtstart=2008-03-19T12:00:00Z" );
        assertEquals( 2, d.getElementsByTagName( "entry").getLength() );
        
        d = getAsDOM( "history/Points?dtstart=2008-03-20T12:00:00Z" );
        assertEquals( 1, d.getElementsByTagName( "entry").getLength() );
        
        d = getAsDOM( "history/Points?dtstart=2008-03-20T11:00:00Z" );
        assertEquals( 2, d.getElementsByTagName( "entry").getLength() );

        d = getAsDOM( "history/Points?dtstart=2008-03-20T11:00:00Z&dtend=2008-03-20T12:39:00Z" );
        assertEquals( 1, d.getElementsByTagName( "entry").getLength() );
    }
}
