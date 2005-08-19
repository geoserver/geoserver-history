/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.wfs;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.global.dto.WFSDTO;



/**
 * DOCUMENT ME!
 *
 * @author User To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WFSContentForm extends ActionForm {

	/**
	 * 
	 * @uml.property name="enabled" multiplicity="(0 1)"
	 */
	private boolean enabled;

	/**
	 * 
	 * @uml.property name="onlineResource" multiplicity="(0 1)"
	 */
	private String onlineResource;

	/**
	 * 
	 * @uml.property name="describeURL" multiplicity="(0 1)"
	 */
	private String describeURL;

	/**
	 * 
	 * @uml.property name="serviceLevel" multiplicity="(0 1)"
	 */
	private int serviceLevel;

	/**
	 * 
	 * @uml.property name="selectedFeatures" multiplicity="(0 1)"
	 */
	private String[] selectedFeatures;

	/**
	 * 
	 * @uml.property name="features" multiplicity="(0 1)"
	 */
	private String[] features;

	/**
	 * 
	 * @uml.property name="srsXmlStyle" multiplicity="(0 1)"
	 */
	private boolean srsXmlStyle;

	/**
	 * 
	 * @uml.property name="srsXmlStyleChecked" multiplicity="(0 1)"
	 */
	private boolean srsXmlStyleChecked = false;

	/**
	 * 
	 * @uml.property name="enabledChecked" multiplicity="(0 1)"
	 */
	/*
	 * Because of the way that STRUTS works, if the user does not check the enabled box,
	 * or unchecks it, setEnabled() is never called, thus we must monitor setEnabled()
	 * to see if it doesn't get called. This must be accessible, as ActionForms need to
	 * know about it -- there is no way we can tell whether we are about to be passed to
	 * an ActionForm or not.
	 *
	 * Probably a better way to do this, but I can't think of one.
	 * -rgould
	 *
	 * TODO: Hey richard Jody here - Struts knows that boolean properties are
	 * not set if the user does nothing. Apparently that is why the reset
	 * method exists.
	 * Reset is called *every* time on ActionForm. Before the populate
	 * process has a go at things.
	 *
	 * The problem is that reset() retrieves the WFS's config enabled value
	 * and uses that to pre-populate the form. Thus, if they deselect it, setEnabled is
	 * never called, and enabled still remains true. The way I have done it isn't simple,
	 * but it works just fine.
	 */
	private boolean enabledChecked = false;


    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean isEnabled() {

        return enabled;
    }

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 * 
	 * @uml.property name="onlineResource"
	 */
	public String getOnlineResource() {
		return onlineResource;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 * 
	 * @uml.property name="describeURL"
	 */
	public void setDescribeURL(String string) {
		describeURL = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param b
	 * 
	 * @uml.property name="enabled"
	 */
	public void setEnabled(boolean b) {
		enabledChecked = true;
		//System.out.println("setEnabled: enabledCheck/Enabled now " + b);
		enabled = b;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 * 
	 * @uml.property name="onlineResource"
	 */
	public void setOnlineResource(String string) {
		onlineResource = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 * 
	 * @uml.property name="features"
	 */
	public String[] getFeatures() {
		return features;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 * 
	 * @uml.property name="selectedFeatures"
	 */
	public String[] getSelectedFeatures() {
		return selectedFeatures;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param strings
	 * 
	 * @uml.property name="features"
	 */
	public void setFeatures(String[] strings) {
		features = strings;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param strings
	 * 
	 * @uml.property name="selectedFeatures"
	 */
	public void setSelectedFeatures(String[] strings) {
		selectedFeatures = strings;
	}


    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);

        enabledChecked = false;
        srsXmlStyleChecked = false;

        ServletContext context = getServlet().getServletContext();
        WFSConfig config = (WFSConfig) context.getAttribute(WFSConfig.CONFIG_KEY);
        
        serviceLevel = config.getServiceLevel();
        this.enabled = config.isEnabled();
        this.srsXmlStyle = config.isSrsXmlStyle();

        URL url = config.getOnlineResource();

        if (url != null) {
            this.onlineResource = url.toString();
        } else {
            this.onlineResource = "";
        }
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (serviceLevel != WFSDTO.BASIC &&
            serviceLevel != WFSDTO.TRANSACTIONAL &&
            serviceLevel != WFSDTO.COMPLETE) {
            errors.add("serviceLevel", new ActionError("error.serviceLevel.invalid"));
        }
        
        if (onlineResource == null || onlineResource.equals("")) {
        	errors.add("onlineResource", new ActionError("error.wfs.onlineResource.required"));
        } else {
        	try {
                URL url = new URL(onlineResource);
            } catch (MalformedURLException badURL) {
                errors.add("onlineResource", new ActionError("error.wfs.onlineResource.malformed", badURL));
            }
        }
        
        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean isEnabledChecked() {
        return enabledChecked;
    }

	/**
	 * DOCUMENT ME!
	 * 
	 * @param b
	 * 
	 * @uml.property name="enabledChecked"
	 */
	public void setEnabledChecked(boolean b) {
		enabledChecked = b;
	}

    
	/**
	 * DOCUMENT ME!
	 *
	 * @return
	 */
	public boolean isSrsXmlStyleChecked() {
		return srsXmlStyleChecked;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param b
	 * 
	 * @uml.property name="srsXmlStyleChecked"
	 */
	public void setSrsXmlStyleChecked(boolean b) {
		srsXmlStyleChecked = b;
	}

	/**
	 * Access serviceLevel property.
	 * 
	 * @return Returns the serviceLevel.
	 * 
	 * @uml.property name="serviceLevel"
	 */
	public int getServiceLevel() {
		return serviceLevel;
	}

	/**
	 * Set serviceLevel to serviceLevel.
	 * 
	 * @param serviceLevel The serviceLevel to set.
	 * 
	 * @uml.property name="serviceLevel"
	 */
	public void setServiceLevel(int serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	
	/**
	 * Whether the srs xml attribute should be in the EPSG:4326 (non-xml)
	 * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326
	 * style.  
	 *
	 * @return <tt>true</tt> if the srs is reported with the xml style
	 */
	public boolean isSrsXmlStyle() {
		return srsXmlStyle;
	}

	/**
	 * Sets whether the srs xml attribute should be in the EPSG:4326 (non-xml)
	 * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326
	 * style.  
	 * 
	 * @param doXmlStyle whether the srs style should be xml or not.
	 * 
	 * @uml.property name="srsXmlStyle"
	 */
	public void setSrsXmlStyle(boolean doXmlStyle) {
		this.srsXmlStyleChecked = true;
		this.srsXmlStyle = doXmlStyle;
	}

}
