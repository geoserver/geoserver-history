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
public class MultiPolygon extends Geometry 
{

	Polygon[] polygons;
	
	public MultiPolygon() 
	{
		type = MULTIPOLYGON;
	}

	public MultiPolygon(Polygon[] polygons) 
	{
		this();
		this.polygons = polygons;
		dimension = polygons[0].dimension;
	}
	
	public MultiPolygon(String value) throws SQLException
	{
		this();
		value = value.trim();
		if ( value.indexOf("MULTIPOLYGON") == 0 ) 
		{
			PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.substring(12).trim()),',');
			int npolygons = t.getSize();
			polygons = new Polygon[npolygons];
			for( int p = 0; p < npolygons; p++)
			{
				polygons[p] = new Polygon(t.getToken(p));
			}
			dimension = polygons[0].dimension;
		} else {
			throw new SQLException("postgis.multipolygongeometry");
		}
	}
	
	public String toString() 
	{
		return "MULTIPOLYGON " + getValue();
	}

	public String getValue() 
	{
		StringBuffer b = new StringBuffer("(");
		for( int p = 0; p < polygons.length; p++ )
		{
			if( p > 0 ) b.append(",");
			b.append(polygons[p].getValue());
		}
		b.append(")");
		return b.toString();
	}

	public int numPolygons()
	{
		return polygons.length;
	}

	public Polygon getPolygon(int idx) 
	{
		if ( idx >= 0 & idx < polygons.length ) {
			return polygons[idx];
		}
		else {
			return null;
		}
	}	

}
