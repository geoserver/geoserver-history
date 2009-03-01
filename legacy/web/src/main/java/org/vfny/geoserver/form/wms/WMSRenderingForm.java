/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.wms;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataAccessFactory.Param;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.form.data.FormUtils;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.DataStoreUtils;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.util.RequestsLegacy;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


public class WMSRenderingForm extends ActionForm {
    List svgRenderers;
    String svgRenderer;
    boolean svgAntiAlias;
    boolean globalWatermarking;
    String globalWatermarkingURL;
    int watermarkTransparency;
    int watermarkPosition;
    List intTypes;
    String allowInterpolation;

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
    private boolean svgAntiAliasChecked = false;
    private boolean globalWatermarkingChecked = false;

    public WMSRenderingForm() {
        svgRenderers = new ArrayList();
        svgRenderers.add(WMSConfig.SVG_SIMPLE);
        svgRenderers.add(WMSConfig.SVG_BATIK);
        svgAntiAlias = true;
        intTypes = new ArrayList();
        intTypes.add(WMSConfig.INT_NEAREST);
        intTypes.add(WMSConfig.INT_BIlINEAR);
        intTypes.add(WMSConfig.INT_BICUBIC);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        ServletContext context = getServlet().getServletContext();
        WMSConfig config = (WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);

        svgRenderer = config.getSvgRenderer();

        if (svgRenderer == null) {
            svgRenderer = WMSConfig.SVG_SIMPLE;
        }

        svgAntiAlias = config.getSvgAntiAlias();
        globalWatermarking = config.getGlobalWatermarking();
        globalWatermarkingURL = config.getGlobalWatermarkingURL();
        watermarkTransparency = config.getWatermarkTransparency();
        watermarkPosition = config.getWatermarkPosition();

        allowInterpolation = config.getAllowInterpolation();

        if (allowInterpolation == null) {
            allowInterpolation = WMSConfig.INT_BIlINEAR;
        }
    }

    public void setSvgRenderer(String svgRenderer) {
        this.svgRenderer = svgRenderer;
    }

    public String getSvgRenderer() {
        return svgRenderer;
    }

    public List getSvgRenderers() {
        return svgRenderers;
    }

    /**
     * @param svgAntiAlias anti alias hint.
     */
    public void setSvgAntiAlias(boolean svgAntiAlias) {
        svgAntiAliasChecked = true;
        this.svgAntiAlias = svgAntiAlias;
    }

    /**
     * @return The value of the anti aliasing rendering hint.
     */
    public boolean getSvgAntiAlias() {
        return svgAntiAlias;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean isSvgAntiAliasChecked() {
        return svgAntiAliasChecked;
    }

    /**
     * @param allowInterpolation interpolation rendering hint.
     */
    public void setAllowInterpolation(String allowInterpolation) {
        this.allowInterpolation = allowInterpolation;
    }

    /**
     * @return The value of the interpolation rendering hint.
     */
    public String getAllowInterpolation() {
        return allowInterpolation;
    }

    public List getIntTypes() {
        return intTypes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
     */
    public void setEnabledChecked(boolean svgAntiAliasChecked) {
        this.svgAntiAliasChecked = svgAntiAliasChecked;
    }

    public boolean isGlobalWatermarking() {
        return globalWatermarking;
    }

    public void setGlobalWatermarking(boolean globalWatermarking) {
        globalWatermarkingChecked = true;
        this.globalWatermarking = globalWatermarking;
    }

    /**
    * @return The value of the anti aliasing rendering hint.
    */
    public boolean getGlobalWatermarking() {
        return globalWatermarking;
    }

    public boolean isGlobalWatermarkingChecked() {
        return globalWatermarkingChecked;
    }

    public String getGlobalWatermarkingURL() {
        return globalWatermarkingURL;
    }

    public void setGlobalWatermarkingURL(String globalWatermarkingURL) {
        this.globalWatermarkingURL = globalWatermarkingURL;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        // Selected DataStoreConfig is in session
        //
        UserContainer user = RequestsLegacy.getUserContainer(request);
        if ((globalWatermarkingURL != null) && !"".equals(globalWatermarkingURL)) {
            URL url = null;

            try {
                // if this does not throw an exception then cool
                url = new URL(globalWatermarkingURL);
            } catch (MalformedURLException e) {
                //check for special case of file
                try {
                    if (GeoserverDataDirectory.findDataFile(globalWatermarkingURL).exists()) {
                        url = new URL("file://" + globalWatermarkingURL);
                        globalWatermarkingURL = "file://" + globalWatermarkingURL;
                    }
                } catch (MalformedURLException e1) {
                    //let this paramter die later
                }
            }
            
            if(url != null && (url.getProtocol() == null || url.getProtocol().equals("file"))) {
                //do a check to see if the shapefile url is valid, report 
                // an error if it does not 
                File file = GeoserverDataDirectory.findDataFile(globalWatermarkingURL);
                FormUtils.checkFileExistsAndCanRead(file, errors);
            } 
        }
        
        if(watermarkTransparency < 0 || watermarkTransparency > 100)
            errors.add("watermarkTransparency", new ActionError("error.watermark.transparency.invalid"));
        
        return errors;
    }

    public int getWatermarkTransparency() {
        return watermarkTransparency;
    }

    public void setWatermarkTransparency(int watermarkTransparency) {
        this.watermarkTransparency = watermarkTransparency;
    }

    public int getWatermarkPosition() {
        return watermarkPosition;
    }

    public void setWatermarkPosition(int watermarkPosition) {
        this.watermarkPosition = watermarkPosition;
    }
}
