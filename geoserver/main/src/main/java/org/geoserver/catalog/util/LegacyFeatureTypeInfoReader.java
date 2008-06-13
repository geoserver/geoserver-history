package org.geoserver.catalog.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.geoserver.ows.util.XmlCharsetDetector;
import org.geoserver.util.ReaderUtils;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Reads a legacy GeoServer 1.x feature type info.xml file.
 *  
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class LegacyFeatureTypeInfoReader {

    /**
     * Root featureType element.
     */
    Element featureType;
    
    /**
     * The directory containing the feature type info.xml file
     */
    File parentDirectory;
    
    /**
     * Parses the info.xml file into a DOM.
     * <p>
     * This method *must* be called before any other methods.
     * </p>
     *
     * @param file The info.xml file.
     *
     * @throws IOException In event of a parser error.
     */
    public void read(File file) throws IOException {
        parentDirectory = file.getParentFile();
        Reader reader = 
            XmlCharsetDetector.getCharsetAwareReader(new FileInputStream(file));
        
        try {
            featureType = ReaderUtils.parse(reader);
        } finally {
            reader.close();
        }
    }
    
    
    public String dataStore() throws Exception {
    	return ReaderUtils.getAttribute( featureType, "datastore", true );
    }
    
    public String name() {
    	return ReaderUtils.getChildText( featureType, "name" );
    }
    
    public String alias() {
        return ReaderUtils.getChildText( featureType, "alias" );
    }
    
    public String srs() throws Exception {
    	return ReaderUtils.getChildText( featureType, "SRS" );
    }
    
    public int srsHandling() {
        String s = ReaderUtils.getChildText( featureType, "SRSHandling" );
        if ( s == null || "".equals( s ) ) {
            return -1;
        }
        
        return Integer.parseInt( s );
    }
    
    public String title() {
    	return ReaderUtils.getChildText( featureType, "title" );
    }
    
    public String abstrct() {
    	return ReaderUtils.getChildText( featureType, "abstract" );
    }
    
    public List<String> keywords() {
    	String raw = ReaderUtils.getChildText( featureType, "keywords" );
    	StringTokenizer st = new StringTokenizer( raw, ", " );
    	ArrayList keywords = new ArrayList();
    	while( st.hasMoreTokens() ) {
    		keywords.add( st.nextToken() );
    	}
    	
    	return keywords;
    }
    
    public Envelope latLonBoundingBox() throws Exception {
    	Element box = ReaderUtils.getChildElement(featureType, "latLonBoundingBox" );
    	double minx = ReaderUtils.getDoubleAttribute(box, "minx", true );
    	double miny = ReaderUtils.getDoubleAttribute(box, "miny", true );
    	double maxx = ReaderUtils.getDoubleAttribute(box, "maxx", true );
    	double maxy = ReaderUtils.getDoubleAttribute(box, "maxy", true );
    
    	return new Envelope( minx, maxx, miny, maxy );
    }
    
    public Envelope nativeBoundingBox() throws Exception {
        Element box = ReaderUtils.getChildElement(featureType, "nativeBBox" );
        boolean dynamic = ReaderUtils.getBooleanAttribute(box, "dynamic", false, true);
        if ( dynamic ) {
            return null;
        }
        
        double minx = ReaderUtils.getDoubleAttribute(box, "minx", true );
        double miny = ReaderUtils.getDoubleAttribute(box, "miny", true );
        double maxx = ReaderUtils.getDoubleAttribute(box, "maxx", true );
        double maxy = ReaderUtils.getDoubleAttribute(box, "maxy", true );
    
        return new Envelope( minx, maxx, miny, maxy );
    }
    
    public String defaultStyle() throws Exception {
    	Element styles = ReaderUtils.getChildElement(featureType, "styles" );
    	return ReaderUtils.getAttribute( styles, "default", false );
    }
    
    public Map<String,Object> legendURL() throws Exception {
        Element legendURL = ReaderUtils.getChildElement(featureType, "LegendURL");

        if (legendURL != null) {
            HashMap map = new HashMap();
            map.put( "width", ReaderUtils.getAttribute(legendURL, "width", true) );
            map.put( "height",Integer.parseInt(ReaderUtils.getAttribute(legendURL, "height", true) ) );
            map.put( "format", ReaderUtils.getChildText(legendURL, "Format", true) ); 
            map.put( "onlineResource", ReaderUtils.getAttribute(ReaderUtils.getChildElement(
                    legendURL, "OnlineResource", true), "xlink:href", true) );
            return map;
        }
        
        return null;
    }
    
    public boolean searchable() {
        Element searchable = ReaderUtils.getChildElement( featureType, "searchable");
        if ( searchable != null ) {
            try {
                return "true".equals( ReaderUtils.getAttribute( searchable, "enabled", false ) );
            } catch (Exception e) {
            }
        }
        
        return false;
    }
    
    public String regionateAttribute() {
        Element regionateAttribute = ReaderUtils.getChildElement( featureType, "regionateAttribute");
        if ( regionateAttribute != null ) {
            return regionateAttribute.getAttribute( "value" );
        }
        
        return null;
    }

    public String regionateStrategy() {
        Element regionateStrategy = ReaderUtils.getChildElement( featureType, "regionateStrategy");
        if ( regionateStrategy != null ) {
            return regionateStrategy.getAttribute("value");
        }

        return "data";
    }

    public int regionateFeatureLimit() {
        Element regionateFeatureLimit = ReaderUtils.getChildElement(featureType, "regionateFeatureLimit");
        try{
            return Integer.valueOf(regionateFeatureLimit.getAttribute("value"));
        } catch (Exception e) {
            return 10;
        }
    }
    
    public String parentDirectoryName() {
        return parentDirectory.getName();
    }
    
}
