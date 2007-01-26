package org.geoserver.wfs.xml.gml3;

import org.geoserver.wfs.WFSException;
import org.geotools.factory.FactoryNotFoundException;
import org.geotools.referencing.CRS;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Subclass of {@link org.geotools.gml3.bindings.AbstractGeometryTypeBinding} which performs some
 * addtional validation checks. 
 * <p>
 * Checks include:
 * <ul>
 * 	<li>All geometries have a crs, when not specified, the server default is used.
 *  <li>If a crs is specified it has a valid authority
 * 	<li>Points defined on geometries fall into the valid coordinate space defined by crs.
 * </ul>
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class AbstractGeometryTypeBinding extends
		org.geotools.gml3.bindings.AbstractGeometryTypeBinding {
	
	public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
		
		try {
			if ( node.hasAttribute("srsName") ) {
				CRS.decode( node.getAttributeValue( "srsName" ).toString() );
			}
		} 
		catch ( NoSuchAuthorityCodeException e ) {
			throw new WFSException( "Invalid Authority Code: " + e.getAuthorityCode(), "InvalidParameterValue" );	
		}
		
		Geometry geometry = (Geometry) super.parse( instance, node, value );
		if ( geometry != null ) {
			//1. ensure a crs is set
			if ( geometry.getUserData() == null ) {
				//set to be server "Default"
				//TODO: actually make this configurable
				geometry.setUserData( CRS.decode( "EPSG:4326" ) );
			}
			
			//2. ensure the coordiantes of the geometry fall into valid space defined by crs
			CoordinateReferenceSystem crs = (CoordinateReferenceSystem) geometry.getUserData();
			Coordinate[] c = geometry.getCoordinates();
			
			//named x,y, but could be anything
			CoordinateSystemAxis x = crs.getCoordinateSystem().getAxis( 0 );
			CoordinateSystemAxis y = crs.getCoordinateSystem().getAxis( 1 );
			
			for ( int i = 0; i < c.length; i++ ) {
				if ( c[i].x < x.getMinimumValue() || c[i].x > x.getMaximumValue() ) {
					throw new WFSException( 
						c[i].x + " outside of (" + x.getMinimumValue() + "," + x.getMaximumValue() + ")", "InvalidParameterValue"
					);
				}
				if ( c[i].y < y.getMinimumValue() || c[i].y > y.getMaximumValue() ) {
					throw new WFSException( 
						c[i].y + " outside of (" + y.getMinimumValue() + "," + y.getMaximumValue() + ")" , "InvalidParameterValue"
					);
				}
			}
		}
		
		return geometry;
	}
	

}
