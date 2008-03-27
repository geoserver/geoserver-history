/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.restlet.resource.Representation;


public class XMLFormat implements DataFormat {
    String myTemplate;

    public XMLFormat(String templ) {
        myTemplate = templ;
    }

    public Representation makeRepresentation(Map map) {
        return XMLTemplate.getXmlRepresentation(myTemplate, map);

        // return new StringRepresentation("XML not implemented", MediaType.TEXT_PLAIN);
    }

    public Map readRepresentation(Representation rep) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(rep.getStream());
            Map map = new HashMap();
            XPath passwordPath = XPath.newInstance("/user/password");
            XPath rolePath = XPath.newInstance("/user/roles/role/@name");
            Element passwordObj = (Element) passwordPath.selectSingleNode(doc);
            List roleObjs = rolePath.selectNodes(doc);
            List roles = new ArrayList();

            Iterator it = roleObjs.iterator();

            while (it.hasNext()) {
                Attribute att = (Attribute) it.next();
                roles.add(att.getValue());
            }

            map.put("password", passwordObj.getText());
            map.put("roles", roles);

            return map;
        } catch (Exception e) {
            e.printStackTrace();

            return new HashMap();
        }
    }
}
