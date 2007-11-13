/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * <b>DemoRequestForm</b><br>
 * Oct 7, 2005<br>
 *
 * <b>Purpose:</b><br>
 * DemoForm collects the list of avialable requests for the demo.
 * <p>
 * Stores the request & post for the demo page, to be used by the DemoAction.
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author Brent Owens (The Open Planning Project)
 * @version
 */
public class DemoRequestForm extends ActionForm {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.form");
    private String action;
    private String url;
    private String body;
    private String demo;
    private String username;
    private String password;
    private File dir;
    List demoList;

    /**
     * Sets request & post based on file selection.
     *
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     *
     * @param arg0
     * @param request
     */
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);

        ServletContext context = getServlet().getServletContext();
        demoList = new ArrayList();
        demoList.add("");

        //DJB: changed this for geoserver_data_dir 
        // this.dir = new File(context.getRealPath("/data/demo"));
        try {
            this.dir = GeoserverDataDirectory.findCreateConfigDir("demo/");

            //commented out, findConfigDir does this for us.    
            //if( dir.exists() && dir.isDirectory() ){
            File[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                if (!file.isDirectory()) {
                    demoList.add(file.getName());
                }
            }
        } catch (org.vfny.geoserver.global.ConfigurationException confE) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(new StringBuffer("Conf e: ").append(confE).toString());
            }

            //eat this, no demo dir, so we just don't get any demo requests.
        }

        Collections.sort(demoList);
    }

    /**
     *
     * Verifies that username is not null or empty.
     * Could potentially do the same for password later.
     *
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     *
     * @param mapping
     * @param request
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    /**
     * @return Returns the demo.
     */
    public String getDemo() {
        return demo;
    }

    /**
     * @param demo The demo to set.
     */
    public void setDemo(String demo) {
        this.demo = demo;
    }

    /**
     * @return Returns the dir.
     */
    public File getDir() {
        return dir;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Returns the demoList.
     */
    public List getDemoList() {
        return demoList;
    }

    /**
     * @return Returns the action.
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action The action to set.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return Returns the body.
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body The body to set.
     */
    public void setBody(String body) {
        this.body = body;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
