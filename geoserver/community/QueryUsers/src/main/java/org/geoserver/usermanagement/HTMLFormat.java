/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import org.restlet.resource.Representation;
import org.geoserver.restconfig.HTMLTemplate;

import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

public class HTMLFormat implements DataFormat{

    private String myTemplateName;

    public HTMLFormat(String templateName){
	myTemplateName = templateName;
    }

    public Representation makeRepresentation(Map map){
	return HTMLTemplate.getHtmlRepresentation(myTemplateName, map); 
    }

    public Map readRepresentation(Representation rep){
	try{
	    SAXBuilder builder = new SAXBuilder();
	    Document doc = builder.build(rep.getStream());
	    Map map = new HashMap();

	    XPath passwordPath = XPath.newInstance("/html/body/ul/li/span");
	    Object passwordObj = passwordPath.selectSingleNode(doc);
	    map.put("password", passwordObj.toString());

	    return map; 
	} catch (JDOMException jde){
	    // TODO: handle exception gracefully
	} catch (IOException ioe){
	    // TODO: handle exception gracefully
	}

	return new HashMap();
    }
}

