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
package org.vnfy.geoserver.config;

import java.util.*;
//import com.vividsolutions.jts.geom.*;
/**
 * CloneLibrary purpose.
 * <p>
 * Description of CloneLibrary 
 * 
 * Static Library class for cloning complex structures independant of their contents.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: EqualsLibrary.java,v 1.1.2.1 2003/12/30 23:39:15 dmzwiers Exp $
 */
public final class EqualsLibrary {
	private EqualsLibrary(){}
	
	public static boolean equals(List a, List b){
		if(a == null)
			return true;
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
	
	public static boolean equals(Map a, Map b){
		if(a == null)
			return true;
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
