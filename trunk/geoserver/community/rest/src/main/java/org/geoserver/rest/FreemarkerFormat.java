/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.rest;

import org.restlet.data.MediaType;
import org.restlet.resource.Representation;
import org.restlet.ext.freemarker.TemplateRepresentation;
import freemarker.template.Configuration;

import java.util.Map;

/**
 * The FreemarkerFormat class is a DataFormat that uses a Freemarker template to
 * do the heavy lifting.  
 *
 * @author David Winslow <dwinslow@openplans.org>
 */
public class FreemarkerFormat implements DataFormat{
    private Configuration myConfig;
    private MediaType myType;
    private String myTemplateFileName;

    /**
     * Set up a new FreemarkerFormat
     *
     * @param templateName the filename of the template to use
     * @param c a Class object to use for retrieving the template resource
     * @param type the MediaType of the result
     */
    public FreemarkerFormat(String templateName, Class c, MediaType type){
        myTemplateFileName = templateName;
        myConfig = new Configuration();
        myConfig.setClassForTemplateLoading(c, "");
        myType = type;
    }

    public Representation makeRepresentation(Object context){
        return new TemplateRepresentation(myTemplateFileName, myConfig, context, myType);
    }

    public Object readRepresentation(Representation representation){
        return null;
    }
}
