/*
 * Created on Jan 27, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.global;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.ContactConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.form.global.GeoServerConfigurationForm;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.UserContainer;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * GeoServerConfigurationSubmit purpose.
 * <p>
 * Description of GeoServerConfigurationSubmit ...
 * </p>
 *
 * <p>
 * Capabilities:
 * </p>
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * GeoServerConfigurationSubmit x = new GeoServerConfigurationSubmit(...);
 * </code></pre>
 *
 * @author User, Refractions Research, Inc.
 * @author $Author: cholmesny $ (last modification)
 * @version $Id$
 */
public class GeoServerConfigurationSubmit extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm incomingForm,
        UserContainer user, HttpServletRequest request, HttpServletResponse response) {
        GeoServerConfigurationForm form = (GeoServerConfigurationForm) incomingForm;
        int maxFeatures = form.getMaxFeatures();

        boolean verbose = form.isVerbose();

        if (form.isVerboseChecked() == false) {
            verbose = false;
        }

        int numDecimals = form.getNumDecimals();
        String stringCharset = form.getCharset();
        Charset charset;

        try {
            charset = Charset.forName(stringCharset);
        } catch (IllegalArgumentException uce) {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.badCharSet"));
            saveErrors(request, errors);

            return mapping.findForward("config.server");
        }

        String baseURL = form.getProxyBaseUrl();
        String schemaBaseURL = form.getSchemaBaseURL();
        String log4jConfigFile = form.getLog4jConfigFile();
        String adminUserName = form.getAdminUserName();
        String adminPassword = form.getAdminPassword();
        boolean verboseExceptions = form.isVerboseExceptions();

        if (form.isVerboseExceptionsChecked() == false) {
            verboseExceptions = false;
        }

        boolean suppressStdOutLogging = form.isSuppressStdOutLogging();

        if (!form.isSuppressStdOutLoggingChecked()) {
            suppressStdOutLogging = false;
        }

        String logLocation = form.getLogLocation();

        if ((logLocation != null) && "".equals(logLocation.trim())) {
            logLocation = null;
        }

        if (logLocation != null) {
            File f = null;

            try {
                f = GeoServer.getLogLocation(logLocation);
            } catch (IOException e) {
                ActionErrors errors = new ActionErrors();
                ActionError error = new ActionError("error.couldNotCreateFile",
                        f.getAbsolutePath(), e.getLocalizedMessage());
                errors.add(ActionErrors.GLOBAL_ERROR, error);
                saveErrors(request, errors);

                return mapping.findForward("config.server");
            }

            if (!f.canWrite()) {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.noWritePermission", logLocation));
                saveErrors(request, errors);

                return mapping.findForward("config.server");
            }
        }

        double jaiMemoryCapacity = form.getJaiMemoryCapacity();
        double jaiMemoryThreshold = form.getJaiMemoryThreshold();
        int jaiTileThreads = form.getJaiTileThreads();
        int jaiTilePriority = form.getJaiTilePriority();
        boolean jaiRecycling = form.getJaiRecycling();

        if (form.isJaiRecyclingChecked() == false) {
            jaiRecycling = false;
        }

        boolean imageIOCache = form.getImageIOCache();

        if (form.isImageIOCacheChecked() == false) {
            imageIOCache = false;
        }

        boolean jaiJPEGNative = form.getJaiJPEGNative();

        if (form.isJaiJPEGNativeChecked() == false) {
            jaiJPEGNative = false;
        }

        boolean jaiPNGNative = form.getJaiPNGNative();

        if (form.isJaiPNGNativeChecked() == false) {
            jaiPNGNative = false;
        }

        
        boolean jaiMosaicNative = form.isJaiMosaicNative();
        if (form.isJaiMosaicNativeChecked()== false) {
        	jaiMosaicNative = false;
        }        
        
        String tileCache = form.getTileCache();

        if ((tileCache == null) || "".equals(tileCache.trim())) {
            tileCache = null;
        }

        GlobalConfig globalConfig = getGlobalConfig();
        globalConfig.setMaxFeatures(maxFeatures);
        globalConfig.setVerbose(verbose);
        globalConfig.setNumDecimals(numDecimals);
        globalConfig.setProxyBaseUrl(baseURL);
        globalConfig.setSchemaBaseUrl(schemaBaseURL);
        globalConfig.setCharSet(charset);
        globalConfig.setAdminUserName(adminUserName);
        globalConfig.setAdminPassword(adminPassword);
        globalConfig.setLog4jConfigFile(log4jConfigFile);
        globalConfig.setSuppressStdOutLogging(suppressStdOutLogging);
        globalConfig.setLogLocation(logLocation);
        globalConfig.setVerboseExceptions(verboseExceptions);
        globalConfig.setJaiMemoryCapacity(jaiMemoryCapacity);
        globalConfig.setJaiMemoryThreshold(jaiMemoryThreshold);
        globalConfig.setJaiTileThreads(jaiTileThreads);
        globalConfig.setJaiTilePriority(jaiTilePriority);
        globalConfig.setJaiRecycling(jaiRecycling);
        globalConfig.setImageIOCache(imageIOCache);
        globalConfig.setJaiJPEGNative(jaiJPEGNative);
        globalConfig.setJaiPNGNative(jaiPNGNative);
        globalConfig.setTileCache(tileCache);
        globalConfig.setJaiMosaicNative(jaiMosaicNative);

        ContactConfig contactConfig = globalConfig.getContact();
        contactConfig.setContactPerson(form.getContactPerson());
        contactConfig.setContactOrganization(form.getContactOrganization());
        contactConfig.setContactPosition(form.getContactPosition());
        contactConfig.setAddressType(form.getAddressType());
        contactConfig.setAddress(form.getAddress());
        contactConfig.setAddressCity(form.getAddressCity());
        contactConfig.setAddressCountry(form.getAddressCountry());
        contactConfig.setAddressPostalCode(form.getAddressPostalCode());
        contactConfig.setAddressState(form.getAddressState());
        contactConfig.setContactVoice(form.getContactVoice());
        contactConfig.setContactFacsimile(form.getContactFacsimile());
        contactConfig.setContactEmail(form.getContactEmail());
        globalConfig.setContact(contactConfig);
        getApplicationState().notifyConfigChanged();

        getServlet().getServletContext().setAttribute(GlobalConfig.CONFIG_KEY, globalConfig);

        return mapping.findForward("config");
    }
}
