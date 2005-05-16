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
package org.vfny.geoserver.form.wcs;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.WCSConfig;



/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class WCSContentForm extends ActionForm {
    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3977860674510534961L;
	private boolean enabled;
    private String onlineResource;
    private String describeURL;


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
     */
    public String getOnlineResource() {
        return onlineResource;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setDescribeURL(String string) {
        describeURL = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
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
     */
    public void setOnlineResource(String string) {
        onlineResource = string;
    }


    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);

        enabledChecked = false;

        ServletContext context = getServlet().getServletContext();
        WCSConfig config = (WCSConfig) context.getAttribute(WCSConfig.CONFIG_KEY);
        
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
     */
    public void setEnabledChecked(boolean b) {
        enabledChecked = b;
    }
 

}
