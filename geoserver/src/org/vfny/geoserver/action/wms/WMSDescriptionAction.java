/*
 * Created on Dec 29, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.wms;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.form.wms.WMSDescriptionForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WMSDescriptionAction extends ConfigAction {

	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {

			WMSDescriptionForm descriptionForm = (WMSDescriptionForm) form;

			String name = descriptionForm.getName();
			String title = descriptionForm.getTitle();
			String accessConstraints = descriptionForm.getAccessConstraints();
			String fees = descriptionForm.getFees();
			String maintainer = descriptionForm.getMaintainer();
			String keywords = descriptionForm.getKeywords();
			String _abstract = descriptionForm.get_abstract();

			WMSConfig config = getWMSConfig();
			config.setName(name);
			config.setTitle(title);
			config.setAccessConstraints(accessConstraints);
			config.setFees(fees);
			config.setMaintainer(maintainer);
			config.setAbstract(_abstract);
			
			String[] array = keywords != null ? keywords.split("\n") : new String[0];
			
			config.setKeywords(array);


			return mapping.findForward("wmsConfigDescription");
		}

}
