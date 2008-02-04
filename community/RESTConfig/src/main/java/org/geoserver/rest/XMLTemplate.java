/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import freemarker.template.Configuration;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import java.util.Map;


/**
 *
 * @author ak@openplans.org, The Open Planning Project
 *
 */
public class XMLTemplate {
    /**
     * static freemaker configuration
     */
    static Configuration cfg;

    static {
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(XMLTemplate.class, "");
    }

    public static TemplateRepresentation getXmlRepresentation(String template, Map map) {
        return new TemplateRepresentation(template, cfg, map, MediaType.TEXT_XML);
    }
}
