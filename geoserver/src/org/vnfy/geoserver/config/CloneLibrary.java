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

import java.util.List;
import java.util.Map;

import java.lang.reflect.Constructor;
import com.vividsolutions.jts.geom.*;
/**
 * CloneLibrary purpose.
 * <p>
 * Description of CloneLibrary 
 * 
 * Static Library class for cloning complex structures independant of their contents.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: CloneLibrary.java,v 1.1.2.2 2003/12/31 00:35:05 dmzwiers Exp $
 */
public final class CloneLibrary {
	
	/**
	 * clone purpose.
	 * <p>
	 * Clones a List so that it matches the requirements that the returned object 
	 * would be equal to the source.
	 * </p>
	 * @param source The list to be cloned.
	 * @return An exact clone of the list.
	 * @see java.lang.Object#clone()
	 * @see java.util.List
	 * @throws CloneNotSupportedException
	 */
	public static List clone(List source) throws CloneNotSupportedException{
		if(source == null)
			return null;
			
		List result;
		
		//to get an exact instance, need to match sub-types for .equals method
		try{
			Class c = source.getClass();
			Constructor ct = c.getConstructor(new Class[0]);
			result = (List)ct.newInstance(new Object[0]);
		}catch(Exception e){
			throw new CloneNotSupportedException();
		}
		Object[] lst = source.toArray();
		for(int i=0;i<lst.length;i++)
			result.add(i,lst[i]);
		
		return result;
	}
	
	/**
	 * clone purpose.
	 * <p>
	 * Clones a Map so that it matches the requirements that the returned object 
	 * would be equal to the source.
	 * </p>
	 * @param source The Map to be cloned.
	 * @return An exact clone of the list.
	 * @see java.lang.Object#clone()
	 * @see java.util.Map
	 * @throws CloneNotSupportedException
	 */
	public static Map clone(Map source) throws CloneNotSupportedException{
		if(source == null)
			return null;
			
		Map result;
		
		//to get an exact instance, need to match sub-types for .equals method
		try{
			Class c = source.getClass();
			Constructor ct = c.getConstructor(new Class[0]);
			result = (Map)ct.newInstance(new Object[0]);
		}catch(Exception e){
			throw new CloneNotSupportedException();
		}
		
		result.putAll(source);
		return result;
	}
	
	public static Envelope clone(Envelope e){
		//guess here
		Envelope result = new Envelope(e);
		//Envelope result = new Envelope(e.getMinX(), e.getMaxX(), e.getMinY(), e.getMaxY());
		return result;
	}
}
