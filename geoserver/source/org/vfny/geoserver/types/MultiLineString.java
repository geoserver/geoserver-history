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
public class MultiLineString extends Geometry 
{

	LineString[] lines;
	
	public MultiLineString() 
	{
		type = MULTILINESTRING;
	}

	public MultiLineString(LineString[] lines) 
	{
		this();
		this.lines = lines;
		dimension = lines[0].dimension;
	}
	
	public MultiLineString(String value) throws SQLException
	{
		this();
		value = value.trim();
		if ( value.indexOf("MULTILINESTRING") == 0 ) 
		{
			PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.substring(15).trim()),',');
			int nlines = t.getSize();
			lines = new LineString[nlines];
			for( int p = 0; p < nlines; p++)
			{
				lines[p] = new LineString(t.getToken(p));
			}
			dimension = lines[0].dimension;
		} else {
			throw new SQLException("postgis.multilinestringgeometry");
		}
	}
	
	public String toString() 
	{
		return "MULTILINESTRING " + getValue();
	}

	public String getValue() 
	{
		StringBuffer b = new StringBuffer("(");
		for( int p = 0; p < lines.length; p++ )
		{
			if( p > 0 ) b.append(",");
			b.append(lines[p].getValue());
		}
		b.append(")");
		return b.toString();
	}
	
	public int numLines() 
	{
		return lines.length;
	}
	
	public LineString getLine(int idx) 
	{
		if( idx >= 0 & idx < lines.length ) {
			return lines[idx];
		}
		else {
			return null;
		}
	}

}
