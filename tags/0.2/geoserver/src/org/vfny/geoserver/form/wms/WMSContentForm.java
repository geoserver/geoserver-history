/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.form.wms;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.WMSConfig;


/**
 * DOCUMENT ME!
 *
 * @author User To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WMSContentForm extends ActionForm {
    private boolean enabled;
    private String onlineResource;

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
     */
    public String getOnlineResource() {
        return onlineResource;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
     */
    public void setEnabled(boolean b) {
        enabledChecked = true;
        enabled = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setOnlineResource(String string) {
        onlineResource = string;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);

        enabledChecked = false;

        ServletContext context = getServlet().getServletContext();
        WMSConfig config = (WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);

        this.enabled = config.isEnabled();

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

        if (onlineResource == null || onlineResource.equals("")) {
        	errors.add("onlineResource", 
                    new ActionError("error.wms.onlineResource.required", onlineResource));
        } else {
            try {
             URL url = new URL(onlineResource);   
            } catch (MalformedURLException badURL) {
             errors.add("onlineResource", 
                    new ActionError("error.wms.onlineResource.malformed", badURL));   
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
     */
    public void setEnabledChecked(boolean b) {
        enabledChecked = b;
    }
}
