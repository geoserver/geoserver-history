/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Envelope;
/**
 * CloneLibrary purpose is used to try and Set up a Deep Copy for DTO objets.
 * <p>
 * Static Library class for cloning complex structures independant of their
 * contents.
 * <p>
 * Jody here - this is much more accessable when presented to the user as
 * new Type( Type ) idiom. Java clone is messed up, I have seen several projects
 * move to a copy() method or the above idom.
 * </p>
 * <p>
 * For the here and now we can use the above Idom. This will even work with
 * all of our lists and Maps since we are only ever using Strings (imutable)
 * in them.
 * </p>
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: CloneLibrary.java,v 1.1.2.4 2004/01/09 09:21:42 jive Exp $
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
