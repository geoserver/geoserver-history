/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Representation;


public class HTMLFormat implements DataFormat {
    private String myTemplateName;

    public HTMLFormat(String templateName) {
        myTemplateName = templateName;
    }

    public Representation makeRepresentation(Map map) {
        return HTMLTemplate.getHtmlRepresentation(myTemplateName, map);
    }

    public Map readRepresentation(Representation rep) {
        return new HashMap();
    }
}
