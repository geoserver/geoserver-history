/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.db.jdbc;

import java.sql.*;

import org.postgresql.util.*;

import org.vfny.geoserver.types.*;

/**
 * Implements an OGC simple type.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PGgeometry extends PGobject 
{

	Geometry geom;

	public PGgeometry() {}

	public PGgeometry(Geometry geom) {
		this.geom = geom;
	}

	public PGgeometry(String value) throws SQLException
	{
		setValue(value);
	}

	public void setValue(String value) throws SQLException
	{
		value = value.trim();
		if( value.startsWith("MULTIPOLYGON")) {
			geom = (MultiPolygon) new PostgisMultiPolygon(value);
		} else if( value.startsWith("MULTILINESTRING")) {
			geom = (MultiLineString) new PostgisMultiLineString(value);
		} else if( value.startsWith("MULTIPOINT")) {
			geom = (MultiPoint) new PostgisMultiPoint(value);
		} else if( value.startsWith("POLYGON")) {
			geom = (Polygon) new PostgisPolygon(value);
		} else if( value.startsWith("LINESTRING")) {
			geom = (LineString) new PostgisLineString(value);
		} else if( value.startsWith("POINT")) {
			geom = (Point) new PostgisPoint(value);
		} else {
			throw new SQLException("Unknown type: " + value);
		}
	}

	public Geometry getGeometry() {
		return geom;
	}

	public int getGeoType() {
		return geom.type;
	}

	public String toString() {
		return geom.toString();
	}

	public String getValue() {
		return geom.toString();
	}

	public Object clone() 
	{
		PGgeometry obj = new PGgeometry(geom);
		obj.setType(type);
		return obj;
	}
	
}
