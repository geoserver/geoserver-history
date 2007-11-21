package org.geoserver.restconfig;
import java.util.Map;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import freemarker.template.Configuration;

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

    public static TemplateRepresentation getXmlRepresentation(String template,
	    Map map) {
	return new TemplateRepresentation(template, cfg, map,
		MediaType.TEXT_XML);
    }
}
