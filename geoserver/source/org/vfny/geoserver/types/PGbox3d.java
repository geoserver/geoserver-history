/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.types;

import org.postgresql.util.*;
import java.sql.*;

/**
 * Implements an OGC simple type.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PGbox3d extends PGobject 
{

	/**
	 * The lower left bottom corner of the box.
	 */
	Point llb;
	
	/**
	 * The upper right top corner of the box.
	 */
	Point urt;
	

	public PGbox3d() {}

	public PGbox3d(Point llb, Point urt) {
		this.llb = llb;
		this.urt = urt;
	}

	public PGbox3d(String value) throws SQLException
	{
		setValue(value);
	}

	public void setValue(String value) throws SQLException
	{
		value = value.trim();
		if( value.startsWith("BOX3D") ) {
			value = value.substring(5);
		}
		PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.trim()),',');
		llb = new Point(t.getToken(0));
		urt = new Point(t.getToken(1));
	}

	public String getValue() {
		return "BOX3D (" + llb.getValue() + "," + urt.getValue() + ")";
	}

	public String toString() {
		return getValue();
	}

	public Object clone() 
	{
		PGbox3d obj = new PGbox3d(llb,urt);
		obj.setType(type);
		return obj;
	}
	
}
