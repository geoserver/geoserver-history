/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.wfs;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.form.wfs.WFSContentForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WFSContentAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {


		WFSContentForm contentForm = (WFSContentForm) form;

		boolean enabled = contentForm.isEnabled();
		System.out.println("Action.execute: setting internal enabled =" + enabled);	
		if (contentForm.isEnabledChecked() == false) {
			enabled = false;
			System.out.println("Whoa, it was never actually checked. Setting to false.");	
		}
		String onlineResource = contentForm.getOnlineResource();
		String[] selectedFeatures = contentForm.getSelectedFeatures();
		String[] features = contentForm.getFeatures(); 
	
		WFSConfig config = getWFSConfig();
		System.out.println("Action.execute: setting config.setEnabled =" + enabled);		
		config.setEnabled(enabled);
		config.setOnlineResource(new URL(onlineResource));
		
		Set set = new TreeSet();
		if (selectedFeatures !=null) {
			for (int i = 0; i < selectedFeatures.length;i++) {
				set.add(selectedFeatures[i]);
			}
		}
		
		config.setEnabledFeatures(set);

		return mapping.findForward("wfsConfigDescription");
	}
}
