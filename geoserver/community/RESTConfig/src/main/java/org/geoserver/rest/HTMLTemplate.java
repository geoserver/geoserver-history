/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;

import freemarker.template.Configuration;


/**
 *
 * @author aaime, The Open Planning Project
 *
 */
public class HTMLTemplate {
    /**
     * static freemaker configuration
     */
    static Configuration cfg;

    static {
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(HTMLTemplate.class, "");
    }

    public static TemplateRepresentation getHtmlRepresentation(String template, Map map) {
        return new TemplateRepresentation(template, cfg, map, MediaType.TEXT_HTML);
    }
}
