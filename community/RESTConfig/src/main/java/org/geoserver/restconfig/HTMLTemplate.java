package org.geoserver.restconfig;
import java.util.HashMap;
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

	public static TemplateRepresentation getHtmlRepresentation(String template,
			HashMap map) {
		return new TemplateRepresentation(template, cfg, map,
				MediaType.TEXT_HTML);
	}
}