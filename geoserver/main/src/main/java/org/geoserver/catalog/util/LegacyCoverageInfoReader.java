/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.geoserver.ows.util.XmlCharsetDetector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Reads a legacy coverage info.xml file.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class LegacyCoverageInfoReader {

    /**
     * Root catalog element.
     */
    Element coverage;
    
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
        Reader reader = XmlCharsetDetector.getCharsetAwareReader(new FileInputStream(file));

        try {
            coverage = ReaderUtils.parse(reader);
        } finally {
            reader.close();
        }
    }
    
    
    public String format() {
        return coverage.getAttribute( "format" );
    }
    
    public String name() {
        return ReaderUtils.getChildText(coverage, "name");
    }
    
    public String description() {
        return ReaderUtils.getChildText(coverage, "description");
    }
    
    public String label() {
        return ReaderUtils.getChildText(coverage, "label");
    }
    
    public Map<String,String> metadataLink() {
        HashMap<String,String> ml = new HashMap<String, String>();
        ml.put( "about", ReaderUtils.getChildAttribute(coverage, "metadataLink", "about" ) );
        ml.put( "metadataType",  ReaderUtils.getChildAttribute(coverage, "metadataLink", ",metadataType" ) );

        return ml;
    }
    
    public List<String> keywords() {
        String raw = ReaderUtils.getChildText( coverage, "keywords" );
        StringTokenizer st = new StringTokenizer( raw, ", " );
        ArrayList keywords = new ArrayList();
        while( st.hasMoreTokens() ) {
                keywords.add( st.nextToken() );
        }
        
        return keywords;
    }
    
    public String defaultStyle() throws Exception  {
        Element styles = ReaderUtils.getChildElement(coverage, "styles" );
        return ReaderUtils.getAttribute( styles, "default", true );
    }
    
    public Map<String,Object> envelope() throws Exception {
        Element envelopeElement = ReaderUtils.getChildElement(coverage, "envelope");
        HashMap<String,Object> e = new HashMap<String, Object>();
    
        e.put( "crs", ReaderUtils.getAttribute(envelopeElement, "crs", false));
        e.put( "srsName", ReaderUtils.getAttribute(envelopeElement, "srsName", false));
        
        Element[] posElements = ReaderUtils.getChildElements(envelopeElement, "pos" );
        String[] pos1 = posElements[0].getFirstChild().getTextContent().split( " " );
        String[] pos2 = posElements[1].getFirstChild().getTextContent().split( " " );
        
        e.put( "x1", Double.parseDouble(pos1[0]) );
        e.put( "y1", Double.parseDouble(pos1[1]) );
        e.put( "x2", Double.parseDouble(pos2[0]) );
        e.put( "y2", Double.parseDouble(pos2[1]) );
        
        return e;
    }
    
    public Map<String,Object> grid() throws Exception {
        Element gridElement = ReaderUtils.getChildElement(coverage, "grid");
        HashMap<String,Object> grid = new HashMap<String, Object>();
        
        grid.put( "dimension", 
            Integer.parseInt( ReaderUtils.getAttribute(gridElement, "dimension", true ) ) );
        
        Element lowElement = ReaderUtils.getChildElement(gridElement, "low");
        String[] lows = lowElement.getFirstChild().getTextContent().trim().split( " ");
        int[] low = new int[lows.length];
        for ( int i = 0; i < low.length; i++ ) {
            low[i] = Integer.parseInt( lows[i] );
        }
        grid.put( "low", low );
        
        Element highElement = ReaderUtils.getChildElement(gridElement, "high" );
        String[] highs = highElement.getFirstChild().getTextContent().trim().split( " ");      
        int[] high = new int[highs.length];
        for ( int i = 0; i < high.length; i++ ) {
            high[i] = Integer.parseInt( highs[i] );
        }
        grid.put( "high", high );
        
        Element[] axisNameElements = ReaderUtils.getChildElements( gridElement, "axisName" );
        String[] axisName = new String[ axisNameElements.length ];
        for ( int i = 0; i < axisName.length; i++ ) {
            axisName[i] = axisNameElements[i].getFirstChild().getTextContent();
        }
        grid.put( "axisName", axisName );
        
        Element geoTransformElement = ReaderUtils.getChildElement( gridElement, "geoTransform" );
        if ( geoTransformElement != null) {
            Map<String,Double> geoTransform = new HashMap<String, Double>();
            String scaleX = ReaderUtils.getChildText( geoTransformElement, "scaleX" );
            String scaleY = ReaderUtils.getChildText( geoTransformElement, "scaleY" );
            String shearX = ReaderUtils.getChildText( geoTransformElement, "shearX" );
            String shearY = ReaderUtils.getChildText( geoTransformElement, "shearY" );
            String translateX = ReaderUtils.getChildText( geoTransformElement, "translateX" );
            String translateY = ReaderUtils.getChildText( geoTransformElement, "translateY" );
            
            geoTransform.put( "scaleX", scaleX != null ? new Double( scaleX ) : null );
            geoTransform.put( "scaleY", scaleY != null ? new Double( scaleY ) : null );
            geoTransform.put( "shearX", shearX != null ? new Double( shearX ) : null );
            geoTransform.put( "shearY", shearY != null ? new Double( shearY ) : null );
            geoTransform.put( "translateX", translateX != null ? new Double( translateX ) : null );
            geoTransform.put( "translateY", translateY != null ? new Double( translateY ) : null );

            grid.put( "geoTransform", geoTransform );
        }
        return grid;
    }
    
    public List<Map> coverageDimensions() throws Exception {
        Element[] cdElements = ReaderUtils.getChildElements( coverage, "CoverageDimension" );
        List<Map> cds = new ArrayList<Map>();
        for ( int i = 0; i < cdElements.length; i++ ) {
            HashMap cd = new HashMap();
            cd.put( "name", ReaderUtils.getChildText( cdElements[i], "name" ) );
            cd.put( "description", ReaderUtils.getChildText( cdElements[i], "description" ) );

            Element intervalElement = ReaderUtils.getChildElement(cdElements[i], "interval");
            double min = Double.parseDouble( ReaderUtils.getChildText(intervalElement, "min") );
            double max = Double.parseDouble( ReaderUtils.getChildText(intervalElement, "max") );
            
            cd.put( "min", min );
            cd.put( "max", max );
            cds.add( cd );
        }
        
        return cds;
        
    }
    
    public List<String> requestCRSs() throws Exception {
        Element supportedCRS = ReaderUtils.getChildElement(coverage, "supportedCRSs" );
        if (ReaderUtils.getChildText( supportedCRS, "requestCRSs" ) != null) {
            String[] requestCRS = ReaderUtils.getChildText( supportedCRS, "requestCRSs" ).trim().split(",");
            return Arrays.asList(requestCRS);
        } else
            return new ArrayList<String>();
    }
    
    public List<String> responseCRSs() throws Exception {
        Element supportedCRS = ReaderUtils.getChildElement(coverage, "supportedCRSs" );
        if (ReaderUtils.getChildText( supportedCRS, "responseCRSs" ) != null) {
            String[] responseCRS = ReaderUtils.getChildText( supportedCRS, "responseCRSs" ).trim().split(",");
            return Arrays.asList(responseCRS);
        } else
            return new ArrayList<String>();
    }
    
    public String nativeFormat() throws Exception {
        Element supportedFormats = ReaderUtils.getChildElement(coverage, "supportedFormats" );
        return ReaderUtils.getAttribute(supportedFormats, "nativeFormat", true);
    }
    
    public List<String> supportedFormats() throws Exception {
        Element supportedFormats = ReaderUtils.getChildElement(coverage, "supportedFormats" );
        if (ReaderUtils.getChildText(supportedFormats, "formats" ) != null) {
            String[] formats = ReaderUtils.getChildText(supportedFormats, "formats" ).split(",");
            return Arrays.asList(formats);
        } else
            return new ArrayList<String>();
    }
    
    public String defaultInterpolation() throws Exception {
        Element supportedFormats = ReaderUtils.getChildElement(coverage, "supportedInterpolations" );
        return ReaderUtils.getAttribute(supportedFormats, "default", true);
    }
    
    public List<String> supportedInterpolations() throws Exception {
        Element supportedFormats = ReaderUtils.getChildElement(coverage, "supportedInterpolations" );
        if (ReaderUtils.getChildText(supportedFormats, "interpolationMethods" ) != null) {
            String[] interpolations = ReaderUtils.getChildText(supportedFormats, "interpolationMethods" ).split(",");
            return Arrays.asList(interpolations);
        } else
            return new ArrayList<String>();
    }

    
    public Map<String,Serializable> parameters() {
           Element parameters = ReaderUtils.getChildElement(coverage, "parameters");
           if ( parameters == null ) {
                   return Collections.EMPTY_MAP;
           }
           
           HashMap<String,Serializable> map = new HashMap<String, Serializable>();
           Element[] parameter = ReaderUtils.getChildElements( parameters, "parameter" );
           for ( int i = 0; i < parameter.length; i++ ) {
                   String name = parameter[i].getAttribute("name");
                   String value = parameter[i].getAttribute( "value" );
                   
                   map.put( name, value );
           }
           
           return map;
    }
    
    public String parentDirectoryName() {
        return parentDirectory.getName();
    }


    public List<String> styles() throws Exception {
        List<String> styles = new ArrayList<String>();
        Element styleElement = ReaderUtils.getChildElement(coverage, "styles" );
        final NodeList childrens = styleElement.getChildNodes();
        final int numChildNodes = childrens.getLength();
        for (int n = 0; n < numChildNodes; n++) {
            final Node child = childrens.item(n);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals("style")) {
                    styles.add(ReaderUtils.getElementText((Element) child));
                }
            }
        }
        
        return styles;
    }
}
