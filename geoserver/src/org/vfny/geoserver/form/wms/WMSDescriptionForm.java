/*
 * Created on Jan 7, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.wms;

import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.vfny.geoserver.config.WMSConfig;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WMSDescriptionForm extends ActionForm implements PlugIn {
	private String name;
	private String title;
	private String accessConstraints;
	private String fees;
	private String maintainer;
	private String keywords;
	private String _abstract;
	
	
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		ServletContext context = getServlet().getServletContext();
		WMSConfig config =
			(WMSConfig) context.getAttribute("GeoServer.WFSConfig");
		
		this.name = config.getName();
		this._abstract = config.getAbstract();
		this.fees = config.getFees();
		this.maintainer = config.getMaintainer();
		this.title = config.getTitle();
		this.accessConstraints = config.getAccessConstraints();
	
		/*
		 *  -- ServiceConfig.keywords now represented as String[] -rgould
		 * 
			List list = config.getKeywords();
			String out = "";
			
			for (int i = 0; i < list.size();i++) {
				out = out + list.get(i) + System.getProperty("line.separator");
			}	
		*/	

		String out = "";
		for (int i = 0; i < config.getKeywords().length; i++) {
			out = out + config.getKeywords()[i] + System.getProperty("line.separator");
		}
		
		this.keywords = out;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
	 */
	public void init(ActionServlet actionServlet, ModuleConfig moduleConfig ) throws ServletException {
		// TODO Auto-generated method stub
		WMSConfig config = new WMSConfig();
		config.setAbstract("Hello Richard? Testing? 1 2 3 Testing?");
		config.setAccessConstraints("none");
		config.setEnabled( true );
		config.setFees("A small fish");
		String[] keywords = new String[4];
		keywords[0] = ("GeoServer");
		keywords[1] = ("Configuration");
		keywords[2] = ("STRUTS");
		keywords[3] = ("test");
		config.setKeywords( keywords );
		config.setMaintainer("Refractions Research");
		config.setName("WFS");
		try {
			config.setOnlineResource(new URL("http://vwfs.refractions.net/"));
		} catch (Exception e) {
			//do nothing
		}

		config.setTitle("Sample WFS Configuration");
	
		actionServlet.getServletContext().setAttribute( "GeoServer.WFSConfig", config ); 	
	}

		public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
			ActionErrors errors = new ActionErrors();
		
			if (name == null || name.length() == 0) {
				errors.add("name", new ActionError("error.name.required"));
			}
		
			if (title == null || title.length() == 0 ){
				errors.add("title", new ActionError("error.title.required")); 
			}
		
			if (fees == null  || fees.length() == 0 ) {
				errors.add("fees", new ActionError("error.fees.required"));
			}
		
			if (accessConstraints == null || accessConstraints.length() == 0) {
				errors.add("accessConstraints", new ActionError("error.accessConstraints.required"));
			}
		
			if (maintainer == null || maintainer.length() == 0) {
				errors.add("maintainer", new ActionError("error.maintainer.required"));
			}
		
			if (_abstract == null || _abstract.length() == 0) {
				errors.add("abstract", new ActionError("error.abstract.required"));
			}
		
			String[] array = keywords != null ? keywords.split("\n") : new String[0];
			if (array.length == 0) {
				errors.add("keywords", new ActionError("error.keywords.required"));
			}
		
			return errors;
		}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.PlugIn#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
