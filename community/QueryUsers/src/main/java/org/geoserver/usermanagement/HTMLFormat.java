/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import java.util.Map;
import java.util.HashMap;
import org.restlet.resource.Representation;
import org.geoserver.restconfig.HTMLTemplate;

public class HTMLFormat implements DataFormat{

    private String myTemplateName;

    public HTMLFormat(String templateName){
	myTemplateName = templateName;
    }

    public Representation makeRepresentation(Map map){
	return HTMLTemplate.getHtmlRepresentation(myTemplateName, map); 
    }

    public Map readRepresentation(Representation rep){
	return new HashMap();
    }
}

