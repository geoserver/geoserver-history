/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.Map;
import org.restlet.resource.Representation;

/**
 * The DataFormat interface defines an object that converts back and forth
 * between a Restlet Representation and a Map.
 * @author David Winslow <dwinslow@openplans.org>
 */ 
public interface DataFormat {
    Representation makeRepresentation(Map data);
    Map readRepresentation(Representation representation);
}
