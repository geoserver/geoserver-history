/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.data.MediaType;
import java.util.Map;
import java.util.HashMap;

public class XMLFormat implements DataFormat{

    public Representation makeRepresentation(Map map){
	return new StringRepresentation("XML not implemented", MediaType.TEXT_PLAIN);
    }
    public Map readRepresentation(Representation rep){
	return new HashMap();
    }
}
