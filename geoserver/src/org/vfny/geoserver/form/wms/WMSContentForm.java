/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.wms;

import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.WMSConfig;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WMSContentForm extends ActionForm {
	
	private boolean enabled;
	private String onlineResource;
	private String updateTime;
	private String[] selectedFeatures;
	private String[] features; 

	/*
	 * Because of the way that STRUTS works, if the user does not check the enabled box,
	 * or unchecks it, setEnabled() is never called, thus we must monitor setEnabled()
	 * to see if it doesn't get called. This must be accessible, as ActionForms need to
	 * know about it -- there is no way we can tell whether we are about to be passed to
	 * an ActionForm or not.
	 * 
	 * Probably a better way to do this, but I can't think of one.
	 * -rgould
	 */
	private boolean enabledChecked = false; 
	
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
	 * @param b
	 */
	public void setEnabled(boolean b) {
		enabledChecked = true;
		System.out.println("setEnabled: enabledCheck/Enabled now " + b);
		enabled = b;
	}


	/**
	 * @param string
	 */
	public void setOnlineResource(String string) {
		onlineResource = string;
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
	
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		
		super.reset(arg0, arg1);
		
		enabledChecked = false;		
		
		ServletContext context = getServlet().getServletContext();
		WMSConfig config =
			(WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);
			
		this.enabled = config.isEnabled();
		
		URL url = config.getOnlineResource();
		if (url != null) {
			this.onlineResource = url.toString();
		} else {
			this.onlineResource = "";
		}

		this.updateTime = config.getUpdateTime();
	
		Set featureSet = config.getEnabledFeatures();
		this.features = new String[featureSet.size()];
		
		Iterator iter = featureSet.iterator();
		int counter = 0;		
		while (iter.hasNext()) {
			String featureTypeName = (String) iter.next();
			features[counter] = featureTypeName;
			counter++;
		}
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		return errors;
	}	
	/**
	 * @return
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param string
	 */
	public void setUpdateTime(String string) {
		updateTime = string;
	}

	/**
	 * @return
	 */
	public boolean isEnabledChecked() {
		return enabledChecked;
	}

	/**
	 * @param b
	 */
	public void setEnabledChecked(boolean b) {
		enabledChecked = b;
	}

}	
