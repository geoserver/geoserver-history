package org.geoserver.data.feature;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.util.ReaderUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Reads a GeoServer info.xml feature type metadata file.
 * <p>
 * Usage:
 * 
 * <pre>
 * 	<code>
 * 		File featureType = new File( ".../featureTypes/.../info.xml" );
 * 		FeatureTypeReader reader = new FeatureTypeReader();
 * 		reader.read( featureType );
 * 		List dataStores = reader.dataStores();
 * 		LIst nameSpaces = reader.nameSpaces();
 * 	</code>
 * </pre>
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class FeatureTypeReader {

	Logger logger = Logger.getLogger( "org.geoserver.data" );
	
	/** 
	 * Root featureType element.
	 */
	Element featureType;
	/**
	 * The catalog
	 */
	GeoServerCatalog catalog;
	
	
	public FeatureTypeReader( GeoServerCatalog catalog ) {
		this.catalog = catalog;
	}
	
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
	public FeatureTypeInfo read( File file ) throws IOException {
		FileReader reader = new FileReader( file );
		
		try {
			featureType = ReaderUtils.parse( reader );	
			
			FeatureTypeInfo info = new FeatureTypeInfo();
			info.setAbstract( abstrct() );
			info.setTitle( title() );
			info.setLatLongBoundingBox( bbox() );
			info.setTypeName( name() );
			//info.setDefaultStyle( defaultStyle() );
			info.setNumDecimals( numDecimals() );
			info.setSRS( srs() );
			
			return info;
		}
		finally {
			reader.close();	
		}
	}
	
	public String dataStore() {
		return featureType.getAttribute( "datastore" );
	}
	
	public String name() {
		return ReaderUtils.getChildText( featureType, "name" );
	}
	
	public int srs() {
		return Integer.parseInt( ReaderUtils.getChildText( featureType, "SRS" ) );
	}
	
	public String title() {
		return ReaderUtils.getChildText( featureType, "title" );
	}
	
	public String abstrct() {
		return ReaderUtils.getChildText( featureType, "abstract" );
	}
	
	public String[] keywords() {
		String text = ReaderUtils.getChildText( featureType, "keywords" );
		return text.split( ", " );
	}
	
	public Envelope bbox() {
		Element bboxElement = ReaderUtils.getChildElement( featureType, "latLonBoundingBox" );
		
		try {
			double minx = ReaderUtils.getDoubleAttribute( bboxElement, "minx", true );
			double miny = ReaderUtils.getDoubleAttribute( bboxElement, "miny", true );
			double maxx = ReaderUtils.getDoubleAttribute( bboxElement, "maxx", true );
			double maxy = ReaderUtils.getDoubleAttribute( bboxElement, "maxy", true );

			return new Envelope( minx, miny, maxx, maxy );
		}
		catch (Exception e) {
			String msg = "Error reading bounding box for feature type";
			logger.log( Level.WARNING, msg, e );
			return null;
		}
	}
	
	public boolean bboxIsDynamic() {
		Element bboxElement = ReaderUtils.getChildElement( featureType, "latLonBoundingBox" );
		return "true".equals( bboxElement.getAttribute( "dynamic" ) );
	}
	
	public String[] styles() {
		Element stylesElement = ReaderUtils.getChildElement( featureType, "styles" );
		NodeList styleElements = stylesElement.getElementsByTagName( "style" );
		String[] styles = new String[ styleElements.getLength() ];
		for ( int i = 0; i < styleElements.getLength(); i++ ) {
			Element styleElement = (Element) styleElements.item( i );
			styles[i] = ReaderUtils.getElementText( styleElement );
		}
		
		return styles;
	}

	public String defaultStyle() {
		Element stylesElement = ReaderUtils.getChildElement( featureType, "styles" );
		return stylesElement.getAttribute( "default" );
	}
	
	public int numDecimals() {
		Element numDecimalsElement = ReaderUtils.getChildElement( featureType, "numDecimals" );
		try {
			return ReaderUtils.getIntAttribute( numDecimalsElement, "value", true, 0 );
		} 
		catch (Exception e) {
			String msg = "Error reading numDecimals for feature type";
			logger.log( Level.WARNING, msg, e );
			return -1;
		}
	}
}
