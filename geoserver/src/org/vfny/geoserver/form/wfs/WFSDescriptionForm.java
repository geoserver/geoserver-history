/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 * Created on Dec 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.wfs;

import java.util.ArrayList;
import java.util.List;

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
import org.vfny.geoserver.config.WFSConfig;


/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/**
 * DOCUMENT ME!
 *
 * @author User To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WFSDescriptionForm extends ActionForm implements PlugIn {
    private String name;
    private String title;
    private String accessConstraints;
    private String fees;
    private String maintainer;
    private String keywords;
    private String _abstract;


    public WFSDescriptionForm() {
    }
	/**
	 * DOCUMENT ME!
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param string
	 */
	public void setTitle(String string) {
		title = string;
	}
	/**
	 * @return
	 */
	public String get_abstract() {
		return _abstract;
	}

	/**
	 * @return
	 */
	public String getAccessConstraints() {
		return accessConstraints;
	}

	/**
	 * @return
	 */
	public String getFees() {
		return fees;
	}

	/**
	 * @return
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @return
	 */
	public String getMaintainer() {
		return maintainer;
	}

	/**
	 * @param string
	 */
	public void set_abstract(String string) {
		_abstract = string;
	}

	/**
	 * @param string
	 */
	public void setAccessConstraints(String string) {
		accessConstraints = string;
	}

	/**
	 * @param string
	 */
	public void setFees(String string) {
		fees = string;
	}

	/**
	 * @param list
	 */
	public void setKeywords(String string) {
		keywords = string;
	}

	/**
	 * @param string
	 */
	public void setMaintainer(String string) {
		maintainer = string;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */

/* (non-Javadoc)
 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
 */
public void reset(ActionMapping arg0, HttpServletRequest arg1) {
	super.reset(arg0, arg1);
	ServletContext context = getServlet().getServletContext();
	WFSConfig config =
		(WFSConfig) context.getAttribute("GeoServer.WFSConfig");
		
	name = config.getService().getName();
	this._abstract = config.getService().getAbstract();
	this.fees = config.getService().getFees();
			
}
/* (non-Javadoc)
 * @see org.apache.struts.action.PlugIn#destroy()
 */
public void destroy() {
	// TODO Auto-generated method stub
	
}
/* (non-Javadoc)
 * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
 */
public void init(ActionServlet actionServlet, ModuleConfig moduleConfig ) throws ServletException {
	// TODO Auto-generated method stub
	WFSConfig config = new WFSConfig();
	config.setDescribeUrl("http://localhost:8080/wfs");
	config.getService().setAbstract("Hello Richard? Testing? 1 2 3 Testing?");
	config.getService().setAccessConstraints("none");
	config.getService().setEnabled( true );
	config.getService().setFees("A small fish");
	List keywords = new ArrayList();
	keywords.add("GeoServer");
	keywords.add("Configuration");
	keywords.add("STRUTS");
	keywords.add("test");
	config.getService().setKeywords( keywords );
	config.getService().setMaintainer("Refractions Research");
	config.getService().setName("WFS");
	config.getService().setOnlineResource("http://vwfs.refractions.net/");
	config.getService().setTitle("Sample WFS Configuration");
	
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


}
