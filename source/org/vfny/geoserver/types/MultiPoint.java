/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.types;

import org.postgresql.util.*;
import java.sql.*;
import java.util.*;

/**
 * Implements an OGC simple type.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class MultiPoint extends Geometry 
{

	Point[] points;
	
	public MultiPoint() 
	{
		type = MULTIPOINT;
	}

	public MultiPoint(Point[] points) 
	{
		this();
		this.points = points;
		dimension = points[0].dimension;
	}
	
	public MultiPoint(String value) throws SQLException
	{
		this();
		value = value.trim();
		if ( value.indexOf("MULTIPOINT") == 0 ) 
		{
			PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.substring(10).trim()),',');
			int npoints = t.getSize();
			points = new Point[npoints];
			for( int p = 0; p < npoints; p++)
			{
				points[p] = new Point(t.getToken(p));
			}
			dimension = points[0].dimension;
		} else {
			throw new SQLException("postgis.multipointgeometry");
		}
	}
	
	public String toString() 
	{
		return "MULTIPOINT " + getValue();
	}

	public String getValue() 
	{
		StringBuffer b = new StringBuffer("(");
		for( int p = 0; p < points.length; p++ )
		{
			if( p > 0 ) b.append(",");
			b.append(points[p].getValue());
		}
		b.append(")");
		return b.toString();
	}
	
	public int numPoints()
	{
		return points.length;
	}
	
	public Point getPoint(int idx) 
	{
		if ( idx >= 0 & idx < points.length ) {
			return points[idx];
		}
		else {
			return null;
		}
	}
	

}
