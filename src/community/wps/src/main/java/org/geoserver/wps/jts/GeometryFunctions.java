/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *    
 */
package org.geoserver.wps.jts;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.buffer.BufferParameters;

/**
 * A set of static functions powering the {@link GeometryProcessFactory}
 */
public class GeometryFunctions {

	/**
	 * Maps the enumeration into a set of
	 * 
	 * @author Andrea Aime - OpenGeo
	 * 
	 */
	enum BufferCapStyle {
		Round(BufferParameters.CAP_ROUND), Flat(BufferParameters.CAP_FLAT), Square(
				BufferParameters.CAP_SQUARE);
		int value;

		private BufferCapStyle(int value) {
			this.value = value;
		}
	};

	static public boolean contains(Geometry a, Geometry b) {
		return a.contains(b);
	}

	static public boolean isEmpty(Geometry geom) {
		return geom.isEmpty();
	}

	static public double geomLength(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getLength();
	}

	static public boolean intersects(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.intersects(arg1);
	}

	static public boolean isValid(Geometry arg0) {
		Geometry _this = arg0;

		return _this.isValid();
	}

	static public String geometryType(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getGeometryType();
	}

	static public int numPoints(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getNumPoints();
	}

	static public boolean isSimple(Geometry arg0) {
		Geometry _this = arg0;

		return _this.isSimple();
	}

	static public double distance(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.distance(arg1);
	}

	static public boolean isWithinDistance(Geometry arg0, Geometry arg1,
			double arg2) {
		Geometry _this = arg0;

		return _this.isWithinDistance(arg1, arg2);
	}

	static public double area(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getArea();
	}

	static public Geometry centroid(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getCentroid();
	}

	static public Geometry interiorPoint(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getInteriorPoint();
	}

	static public int dimension(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getDimension();
	}

	static public Geometry boundary(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getBoundary();
	}

	static public int boundaryDimension(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getBoundaryDimension();
	}

	static public Geometry envelope(Geometry arg0) {
		Geometry _this = arg0;

		return _this.getEnvelope();
	}

	static public boolean disjoint(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.disjoint(arg1);
	}

	static public boolean touches(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.touches(arg1);
	}

	static public boolean crosses(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.crosses(arg1);
	}

	static public boolean within(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.within(arg1);
	}

	static public boolean overlaps(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.overlaps(arg1);
	}

	static public boolean relatePattern(Geometry arg0, Geometry arg1,
			String arg2) {
		Geometry _this = arg0;

		return _this.relate(arg1, arg2);
	}

	static public String relate(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.relate(arg1).toString();
	}

	@DescribeProcess(title = "Geometry buffer", description = "Buffers a geometry using a certain distance")
	@DescribeResult(description = "The buffered geometry")
	static public Geometry buffer(
			@DescribeParameter(name = "geom", description = "The geometry to be buffered") Geometry geom,
			@DescribeParameter(name = "distance", description = "The distance (same unit of measure as the geometry)") double distance,
			@DescribeParameter(name = "quadrantSegments", description = "Number of quadrant segments. Use > 0 for round joins, 0 for flat joins, < 0 for mitred joins", min = 0) Integer quadrantSegments,
			@DescribeParameter(name = "capStyle", description = "The buffer cap style, round, flat, square", min = 0) BufferCapStyle capStyle) {
		if (quadrantSegments == null)
			quadrantSegments = BufferParameters.DEFAULT_QUADRANT_SEGMENTS;
		if (capStyle == null)
			capStyle = BufferCapStyle.Round;
		return geom.buffer(distance, quadrantSegments, capStyle.value);
	}

	static public Geometry convexHull(Geometry arg0) {
		Geometry _this = arg0;

		return _this.convexHull();
	}

	static public Geometry intersection(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.intersection(arg1);
	}

	@DescribeProcess(title = "Geometry union", description = "Performs the geometric union of two or more geometries")
	@DescribeResult(description = "The union of all input geometries")
	static public Geometry union(
			@DescribeParameter(name = "geom", description = "The geometries to be united", min = 2) Geometry... geoms) {
		Geometry result = null;
		for (Geometry g : geoms) {
			if (result == null) {
				result = g;
			} else {
				result = result.union(g);
			}
		}
		return result;
	}

	static public Geometry difference(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.difference(arg1);
	}

	static public Geometry symDifference(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.symDifference(arg1);
	}

	static public boolean equalsExactTolerance(Geometry arg0, Geometry arg1,
			double arg2) {
		Geometry _this = arg0;

		return _this.equalsExact(arg1, arg2);
	}

	static public boolean equalsExact(Geometry arg0, Geometry arg1) {
		Geometry _this = arg0;

		return _this.equalsExact(arg1);
	}

	static public int numGeometries(Geometry arg0) {
		GeometryCollection _this = (GeometryCollection) arg0;

		return _this.getNumGeometries();
	}

	static public Geometry getGeometryN(Geometry arg0, int arg1) {
		GeometryCollection _this = (GeometryCollection) arg0;

		return _this.getGeometryN(arg1);
	}

	static public double getX(Geometry arg0) {
		Point _this = (Point) arg0;

		return _this.getX();
	}

	static public double getY(Geometry arg0) {
		Point _this = (Point) arg0;

		return _this.getY();
	}

	static public boolean isClosed(Geometry arg0) {
		LineString _this = (LineString) arg0;

		return _this.isClosed();
	}

	static public Geometry pointN(Geometry arg0, int arg1) {
		LineString _this = (LineString) arg0;

		return _this.getPointN(arg1);
	}

	static public Geometry startPoint(Geometry arg0) {
		LineString _this = (LineString) arg0;

		return _this.getStartPoint();
	}

	static public Geometry endPoint(Geometry arg0) {
		LineString _this = (LineString) arg0;

		return _this.getEndPoint();
	}

	static public boolean isRing(Geometry arg0) {
		LineString _this = (LineString) arg0;

		return _this.isRing();
	}

	static public Geometry exteriorRing(Geometry arg0) {
		Polygon _this = (Polygon) arg0;

		return _this.getExteriorRing();
	}

	static public int numInteriorRing(Geometry arg0) {
		Polygon _this = (Polygon) arg0;

		return _this.getNumInteriorRing();
	}

	static public Geometry interiorRingN(Geometry arg0, int arg1) {
		Polygon _this = (Polygon) arg0;

		return _this.getInteriorRingN(arg1);
	}

}
