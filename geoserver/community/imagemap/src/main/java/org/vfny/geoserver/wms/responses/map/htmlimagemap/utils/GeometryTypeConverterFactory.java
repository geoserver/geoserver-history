/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.htmlimagemap.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jj2000.j2k.util.ArrayUtil;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
/**
 * Converter factory performing converstions among JTS geometries
 * of different types.
 * 
 *  - single type geometries (Point, LineString, Polygon) are converted to multi types
 *    (MultiPoint, LineString, MultiPolygon) containing 1 geometry element.
 *  - GeometryCollection(s) are converted to multi types, converting each element to the
 *    permitted type (Point, LineString or Polygon).   
 *  
 * @author m.bartolomeoli
 *
 */
public class GeometryTypeConverterFactory implements ConverterFactory {

	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.wms.responses.map.htmlimagemap.utils.GeometryTypeConverterFactory");
	
	static GeometryFactory gFac=new GeometryFactory();
	
	public Converter createConverter(Class<?> source, Class<?> target,
			Hints hints) {
		// we can convert geometric types
		if(Geometry.class.isAssignableFrom(source) && Geometry.class.isAssignableFrom(target)) {
			return new Converter() {
				/**
				 * Converts all the geometries of the given GeometryCollection to a specified
				 * simple type.
				 * @param <T>
				 * @param gc
				 * @param target
				 * @return
				 * @throws Exception
				 */
				public <T> List<T> convertAll(GeometryCollection gc,Class<T> target) throws Exception {
					List<T> result=new ArrayList<T>();
					for(int count=0;count<gc.getNumGeometries();count++) {
						T geo=(T)convert(gc.getGeometryN(count),target);
						if(geo!=null)
							result.add(geo);
					}
					return result;
				}
				
				public Object convert(Object source, Class target)
						throws Exception {
					// hierarchy compatible geometries -> nothing to do
					if(target.isAssignableFrom(source.getClass()))
						return source;
					if(source instanceof Geometry) { 
						Geometry sourceGeometry=(Geometry)source;
						// multi<geometry> types: for each one we
						// try the followings:
						//  - if source is <geometry> we create a multi<geometry> with just 1 element
						//  - if source is a GeometryCollection, we try to convert each element to <geometry>
						//  - else we firtsly convert the geometry to a <geometry> and then we create a multi<geometry> with the obtained element
						if(MultiPoint.class.isAssignableFrom(target)) {
							Point[] points=new Point[] {};
							if(source instanceof Point)
								points=new Point[] {(Point)source};
							else if(source instanceof GeometryCollection)
								points=this.convertAll((GeometryCollection)source,Point.class).toArray(points);												
							else
								points=new Point[] {(Point)this.convert(source,Point.class)};
							return gFac.createMultiPoint(points);
						}
						if(MultiLineString.class.isAssignableFrom(target)) {
							LineString[] lineStrings=new LineString[] {};
							if(source instanceof LineString)
								lineStrings=new LineString[] {(LineString)source};
							else if(source instanceof GeometryCollection)
								lineStrings=this.convertAll((GeometryCollection)source,LineString.class).toArray(lineStrings);							
							else
								lineStrings=new LineString[] {(LineString)this.convert(source,LineString.class)};
							return gFac.createMultiLineString(lineStrings);
						}
						if(MultiPolygon.class.isAssignableFrom(target)) {
							Polygon[] polygons=new Polygon[] {};
							if(source instanceof Polygon)
								polygons=new Polygon[] {(Polygon)source};
							else if(source instanceof GeometryCollection)
								polygons=this.convertAll((GeometryCollection)source,Polygon.class).toArray(polygons);
							else
								polygons=new Polygon[] {(Polygon)this.convert(source,Polygon.class)};
							return gFac.createMultiPolygon(polygons);
						}
						
						// target is a geometrycollection: we add the source to
						// a new geometrycollection
						if(GeometryCollection.class.isAssignableFrom(target))
							return gFac.createGeometryCollection(new Geometry[] {(Geometry)source});						
						
						// target is a point: we return the centroid of any complex geometry
						if(Point.class.isAssignableFrom(target)) {
							if(source instanceof MultiPoint && sourceGeometry.getNumGeometries()==1)
								return ((MultiPoint)source).getGeometryN(0);
							else {
								if(LOGGER.isLoggable(Level.FINE))
									LOGGER.fine("Converting Geometry "+source.toString()+" to Point. This could be unsafe");
								return ((Geometry)source).getCentroid();
							}
						}
						
						// target is a linestring: we return the linestring connecting all the geometry coordinates
						if(LineString.class.isAssignableFrom(target)) {
							if(source instanceof MultiLineString && sourceGeometry.getNumGeometries()==1)
								return ((MultiLineString)source).getGeometryN(0);
							else {
								if(LOGGER.isLoggable(Level.FINE))
									LOGGER.fine("Converting Geometry "+source.toString()+" to LineString. This could be unsafe");
								return gFac.createLineString(getLineStringCoordinates(((Geometry)source).getCoordinates()));
							}
						}
						// target is a polygon: we return a polygon connecting all the coordinates of the given geometry
						if(Polygon.class.isAssignableFrom(target)) {
							if(source instanceof MultiPolygon && sourceGeometry.getNumGeometries()==1)
								return ((MultiPolygon)source).getGeometryN(0);
							else {
								if(LOGGER.isLoggable(Level.FINE))
									LOGGER.fine("Converting Geometry "+source.toString()+" to Polygon. This could be unsafe");
								Coordinate[] coords=getPolygonCoordinates(((Geometry)source).getCoordinates());
								return gFac.createPolygon(gFac.createLinearRing(coords), new LinearRing[] {});
							}
						}
					}
						
					return null;
				}

				/**
				 * Add dummy coordinates to the given array to reach
				 * numpoints points.
				 * If the array is already made of numpoints or
				 * more coordinates, it will be returned untouched.
				 * @param input
				 * @param numpoints
				 * @return
				 */
				private Coordinate[] growCoordinatesNum(Coordinate[] input,int numpoints) {
					if(input.length<numpoints) {
						Coordinate[] newCoordinates=Arrays.copyOf(input,numpoints);
						Arrays.fill(newCoordinates, input.length, numpoints,input[0]);
						
						input=newCoordinates;
					}
					return input;
				}
				/**
				 * Gets a set of coordinates valid to create a linestring:
				 *  - at least 2 coordinates
				 *  
				 * @param coordinates
				 * @return
				 */
				private Coordinate[] getLineStringCoordinates(
						Coordinate[] coordinates) {
					// at least 2 points
					coordinates=growCoordinatesNum(coordinates, 2);
					return coordinates;
				}

				/**
				 * Gets a set of coordinates valid to create a polygon:
				 *  - at least 4 coordinates
				 *  - closed path
				 * @param coordinates
				 * @return
				 */
				private Coordinate[] getPolygonCoordinates(
						Coordinate[] coordinates) {
					// at least 4 points
					coordinates=growCoordinatesNum(coordinates, 4);
					
					if(!coordinates[coordinates.length-1].equals(coordinates[0])) {
						Coordinate[] newCoordinates=Arrays.copyOf(coordinates,coordinates.length+1);
						newCoordinates[newCoordinates.length-1]=newCoordinates[0];
						
						coordinates=newCoordinates;
					}
					return coordinates;
				}
				
			};
			
			
		}
		
		return null;
	}

}
