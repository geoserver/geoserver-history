/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

/**
 * Marker used to indicate a public "Deep Copy" clone implementation.
 * 
 * <p>
 * This is intended to be used to provide a known interface for data
 * structures to recursively clone or test equality through data structures
 * such as Maps or Lists.
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataStructure.java,v 1.1.2.1 2004/01/04 06:21:33 jive Exp $
 *
 * @see java.util.Map
 * @see java.util.List
 */
public interface DataStructure extends Cloneable {
    /**
     * Implement clone as a Deep Copy.
     * 
     * <p>
     * Create a clone of this object and return it.
     * </p>
     *
     * @return A new DataStructure which is a copy of this DataStructure.
     *
     * @see java.lang.Object#clone()
     */
    Object clone();

    /**
     * Implement equals.
     * 
     * <p>
     * Compares the equality of the two objects.
     * </p>
     *
     * @param obj The object to checked for equivalence.
     *
     * @return true when the objects are the same.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    boolean equals(Object obj);
}