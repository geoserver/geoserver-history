/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.apache.commons.digester.Digester;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ModuleConfigFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;


public class MultipleActionServlet extends ActionServlet {
    /**
     *
     */
    private static final long serialVersionUID = -5222456011963238678L;

    /**
     * <p>Initialize this servlet.  Most of the processing has been factored into
     * support methods so that you can override particular functionality at a
     * fairly granular level.</p>
     *
     * @exception ServletException if we cannot configure ourselves correctly
     */
    public void init() throws ServletException {
        // Wraps the entire initialization in a try/catch to better handle
        // unexpected exceptions and errors to provide better feedback
        // to the developer
        try {
            initInternal();
            initOther();
            initServlet();

            getServletContext().setAttribute(Globals.ACTION_SERVLET_KEY, this);
            initModuleConfigFactory();

            // Initialize modules as needed
            ModuleConfig moduleConfig = initModuleConfig("", config);
            initModuleMessageResources(moduleConfig);
            initModuleDataSources(moduleConfig);
            initModulePlugIns(moduleConfig);
            moduleConfig.freeze();

            Enumeration names = getServletConfig().getInitParameterNames();

            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();

                if (!name.startsWith("config/")) {
                    continue;
                }

                String prefix = name.substring(6);
                moduleConfig = initModuleConfig(prefix, getServletConfig().getInitParameter(name));
                initModuleMessageResources(moduleConfig);
                initModuleDataSources(moduleConfig);
                initModulePlugIns(moduleConfig);
                moduleConfig.freeze();
            }

            this.initModulePrefixes(this.getServletContext());

            this.destroyConfigDigester();
        } catch (UnavailableException ex) {
            throw ex;
        } catch (Throwable t) {
            // The follow error message is not retrieved from internal message
            // resources as they may not have been able to have been 
            // initialized
            log.error("Unable to initialize Struts ActionServlet due to an "
                + "unexpected exception or error thrown, so marking the "
                + "servlet as unavailable.  Most likely, this is due to an "
                + "incorrect or missing library dependency.", t);
            throw new UnavailableException(t.getMessage());
        }
    }

    /**
     * <p>Initialize the module configuration information for the
     * specified module.</p>
     *
     * @param prefix Module prefix for this module
     * @param paths Comma-separated list of context-relative resource path(s)
     *  for this modules's configuration resource(s)
     *
     * @exception ServletException if initialization cannot be performed
     * @since Struts 1.1
     */
    protected ModuleConfig initModuleConfig(String prefix, String paths)
        throws ServletException {
        // :FIXME: Document UnavailableException? (Doesn't actually throw anything)
        if (log.isDebugEnabled()) {
            log.debug("Initializing module path '" + prefix + "' configuration from '" + paths
                + "'");
        }

        // Parse the configuration for this module
        ModuleConfigFactory factoryObject = ModuleConfigFactory.createFactory();
        ModuleConfig config = factoryObject.createModuleConfig(prefix);

        // Configure the Digester instance we will use
        Digester digester = initConfigDigester();

        // Process each specified resource path
        while (paths.length() > 0) {
            digester.push(config);

            String path = null;
            int comma = paths.indexOf(',');

            if (comma >= 0) {
                path = paths.substring(0, comma).trim();
                paths = paths.substring(comma + 1);
            } else {
                path = paths.trim();
                paths = "";
            }

            if (path.length() < 1) {
                break;
            }

            this.parseModuleConfigFile(digester, path);
        }

        getServletContext().setAttribute(Globals.MODULE_KEY + config.getPrefix(), config);

        // Force creation and registration of DynaActionFormClass instances
        // for all dynamic form beans we wil be using
        FormBeanConfig[] fbs = config.findFormBeanConfigs();

        for (int i = 0; i < fbs.length; i++) {
            if (fbs[i].getDynamic()) {
                fbs[i].getDynaActionFormClass();
            }
        }

        return config;
    }

    /**
     * <p>Parses one module config file.</p>
     *
     * @param digester Digester instance that does the parsing
     * @param path The path to the config file to parse.
     *
     * @throws UnavailableException if file cannot be read or parsed
     * @since Struts 1.2
     */
    protected void parseModuleConfigFile(Digester digester, String path)
        throws UnavailableException {
        InputStream input = null;

        try {
            Resource[] resources = WebApplicationContextUtils.getWebApplicationContext(getServletContext())
                                                             .getResources(path);
            final int length = resources.length;

            for (int i = 0; i < length; i++) {
                URL url = resources[i].getURL(); /*getServletContext().getResource(path)*/
                ;

                // If the config isn't in the servlet context, try the class loader
                // which allows the config files to be stored in a jar
                if (url == null) {
                    url = getClass().getResource(path);
                }

                if (url == null) {
                    String msg = internal.getMessage("configMissing", path);
                    log.error(msg);
                    throw new UnavailableException(msg);
                }

                InputSource is = new InputSource(url.toExternalForm());
                input = url.openStream();
                is.setByteStream(input);
                digester.parse(is);
            }
        } catch (MalformedURLException e) {
            handleConfigException(path, e);
        } catch (IOException e) {
            handleConfigException(path, e);
        } catch (SAXException e) {
            handleConfigException(path, e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new UnavailableException(e.getMessage());
                }
            }
        }
    }

    /**
     * <p>Simplifies exception handling in the <code>parseModuleConfigFile</code> method.<p>
     * @param path
     * @param e
     * @throws UnavailableException as a wrapper around Exception
     */
    private void handleConfigException(String path, Exception e)
        throws UnavailableException {
        String msg = internal.getMessage("configParse", path);
        log.error(msg, e);
        throw new UnavailableException(msg);
    }
}
