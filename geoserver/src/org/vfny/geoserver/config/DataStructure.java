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

/**
 * DataStructure purpose.
 * <p>
 * This is intended to be used to provide a known interface for data 
 * structures to recursively clone or test equality through data structures 
 * such as Maps or Lists.
 * <p>
 * @see java.util.Map
 * @see java.util.List
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataStructure.java,v 1.1.2.2 2004/01/02 17:13:27 dmzwiers Exp $
 */
public interface DataStructure extends Cloneable {
	
	/**
	 * Implement clone.
	 * <p>
	 * Create a clone of this object and return it.
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return A new DataStructure which is a copy of this DataStructure.
	 */
	Object clone();

	/**
	 * Implement equals.
	 * <p>
	 * Compares the equality of the two objects. 
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj The object to checked for equivalence.
	 * @return true when the objects are the same.
	 */
	boolean equals(Object obj);
}
