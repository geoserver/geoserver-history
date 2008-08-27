package org.geoserver.config.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.geoserver.catalog.util.ReaderUtils;
import org.geoserver.ows.util.XmlCharsetDetector;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Reads the GeoServer services.xml file.
 * <p>
 * Usage:
 * 
 * <pre>
 *         <code>
 * File services = new File(&quot;.../services.xml&quot;);
 * LegacyServicesReader reader = new LegacyServicesReader();
 * reader.read(services);
 * Map global = reader.global();
 * Map wfs = reader.wfs();
 * </code>
 * </pre>
 * 
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * 
 */
public class LegacyServicesReader {

    /**
     * Root serverConfiguration element.
     */
    Element serverConfiguration;

    /**
     * Parses the servivces.xml file into a DOM.
     * <p>
     * This method *must* be called before any other methods.
     * </p>
     * 
     * @param file
     *                The services.xml file.
     * 
     * @throws IOException
     *                 In event of a parser error.
     */
    public void read(File file) throws IOException {
        Reader reader = XmlCharsetDetector.getCharsetAwareReader(new FileInputStream(file));

        try {
            serverConfiguration = ReaderUtils.parse(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * Reads the "global" section of the configuration.
     * <p>
     * The configuration key / value pairs are returned an a map.
     * </p>
     * 
     */
    public Map<String,Object> global() throws Exception {
        Element globalElement = ReaderUtils.getChildElement(
                serverConfiguration, "global");
        HashMap global = new HashMap();

        value("verbose", globalElement, global, Boolean.class);
        value("verboseExceptions", globalElement, global, Boolean.class);
        value("charSet", globalElement, global, String.class);
        text("updateSequence", globalElement, global, Integer.class, false, 0);
        text("log4jConfigFile", globalElement, global, String.class, false, "DEFAULT_LOGGING.properties");
        text("logLocation", globalElement, global, String.class, false, "logs/geoserver.log");
        value("suppressStdOutLogging", globalElement, global, Boolean.class, false, Boolean.FALSE);
        value("maxFeatures", globalElement, global, Integer.class );
        value("numDecimals", globalElement, global, Integer.class );
        text("onlineResource", globalElement, global, String.class, false, "http://geoserver.org");
        text("ProxyBaseUrl", globalElement, global, String.class );
        
        value("JaiMemoryCapacity", globalElement, global, Double.class);
        value("JaiMemoryThreshold", globalElement, global, Double.class);
        value("JaiTileThreads", globalElement, global, Integer.class);
        value("JaiTilePriority", globalElement, global, Integer.class);
        value("JaiRecycling", globalElement, global, Boolean.class);
        value("ImageIOCache", globalElement, global, Boolean.class);
        value("JaiJPEGNative", globalElement, global, Boolean.class);
        value("JaiPNGNative", globalElement, global, Boolean.class);
        
        return global;
    }

    public Map<String,Object> contact() throws Exception {
        Element globalElement = ReaderUtils.getChildElement(
                serverConfiguration, "global");
        
        HashMap<String,Object> contact = new HashMap();
        
        Element contactElement = ReaderUtils.getChildElement(globalElement, "ContactInformation");
        if ( contactElement != null ) {
            Element personPrimaryElement = ReaderUtils.getChildElement(contactElement, "ContactPersonPrimary" );
            if ( personPrimaryElement != null ) {
                text( "ContactPerson", personPrimaryElement, contact, String.class );
                text( "ContactOrganization", personPrimaryElement, contact, String.class );
            }
            text( "ContactPosition", contactElement, contact, String.class );
            
            Element addressElement = ReaderUtils.getChildElement( contactElement, "ContactAddress");
            if ( addressElement != null ) {
                text( "Address", addressElement, contact, String.class );
                text( "AddressType", addressElement, contact, String.class );
                text( "City", addressElement, contact, String.class );
                text( "StateOrProvince", addressElement, contact, String.class );
                text( "PostCode", addressElement, contact, String.class );
                text( "Country", addressElement, contact, String.class );
            }
            text( "ContactVoiceTelephone", contactElement, contact, String.class );
            text( "ContactFacsimileTelephone", contactElement, contact, String.class );
            text( "ContactElectronicMailAddress", contactElement, contact, String.class );
        }
        
        return contact;
    }
    
    public Map<String,Object> wfs() throws Exception {
        Element servicesElement = ReaderUtils.getChildElement(serverConfiguration, "services", true);
        Element wfsElement = service( servicesElement, "WFS" );
        
        Map<String,Object> wfs = readService( wfsElement );
        value( "serviceLevel", wfsElement, wfs, Integer.class );
        value( "srsXmlStyle", wfsElement, wfs, Boolean.class, false, Boolean.TRUE );
        value( "featureBounding", wfsElement, wfs, Boolean.class, false, Boolean.FALSE );
        
        return wfs;
    }
    
    public Map<String,Object> wms() throws Exception {
        Element servicesElement = ReaderUtils.getChildElement(serverConfiguration, "services", true);
        Element wmsElement = service( servicesElement, "WMS" );
        
        Map<String,Object> wms = readService( wmsElement );
        text( "globalWatermarking", wmsElement, wms, Boolean.class,false, Boolean.FALSE );
        text( "globalWatermarkingURL", wmsElement, wms, String.class, false, null );
        text( "globalWatermarkingTransparency", wmsElement, wms, Integer.class, false, 0 );
        text( "globalWatermarkingPosition", wmsElement, wms, Integer.class, false, 8 );
        text( "allowInterpolation", wmsElement, wms, String.class, false, "Nearest" );
        text( "svgRenderer", wmsElement, wms, String.class, false, "Batik" );
        text( "svgAntiAlias", wmsElement, wms, Boolean.class, false, Boolean.TRUE );
        text( "capabilitiesCrsList", wmsElement, wms, String.class, false, null);
        
        ArrayList<Map> baseMaps = new ArrayList<Map>();
        Element baseMapGroupsElement = ReaderUtils.getChildElement(wmsElement, "BaseMapGroups");
        if ( baseMapGroupsElement != null ) {
            Element[] baseMapGroupElements = ReaderUtils.getChildElements(baseMapGroupsElement, "BaseMapGroup" );
            for ( int i = 0; i < baseMapGroupElements.length; i++ ) {
                Element baseMapGroupElement = baseMapGroupElements[i];
                HashMap<String, Object> baseMap = new HashMap<String, Object>();
                baseMap.put( "baseMapTitle", ReaderUtils.getAttribute(baseMapGroupElement, "baseMapTitle", true));
                
                baseMap.put( "baseMapLayers", 
                    Arrays.asList( ReaderUtils.getChildText( baseMapGroupElement, "baseMapLayers" ).split( ",") ) );
                
                String baseMapStyles = ReaderUtils.getChildText( baseMapGroupElement, "baseMapStyles" );
                if ( baseMapStyles != null && !"".equals( baseMapStyles ) ) {
                    baseMap.put( "baseMapStyles", 
                            Arrays.asList( baseMapStyles.split( ",") ) );
                    baseMap.put( "rawBaseMapStyles", baseMapStyles );
                }
                else {
                    baseMap.put( "baseMapStyles", Collections.EMPTY_LIST );
                    baseMap.put( "rawBaseMapStyles", "" );
                }
                  
                Element baseMapEnvelopeElement = ReaderUtils.getChildElement( baseMapGroupElement, "baseMapEnvelope");
                if ( baseMapEnvelopeElement != null ) {
                    Element[] posElements = ReaderUtils.getChildElements( baseMapEnvelopeElement, "pos");
                    double x1 = Double.parseDouble( posElements[0].getFirstChild().getNodeValue().split( " " )[0] );
                    double y1 = Double.parseDouble( posElements[0].getFirstChild().getNodeValue().split( " " )[1] );
                    double x2 = Double.parseDouble( posElements[1].getFirstChild().getNodeValue().split( " " )[0] );
                    double y2 = Double.parseDouble( posElements[1].getFirstChild().getNodeValue().split( " " )[1] );
                    
                    String srs = ReaderUtils.getAttribute( baseMapEnvelopeElement, "srsName", false );
                    CoordinateReferenceSystem crs = srs != null ? CRS.decode( srs ) : null;
                    baseMap.put( "baseMapEnvelope", new ReferencedEnvelope( x1, x2, y1, y2, crs ) );
                }
                
                baseMaps.add( baseMap );
            }
        }
        wms.put( "BaseMapGroups", baseMaps );
        
        return wms;
    }
    
    public Map<String,Object> wcs() throws Exception {
        Element servicesElement = ReaderUtils.getChildElement(serverConfiguration, "services", true);
        Element wcsElement = service( servicesElement, "WCS" );
        
        Map<String,Object> wcs = readService( wcsElement );
        
        return wcs;
    }
    
    Map<String,Object> readService( Element serviceElement ) throws Exception {
        HashMap<String,Object> service = new HashMap<String,Object>();
        service.put( "enabled", ReaderUtils.getBooleanAttribute(serviceElement, "enabled", false, true ) );
        
        text( "name", serviceElement, service, String.class );
        text( "title", serviceElement, service, String.class );
        text( "abstract", serviceElement, service, String.class );
        
        Element mlElement = ReaderUtils.getChildElement(serviceElement, "metadataLink" );
        if ( mlElement != null ) {
            HashMap metadataLink = new HashMap();
            metadataLink.put( "about", ReaderUtils.getAttribute(mlElement, "about", false) );
            metadataLink.put( "type", ReaderUtils.getAttribute(mlElement, "type", false) );
            metadataLink.put( "metadataType", ReaderUtils.getAttribute(mlElement, "metadataType", false) );
            service.put( "metadataLink", metadataLink );
        }
        
        Element keywordsElement = ReaderUtils.getChildElement( serviceElement, "keywords" );
        Element[] keywordElements = ReaderUtils.getChildElements( keywordsElement, "keyword" );
        ArrayList keywords = new ArrayList();
        if(keywordElements != null) {
            for ( int i = 0; i < keywordElements.length; i++ ) {
                keywords.add( keywordElements[i].getFirstChild().getTextContent() );
            }
        }
        service.put( "keywords", keywords );
        
        text( "onlineResource", serviceElement, service, String.class );
        text( "fees", serviceElement, service, String.class );
        text( "accessConstraints", serviceElement, service, String.class );
        text( "SchemaBaseUrl", serviceElement, service, String.class, false, "http://schemas.opengis.net" );
        value( "srsXmlStyle", serviceElement, service, Boolean.class );
        value( "serviceLevel", serviceElement, service, Integer.class );
        text( "citeConformanceHacks", serviceElement, service, Boolean.class, false, Boolean.FALSE );
        text("maintainer", serviceElement, service, String.class );
        
        return service;    
    }
    
    Element service( Element servicesElement, String id ) throws Exception {
        Element[] serviceElements = ReaderUtils.getChildElements(servicesElement, "service");
        for ( int i = 0; i < serviceElements.length; i++ ) {
            String serviceId = ReaderUtils.getAttribute(serviceElements[i], "type", false );
            if ( id.equals( serviceId ) ) {
                return serviceElements[i];
            }
        }
        
        throw new Exception( "No service element: " + id );
    }
    
    void value(String parameter, Element element, Map map, Class clazz)
            throws Exception {
        value( parameter, element, map, clazz, false, null );
    }
    
    <T> void value(String parameter, Element element, Map map, Class<T> clazz, boolean man, T def ) 
        throws Exception {
        
        Element valueElement = ReaderUtils.getChildElement(element, parameter);
        if ( valueElement == null ){
            if ( man ) {
                throw new RuntimeException( "No such element: " + parameter ); 
            }
            else {
                map.put( parameter, def );
                return;
            }
        }
        

        Object value = null;
        if (Boolean.class.equals(clazz)) {
            value = Boolean.valueOf(ReaderUtils.getBooleanAttribute(
                    valueElement, "value", true, false));
        } else if (Integer.class.equals(clazz)) {
            value = new Integer(ReaderUtils.getIntAttribute(valueElement,
                    "value", true, -1));

        } else if (Double.class.equals(clazz)) {
            value = new Double(ReaderUtils.getDoubleAttribute(valueElement,
                    "value", true));
        } else {
            value = ReaderUtils.getAttribute(valueElement, "value", true);
        }

        map.put(parameter, value);    
    }
    void text(String parameter, Element element, Map map, Class clazz)
            throws Exception {
       text(parameter,element,map,clazz,false,null);
    }
    
    <T> void text(String parameter, Element element, Map map, Class<T> clazz, boolean man, T def)
        throws Exception {
        
        String text = ReaderUtils.getChildText(element, parameter);
        if ( text == null ) {
            if ( man ) {
                throw new RuntimeException( "No such element: " + parameter );
            }
            else {
                map.put( parameter, def );
                return;
            }
        }
        
        Object value = text;
        if ( text != null ) {
            if (Boolean.class.equals(clazz)) {
                value = Boolean.valueOf(text);
            } else if (Integer.class.equals(clazz)) {
                value = Integer.valueOf(text);
            } else if (Double.class.equals(clazz)) {
                value = Double.valueOf(text);
            }
        }
        
        map.put(parameter, value);
    }
}
