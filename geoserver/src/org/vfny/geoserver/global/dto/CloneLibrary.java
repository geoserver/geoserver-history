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
package org.vfny.geoserver.global.dto;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Envelope;
/**
 * CloneLibrary purpose.
 * <p>
 * Description of CloneLibrary 
 * 
 * Static Library class for cloning complex structures independant of their contents.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: CloneLibrary.java,v 1.1.2.2 2004/01/06 23:55:02 jive Exp $
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
		for(int i=0;i<lst.length;i++){
			result.add(i,clone(lst[i]));
		}
		
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
		
		//result.putAll(source);
		Iterator keyIter = source.keySet().iterator();
		while(keyIter.hasNext()){
			Object key = keyIter.next();
			result.put(key,clone(source.get(key)));
		}
		
		return result;
	}

	
	/**
	 * clone purpose.
	 * <p>
	 * Clones a Envelope so that it matches the requirements that the returned object 
	 * would be equal to the source.
	 * </p>
	 * @param source The Envelope to be cloned.
	 * @return An exact clone of the list.
	 * @see java.lang.Object#clone()
	 * @see com.vividsolutions.jts.geom.Envelope
	 * @throws CloneNotSupportedException
	 */
	public static Envelope clone(Envelope e){
		Envelope result = new Envelope(e);
		return result;
	}
	
	/**
	 * clone purpose.
	 * <p>
	 * used to check class type and clone it.
	 * </p>
	 * @param ds the DataStructure to clone.
	 * @return a clone of the parameter
	 */
	private static Object clone(DataStructure ds){
		return ds.clone();
	}
	/** Clone a string array */
    public static String[] clone( String[] array ){
        String copy[] = new String[ array.length ];
        System.arraycopy( array, 0, copy, 0, array.length );
        return copy;
    }
	/**
	 * clone purpose.
	 * <p>
	 * to make the compiler happy, should never be here.
	 * </p>
	 * @param obj the parameter to return, does nothing.
	 * @return returns the parameter
	 */
	private static Object clone(Object obj){
		return obj;
	}
}
