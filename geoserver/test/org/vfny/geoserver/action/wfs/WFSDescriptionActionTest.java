/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 * Created on Jan 3, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.wfs;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.DataSourceConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.PlugInConfig;
import org.vfny.geoserver.form.wfs.WFSDescriptionForm;


/**
 * DOCUMENT ME!
 *
 * @author User To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WFSDescriptionActionTest extends TestCase {
    private ActionMapping actionMapping;
    private WFSDescriptionForm form;
    WFSDescriptionAction action;

    /**
     * Constructor for WFSDescriptionActionTest.
     *
     * @param arg0
     */
    public WFSDescriptionActionTest(String arg0) {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        final ServletContext context = new ServletContext() {
                Map map = new HashMap();

                public Object getAttribute(String key) {
                    return map.get(key);
                }

                public Enumeration getAttributeNames() {
                    return Collections.enumeration(map.keySet());
                }

                public ServletContext getContext(String arg0) {
                    return null;
                }

                public String getInitParameter(String arg0) {
                    return null;
                }

                public Enumeration getInitParameterNames() {
                    return null;
                }

                public int getMajorVersion() {
                    return 0;
                }

                public String getMimeType(String arg0) {
                    return null;
                }

                public int getMinorVersion() {
                    return 0;
                }

                public RequestDispatcher getNamedDispatcher(String arg0) {
                    return null;
                }

                public String getRealPath(String arg0) {
                    return null;
                }

                public RequestDispatcher getRequestDispatcher(String arg0) {
                    return null;
                }

                public URL getResource(String arg0)
                    throws MalformedURLException {
                    return null;
                }

                public InputStream getResourceAsStream(String arg0) {
                    return null;
                }

                public Set getResourcePaths(String arg0) {
                    return null;
                }

                public String getServerInfo() {
                    return null;
                }

                public Servlet getServlet(String arg0)
                    throws ServletException {
                    return null;
                }

                public String getServletContextName() {
                    return null;
                }

                public Enumeration getServletNames() {
                    return null;
                }

                public Enumeration getServlets() {
                    return null;
                }

                public void log(Exception arg0, String arg1) {
                }

                public void log(String arg0) {
                }

                public void log(String arg0, Throwable arg1) {
                }

                public void removeAttribute(String arg0) {
                }

                public void setAttribute(String key, Object value) {
                    map.put(key, value);
                }
            };

        ActionServlet actionServlet = new ActionServlet() {
                public ServletContext getServletContext() {
                    return context;
                }
            };

        action = new WFSDescriptionAction();
        action.setServlet(actionServlet);

        form = new WFSDescriptionForm();

        //form.init( actionServlet, null );
        form.setServlet(actionServlet);
        form.reset(null, null);

        actionMapping = new ActionMapping();

        ModuleConfig moduleConfig = new ModuleConfig() {
                public boolean getConfigured() {
                    // TODO Auto-generated method stub
                    return false;
                }

                public ControllerConfig getControllerConfig() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public void setControllerConfig(ControllerConfig cc) {
                    // TODO Auto-generated method stub
                }

                public String getPrefix() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public void setPrefix(String prefix) {
                    // TODO Auto-generated method stub
                }

                public String getActionMappingClass() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public void setActionMappingClass(String actionMappingClass) {
                    // TODO Auto-generated method stub
                }

                public void addActionConfig(ActionConfig config) {
                    // TODO Auto-generated method stub
                }

                public void addDataSourceConfig(DataSourceConfig config) {
                    // TODO Auto-generated method stub
                }

                public void addExceptionConfig(ExceptionConfig config) {
                    // TODO Auto-generated method stub
                }

                public void addFormBeanConfig(FormBeanConfig config) {
                    // TODO Auto-generated method stub
                }

                public void addForwardConfig(ForwardConfig config) {
                    // TODO Auto-generated method stub
                }

                public void addMessageResourcesConfig(
                    MessageResourcesConfig config) {
                    // TODO Auto-generated method stub
                }

                public void addPlugInConfig(PlugInConfig plugInConfig) {
                    // TODO Auto-generated method stub
                }

                public ActionConfig findActionConfig(String path) {
                    // TODO Auto-generated method stub
                    return null;
                }

                public ActionConfig[] findActionConfigs() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public DataSourceConfig findDataSourceConfig(String key) {
                    // TODO Auto-generated method stub
                    return null;
                }

                public DataSourceConfig[] findDataSourceConfigs() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public ExceptionConfig findExceptionConfig(String type) {
                    // TODO Auto-generated method stub
                    return null;
                }

                public ExceptionConfig[] findExceptionConfigs() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public FormBeanConfig findFormBeanConfig(String name) {
                    // TODO Auto-generated method stub
                    return null;
                }

                public FormBeanConfig[] findFormBeanConfigs() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public ForwardConfig findForwardConfig(String name) {
                    // TODO Auto-generated method stub
                    return null;
                }

                public ForwardConfig[] findForwardConfigs() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public MessageResourcesConfig findMessageResourcesConfig(
                    String key) {
                    // TODO Auto-generated method stub
                    return null;
                }

                public MessageResourcesConfig[] findMessageResourcesConfigs() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public PlugInConfig[] findPlugInConfigs() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public void freeze() {
                    // TODO Auto-generated method stub
                }

                public void removeActionConfig(ActionConfig config) {
                    // TODO Auto-generated method stub
                }

                public void removeExceptionConfig(ExceptionConfig config) {
                    // TODO Auto-generated method stub
                }

                public void removeDataSourceConfig(DataSourceConfig config) {
                    // TODO Auto-generated method stub
                }

                public void removeFormBeanConfig(FormBeanConfig config) {
                    // TODO Auto-generated method stub
                }

                public void removeForwardConfig(ForwardConfig config) {
                    // TODO Auto-generated method stub
                }

                public void removeMessageResourcesConfig(
                    MessageResourcesConfig config) {
                    // TODO Auto-generated method stub
                }
            };

        actionMapping.setModuleConfig(moduleConfig);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test for ActionForward execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public void testExecuteActionMappingActionFormHttpServletRequestHttpServletResponse()
        throws Exception {
        HttpServletResponse response = null;
        HttpServletRequest request = null;

        action.execute(actionMapping, form, request, response);
    }
}
