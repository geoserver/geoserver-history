/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
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
package org.vfny.geoserver.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import com.vividsolutions.jts.geom.*;
/**
 * EqualsLibrary purpose.
 * <p>
 * Description of EqualsLibrary 
 * 
 * Static Library class for testing equality of complex structures independant of their contents.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: EqualsLibrary.java,v 1.1.2.2 2004/01/03 00:19:20 dmzwiers Exp $
 */
public final class EqualsLibrary {
	
	/**
	 * EqualsLibrary constructor.
	 * <p>
	 * Should never be called, static class.
	 * </p>
	 */
	private EqualsLibrary(){}
	
	/**
	 * equals purpose.
	 * <p>
	 * Performs a complex equality check between two Lists
	 * </p>
	 * @param a One List to be compared
	 * @param b The other List to be compared
	 * @return true if they are equal
	 * @see java.util.List
     * @see java.lang.Object#equals(java.lang.Object)
	 */
	public static boolean equals(List a, List b){
		if(a == b)
			return true;
		if(a == null)
			return false;
		if(b == null)
			return false;
		if(a.size()!=b.size())
			return false;
		Iterator i = a.iterator();
		while(i.hasNext())
			if(!b.contains(i.next()))
				return false;
		return true;
	}
	
	/**
	 * equals purpose.
	 * <p>
	 * Performs a complex equality check between two Maps
	 * </p>
	 * @param a One Map to be compared
	 * @param b The other Map to be compared
	 * @return true if they are equal
	 * @see java.util.Map
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public static boolean equals(Map a, Map b){
		if(a == b)
			return true;
		if(a == null)
			return false;
		if(b == null)
			return false;
		if(a.size()!=b.size())
			return false;
		Iterator i = a.keySet().iterator();
		while(i.hasNext()){
			Object t = i.next();
			if(!b.containsKey(t) || !a.get(t).equals(b.get(t)))
				return false;
		}
		return true;
	}
}
