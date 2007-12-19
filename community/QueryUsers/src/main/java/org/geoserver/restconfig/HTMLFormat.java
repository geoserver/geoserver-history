/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.geoserver.restconfig.HTMLTemplate;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.restlet.resource.Representation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


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
