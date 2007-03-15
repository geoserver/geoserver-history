/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;


/**
 * Marker used to indicate a public "Deep Copy" clone implementation.
 *
 * <p>
 * This is intended to be used to provide a known interface for data structures
 * to recursively clone or test equality through data structures such as Maps
 * or Lists.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id$
 *
 * @see java.util.Map
 * @see java.util.List
 */
public interface DataTransferObject extends Cloneable {
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
     * Compares the equality of the two objects.
     *
     * @param obj The object to checked for equivalence.
     *
     * @return <code>true</code> when the objects are the same.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    boolean equals(Object other);

    /**
     * DOCUMENT ME!
     *
     * @return hasCode for this Object
     *
     * @see java.lang.Object#hashCode()
     */
    int hashCode();
}
