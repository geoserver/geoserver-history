/*
 * Created on Dec 29, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.wfs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.form.wfs.WFSDescriptionForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WFSDescriptionAction extends ConfigAction {

	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {

			WFSDescriptionForm descriptionForm = (WFSDescriptionForm) form;

			String name = descriptionForm.getName();
			String title = descriptionForm.getTitle();
			String accessConstraints = descriptionForm.getAccessConstraints();
			String fees = descriptionForm.getFees();
			String maintainer = descriptionForm.getMaintainer();
			String keywords = descriptionForm.getKeywords();
			String _abstract = descriptionForm.get_abstract();

//			HttpSession session = request.getSession();
//			session.setAttribute("wfsDescription", form);
			
			WFSConfig config = getWFSConfig();
			config.setName(name);
			config.setTitle(title);
			config.setAccessConstraints(accessConstraints);
			config.setFees(fees);
			config.setMaintainer(maintainer);
			config.setAbstract(_abstract);
			
			String[] array = keywords != null ? keywords.split("\n") : new String[0];
			
			config.setKeywords(array);


			return mapping.findForward("welcome");
		}

}
