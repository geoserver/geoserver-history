/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.usermanagement;

import org.geoserver.rest.FreemarkerFormat;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.restlet.resource.Representation;
import org.restlet.data.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class UserHTMLFormat extends FreemarkerFormat {
    public UserHTMLFormat(String templateName) {
        super(templateName, UserHTMLFormat.class, MediaType.TEXT_HTML);
    }

    public Map readRepresentation(Representation rep) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(rep.getStream());
            Map map = new HashMap();

            XPath passwordPath = XPath.newInstance("/html/body/ul/li/span");
            XPath rolePath = XPath.newInstance("/html/body/ul/li/ul/li");
            Element passwordObj = (Element) passwordPath.selectSingleNode(doc);
            List roleObjs = rolePath.selectNodes(doc);
            List roles = new ArrayList();

            map.put("password", passwordObj.getText());

            Iterator it = roleObjs.iterator();

            while (it.hasNext()) {
                Element elt = (Element) it.next();
                roles.add(elt.getText());
            }

            map.put("roles", roles);

            return map;
        } catch (JDOMException jde) {
            // TODO: handle exception gracefully
        } catch (IOException ioe) {
            // TODO: handle exception gracefully
        }

        return new HashMap();
    }
}
