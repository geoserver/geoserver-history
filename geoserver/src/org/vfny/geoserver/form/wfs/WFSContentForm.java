/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.wfs;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WFSContentForm extends ActionForm{// implements PlugIn {
	
	private String serviceType;
	private boolean enabled;
	private String onlineResource;
	private String URL;
	private String describeURL;
	private String[] selectedFeatures;
	private String[] features = {"bork1", "bork2", "bork3"}; 

	/* (non-Javadoc)
	 * @see org.apache.struts.action.PlugIn#destroy()
	 */
	/*public void destroy() {
		// TODO Auto-generated method stub

	}*/

	/* (non-Javadoc)
	 * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
	 */
	/*public void init(ActionServlet servlet, ModuleConfig config)
		throws ServletException {
		// TODO Auto-generated method stub

	}*/

	/**
	 * @return
	 */
	public String getDescribeURL() {
		return describeURL;
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}



	/**
	 * @return
	 */
	public String getOnlineResource() {
		return onlineResource;
	}

	/**
	 * @return
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @return
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param string
	 */
	public void setDescribeURL(String string) {
		describeURL = string;
	}

	/**
	 * @param b
	 */
	public void setEnabled(boolean b) {
		enabled = b;
	}


	/**
	 * @param string
	 */
	public void setOnlineResource(String string) {
		onlineResource = string;
	}

	/**
	 * @param string
	 */
	public void setServiceType(String string) {
		serviceType = string;
	}

	/**
	 * @param string
	 */
	public void setURL(String string) {
		URL = string;
	}

	/**
	 * @return
	 */
	public String[] getFeatures() {
		return features;
	}

	/**
	 * @return
	 */
	public String[] getSelectedFeatures() {
		return selectedFeatures;
	}

	/**
	 * @param strings
	 */
	public void setFeatures(String[] strings) {
		features = strings;
	}

	/**
	 * @param strings
	 */
	public void setSelectedFeatures(String[] strings) {
		selectedFeatures = strings;
	}

}
