/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.form.data.DataAttributeTypesSelectForm;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataAttributeTypesSelectAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping,
        ActionForm incomingForm, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        DataAttributeTypesSelectForm form = (DataAttributeTypesSelectForm) incomingForm;
        String action = form.getButtonAction();

        DataConfig dataConfig = (DataConfig) getServlet().getServletContext()
                                                 .getAttribute(DataConfig.CONFIG_KEY);
        FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession()
                                                                .getAttribute(DataConfig.SELECTED_FEATURE_TYPE);
        AttributeTypeInfoConfig config = ftConfig.getAttributeFromSchema(form
                .getSelectedAttributeType());

        //SAVE SELECTED ATTRIBUTE AND FORWARD TO EDITOR
        if (action.equals("edit")) {
            request.getSession().setAttribute(DataConfig.SELECTED_ATTRIBUTE_TYPE,
                config);
            form.reset(mapping, request);

            return mapping.findForward("dataConfigFeatureTypes");
        }

        if (action.equals("delete")) {
            request.getSession().removeAttribute(DataConfig.SELECTED_ATTRIBUTE_TYPE);

            List list = ftConfig.getSchemaAttributes();
            list.remove(config);
            ftConfig.setSchemaAttributes(list);

            return mapping.findForward("dataConfigFeatureTypes");
        }

        if (action.equals("moveUp") || action.equals("moveDown")) {
            int direction = 0;

            if (action.equals("moveUp")) {
                direction = -1;
            } else if (action.equals("moveDown")) {
                direction = 1;
            }

            List list = ftConfig.getSchemaAttributes();

            //the index of the object to be moved;
            int targetIndex = list.indexOf(config);
            System.out.println("TargetIndex:" + targetIndex);

            if ((targetIndex == 0) && action.equals("moveUp")) {
                return mapping.findForward("dataConfigFeatureTypes");
            }

            if ((targetIndex == (list.size() - 1)) && action.equals("moveDown")) {
                return mapping.findForward("dataConfigFeatureTypes");
            }

            //retrieve the object currently where this one wants to go
            Object temp = list.get(targetIndex + direction);

            list.set(targetIndex + direction, config);
            list.set(targetIndex, temp);

            return mapping.findForward("dataConfigFeatureTypes");
        }

        throw new ServletException(
            "Action must equal either 'edit', 'delete', 'moveUp' or 'moveDown'");
    }
}
