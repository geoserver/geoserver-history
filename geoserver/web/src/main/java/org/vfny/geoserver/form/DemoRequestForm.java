/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * <b>DemoRequestForm</b><br> Oct 7, 2005<br>
 * <b>Purpose:</b><br>
 * DemoForm collects the list of avialable requests for the demo.<p>Stores
 * the request & post for the demo page, to be used by the DemoAction.</p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author Brent Owens (The Open Planning Project)
 * @version
 */
public class DemoRequestForm extends ActionForm {
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.form");
    private String action;
    private String url;
    private String body;
    private String demo;
    private File[] dirs;
    List demoList;

    /**
     * Sets request & post based on file selection.
     *
     * @param arg0
     * @param request
     *
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);

        Set demoSet = new HashSet();
        demoSet.add("");

        File dataDir = GeoserverDataDirectory.getGeoserverDataDirectory();

        try {
            this.dirs = findDemoDirs(dataDir);

            for (int i = 0; i < dirs.length; i++) {
                File[] files = dirs[i].listFiles();

                for (int j = 0; j < files.length; j++) {
                    File file = files[j];

                    if (!file.isDirectory()) {
                        demoSet.add(file.getName());
                    }
                }
            }
        } catch (org.vfny.geoserver.global.ConfigurationException confE) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(new StringBuffer("Conf e: ").append(confE).toString());
            }

            // eat this, no demo dir, so we just don't get any demo requests.
        }

        demoList = new ArrayList(demoSet);
        Collections.sort(demoList);
    }

    private File[] findDemoDirs(File dataDir) throws ConfigurationException {
        List dirs = new ArrayList();
        File d = GeoserverDataDirectory.findConfigDir(new File("/"), "basicdemo/");

        if (d != null) {
            dirs.add(d);
        }

        d = GeoserverDataDirectory.findConfigDir(dataDir, "demo/");

        if (d != null) {
            dirs.add(d);
        }

        return (File[]) dirs.toArray(new File[dirs.size()]);
    }

    /**
     * Verifies that username is not null or empty. Could potentially
     * do the same for password later.
     *
     * @param mapping
     * @param request
     *
     * @return
     *
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the demo.
     */
    public String getDemo() {
        return demo;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @param demo The demo to set.
     */
    public void setDemo(String demo) {
        this.demo = demo;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the dir.
     */
    public File[] getDirs() {
        return dirs;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the demoList.
     */
    public List getDemoList() {
        return demoList;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the action.
     */
    public String getAction() {
        return action;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @param action The action to set.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the body.
     */
    public String getBody() {
        return body;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @param body The body to set.
     */
    public void setBody(String body) {
        this.body = body;
    }
}
