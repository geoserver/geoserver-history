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
public class Polygon extends Geometry 
{

	LinearRing[] rings;
	
	public Polygon() 
	{
		type = POLYGON;
	}

	public Polygon(LinearRing[] rings) 
	{
		this();
		this.rings = rings;
		dimension = rings[0].dimension;
	}
	
	public Polygon(String value) throws SQLException
	{
		this();
		value = value.trim();
		if ( value.indexOf("POLYGON") == 0 ) 
		{
			value = value.substring(7).trim();
		}
		PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value),',');
		int nrings = t.getSize();
		rings = new LinearRing[nrings];
		for( int r = 0; r < nrings; r++)
		{
			rings[r] = new LinearRing(t.getToken(r));
		}
		dimension = rings[0].dimension;
	}
	
	public String toString() 
	{
		return "POLYGON " + getValue();
	}

	public String getValue() 
	{
		StringBuffer b = new StringBuffer("(");
		for( int r = 0; r < rings.length; r++ )
		{
			if( r > 0 ) b.append(",");
			b.append(rings[r].toString());
		}
		b.append(")");
		return b.toString();
	}
	
	public int numRings()
	{
		return rings.length;
	}
	
	public LinearRing getRing(int idx)
	{
		if( idx >= 0 & idx < rings.length ) {
			return rings[idx];
		}
		else {
			return null;
		}
	}
}
