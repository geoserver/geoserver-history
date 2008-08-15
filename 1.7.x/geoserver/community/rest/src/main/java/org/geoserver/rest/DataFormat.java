/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import java.util.Map;

import org.restlet.resource.Representation;


/**
 * The DataFormat interface defines an object that converts back and forth
 * between a Restlet Representation and a Map.
 * @author David Winslow <dwinslow@openplans.org>
 * @see org.geoserver.rest.MapResource
 */
public interface DataFormat {
    /**
     * Convert a Map of data to a Representation.
     * @param data a Map containing the data to be represented
     * @param the corresponding Representation
     */
    Representation makeRepresentation(Object data);

    /**
     * Convert a Representation to a Map
     * @param representation a Representation containing the data to be converted
     * @return the corresponding Map
     */
    Object readRepresentation(Representation representation);
}
