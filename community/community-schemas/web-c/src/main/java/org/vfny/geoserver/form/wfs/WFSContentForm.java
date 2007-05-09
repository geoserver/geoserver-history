/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.global.dto.WFSDTO;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author User To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WFSContentForm extends ActionForm {
    private boolean enabled;
    private String onlineResource;
    private String describeURL;
    private int serviceLevel;
    private String[] selectedFeatures;
    private String[] features;
    private boolean srsXmlStyle;
    private boolean srsXmlStyleChecked = false;
    private boolean citeConformanceHacks;
    private boolean citeConformanceHacksChecked = false;
    private boolean featureBounding;
    private boolean featureBoundingChecked = false;

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

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String[] getFeatures() {
        return features;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String[] getSelectedFeatures() {
        return selectedFeatures;
    }

    /**
     * DOCUMENT ME!
     *
     * @param strings
     */
    public void setFeatures(String[] strings) {
        features = strings;
    }

    /**
     * DOCUMENT ME!
     *
     * @param strings
     */
    public void setSelectedFeatures(String[] strings) {
        selectedFeatures = strings;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);

        enabledChecked = false;
        srsXmlStyleChecked = false;
        citeConformanceHacksChecked = false;
        featureBoundingChecked = false;

        ServletContext context = getServlet().getServletContext();
        WFSConfig config = (WFSConfig) context.getAttribute(WFSConfig.CONFIG_KEY);

        citeConformanceHacks = config.getCiteConformanceHacks();
        featureBounding = config.isFeatureBounding();

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

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ((serviceLevel != WFSDTO.BASIC) && (serviceLevel != WFSDTO.TRANSACTIONAL)
                && (serviceLevel != WFSDTO.COMPLETE)) {
            errors.add("serviceLevel", new ActionError("error.serviceLevel.invalid"));
        }

        if ((onlineResource == null) || onlineResource.equals("")) {
            errors.add("onlineResource", new ActionError("error.wfs.onlineResource.required"));
        } else {
            try {
                URL url = new URL(onlineResource);
            } catch (MalformedURLException badURL) {
                errors.add("onlineResource",
                    new ActionError("error.wfs.onlineResource.malformed", badURL));
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
     */
    public void setSrsXmlStyleChecked(boolean b) {
        srsXmlStyleChecked = b;
    }

    /**
     * Access serviceLevel property.
     *
     * @return Returns the serviceLevel.
     */
    public int getServiceLevel() {
        return serviceLevel;
    }

    /**
     * Set serviceLevel to serviceLevel.
     *
     * @param serviceLevel The serviceLevel to set.
     */
    public void setServiceLevel(int serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    /**
     * Whether the srs xml attribute should be in the EPSG:4326 (non-xml)
     * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326 style.
     *
     * @return <tt>true</tt> if the srs is reported with the xml style
     */
    public boolean isSrsXmlStyle() {
        return srsXmlStyle;
    }

    /**
     * Sets whether the srs xml attribute should be in the EPSG:4326 (non-xml)
     * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326 style.
     *
     * @param doXmlStyle whether the srs style should be xml or not.
     */
    public void setSrsXmlStyle(boolean doXmlStyle) {
        this.srsXmlStyleChecked = true;
        this.srsXmlStyle = doXmlStyle;
    }

    /**
     * turn on/off the citeConformanceHacks option.
     *
     * @param on
     */
    public void setCiteConformanceHacks(boolean on) {
        this.citeConformanceHacksChecked = true; //this function only gets called when the form has it checked...
        citeConformanceHacks = on;
    }

    /**
     * get the current value of the citeConformanceHacks
     *
     * @return
     */
    public boolean getCiteConformanceHacks() {
        return (citeConformanceHacks);
    }

    /**
     * get the current value of the citeConformanceHacksChecked (ie. was it in
     * the http form?)
     *
     * @return
     */
    public boolean getCiteConformanceHacksChecked() {
        return (citeConformanceHacksChecked);
    }

    /**
     * turn on/off the featureBounding option.
     *
     * @param on
     */
    public void setFeatureBounding(boolean on) {
        this.featureBoundingChecked = true; //this function only gets called when the form has it checked...
        featureBounding = on;
    }

    /**
     * get the current value of the featureBounding
     *
     * @return
     */
    public boolean isFeatureBounding() {
        return (featureBounding);
    }

    /**
     * get the current value of the featureBoundingChecked (ie. was it in the
     * http form?)
     *
     * @return
     */
    public boolean isFeatureBoundingChecked() {
        return (featureBoundingChecked);
    }
}
