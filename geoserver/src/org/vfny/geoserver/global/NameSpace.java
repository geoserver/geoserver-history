/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.global.dto.NameSpaceDTO;


/**
 * NameSpace purpose.
 * 
 * <p>
 * A representation of a namespace for the Geoserver application.
 * </p>
 * 
 * <p></p>
 * 
 * <p>
 * NameSpace ns = new NameSpace(dto); System.out.println(ns.getPrefix() +
 * ns.getUri());
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: NameSpace.java,v 1.1.2.10 2004/01/09 17:15:30 dmzwiers Exp $
 */
public class NameSpace extends GlobalLayerSupertype {
    /**
     * A copy of the NameSpaceDTO which contains the data for this class.
     * Editing the DTO should be completed with extreme caution.
     */
    private NameSpaceDTO nsDTO;

    /**
     * NameSpaceConfig constructor.
     * 
     * <p>
     * Creates a NameSpaceConfig to represent an instance with default data.
     * </p>
     */
    public NameSpace() {
        nsDTO = new NameSpaceDTO();
    }

    /**
     * NameSpaceConfig constructor.
     * 
     * <p>
     * Creates a NameSpaceConfig based on the data provided. All the data
     * structures are cloned.
     * </p>
     *
     * @param ns The namespace to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public NameSpace(NameSpaceDTO ns) {
        if (ns == null) {
            throw new NullPointerException();
        }

        nsDTO = (NameSpaceDTO) ns.clone();
    }

    /**
     * NameSpaceConfig constructor.
     * 
     * <p>
     * Creates a copy of the NameSpaceConfig provided. All the data structures
     * are cloned.
     * </p>
     *
     * @param ns The namespace to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public NameSpace(NameSpace ns) {
        if (ns == null) {
            throw new NullPointerException();
        }

        nsDTO = new NameSpaceDTO();
        nsDTO.setPrefix(ns.getPrefix());
        nsDTO.setUri(ns.getUri());
        nsDTO.setDefault(ns.isDefault());
    }

    /**
     * Implement toDTO.
     * 
     * <p>
     * Package method used by GeoServer. This method may return references, and
     * does not clone, so extreme caution sould be used when traversing the
     * results.
     * </p>
     *
     * @return NameSpaceDTO An instance of the data this class represents.
     *         Please see Caution Above.
     *
     * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
     * @see NameSpaceDTO
     */
    Object toDTO() {
        return nsDTO;
    }

    /**
     * Implement clone.
     * 
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this NameSpaceConfig
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new NameSpace(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The NameSpaceConfig object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        NameSpace ns = (NameSpace) obj;

        return ((nsDTO.getPrefix() == ns.getPrefix())
        && ((nsDTO.getUri() == ns.getUri())
        && (nsDTO.isDefault() == ns.isDefault())));
    }

    /**
     * isDefault purpose.
     * 
     * <p>
     * Whether this is the default namespace.
     * </p>
     *
     * @return true when this is the default namespace.
     */
    public boolean isDefault() {
        return nsDTO.isDefault();
    }

    /**
     * getPrefix purpose.
     * 
     * <p>
     * returns the namespace's prefix.
     * </p>
     *
     * @return String the namespace's prefix
     */
    public String getPrefix() {
        return nsDTO.getPrefix();
    }

    /**
     * getUri purpose.
     * 
     * <p>
     * returns the namespace's uri.
     * </p>
     *
     * @return String the namespace's uri.
     */
    public String getUri() {
        return nsDTO.getUri();
    }

    /**
     * setDdefault purpose.
     * 
     * <p>
     * sets the default namespace.
     * </p>
     *
     * @param b this is the default namespace.
     */
    public void setDefault(boolean b) {
        nsDTO.setDefault(b);
    }

    /**
     * setPrefix purpose.
     * 
     * <p>
     * stores the namespace's prefix.
     * </p>
     *
     * @param string the namespace's prefix.
     */
    public void setPrefix(String string) {
        nsDTO.setPrefix(string);
    }

    /**
     * setUri purpose.
     * 
     * <p>
     * Stores the namespace's uri.
     * </p>
     *
     * @param string the namespace's uri.
     */
    public void setUri(String string) {
        nsDTO.setUri(string);
    }
}
