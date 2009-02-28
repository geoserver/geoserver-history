/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config.web.tiles;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ComponentDefinition;
import org.apache.struts.tiles.Controller;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.DefinitionsUtil;
import org.apache.struts.tiles.NoSuchDefinitionException;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.struts.DelegatingActionProxy;
import org.springframework.web.struts.DelegatingActionUtils;
import org.springframework.web.struts.DelegatingRequestProcessor;
import org.springframework.web.struts.DelegatingTilesRequestProcessor;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Subclass of Struts' TilesRequestProcessor that looks up Spring-managed
 * Struts 1.1 Actions defined in ContextLoaderPlugIn's WebApplicationContext.
 *
 * <p>Behaves like
 * {@link DelegatingRequestProcessor DelegatingRequestProcessor},
 * but also provides the Tiles functionality of the original TilesRequestProcessor.
 * As there's just a single central class to customize in Struts, we have to provide
 * another subclass here, covering both the Tiles and the Spring delegation aspect.
 *
 * <p>The default implementation delegates to the DelegatingActionUtils
 * class as fas as possible, to reuse as much code as possible despite
 * the need to provide two RequestProcessor subclasses. If you need to
 * subclass yet another RequestProcessor, take this class as a template,
 * delegating to DelegatingActionUtils just like it.
 *
 * @author Juergen Hoeller
 * @since 1.0.2
 * @see DelegatingRequestProcessor
 * @see DelegatingActionProxy
 * @see DelegatingActionUtils
 */
public class MultipleDelegatingTilesRequestProcessor extends DelegatingTilesRequestProcessor {
    /**
     * Overloaded method from Struts' RequestProcessor.
     * Forward or redirect to the specified destination by the specified
     * mechanism.
     * This method catches the Struts' actionForward call. It checks if the
     * actionForward is done on a Tiles definition name. If true, process the
     * definition and insert it. If false, call the original parent's method.
     * @param request The servlet request we are processing.
     * @param response The servlet response we are creating.
     * @param forward The ActionForward controlling where we go next.
     *
     * @exception IOException if an input/output error occurs.
     * @exception ServletException if a servlet exception occurs.
     */
    protected void processForwardConfig(HttpServletRequest request, HttpServletResponse response,
        ForwardConfig forward) throws IOException, ServletException {
        // Required by struts contract
        if (forward == null) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("processForwardConfig(" + forward.getPath() + ", "
                + forward.getContextRelative() + ")");
        }

        // Try to process the definition.
        if (processTilesDefinition(forward.getPath(), /*forward.getContextRelative()*/
                    false, request, response)) {
            if (log.isDebugEnabled()) {
                log.debug("  '" + forward.getPath() + "' - processed as definition");
            }

            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("  '" + forward.getPath() + "' - processed as uri");
        }

        // forward doesn't contain a definition, let parent do processing
        super.processForwardConfig(request, response, forward);
    }

    /**
     * Process a Tile definition name.
     * This method tries to process the parameter <code>definitionName</code> as a definition name.
     * It returns <code>true</code> if a definition has been processed, or <code>false</code> otherwise.
     * Parameter <code>contextRelative</code> is not used in this implementation.
     *
     * @param definitionName Definition name to insert.
     * @param contextRelative Is the definition marked contextRelative ?
     * @param request Current page request.
     * @param response Current page response.
     * @return <code>true</code> if the method has processed uri as a definition name, <code>false</code> otherwise.
     */
    protected boolean processTilesDefinition(String definitionName, boolean contextRelative,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        // Do we do a forward (original behavior) or an include ?
        boolean doInclude = false;

        // Controller associated to a definition, if any
        Controller controller = null;

        // Computed uri to include
        String uri = null;

        ComponentContext tileContext = null;

        try {
            // Get current tile context if any.
            // If context exist, we will do an include
            tileContext = ComponentContext.getContext(request);
            doInclude = (tileContext != null);

            ComponentDefinition definition = null;

            // Process tiles definition names only if a definition factory exist,
            // and definition is found.
            if (definitionsFactory != null) {
                // Get definition of tiles/component corresponding to uri.
                try {
                    definition = definitionsFactory.getDefinition(definitionName, request,
                            getServletContext());
                } catch (NoSuchDefinitionException ex) {
                    // Ignore not found
                    log.debug("NoSuchDefinitionException " + ex.getMessage());
                }

                if (definition != null) { // We have a definition.
                                          // We use it to complete missing attribute in context.
                                          // We also get uri, controller.
                    uri = definition.getPath();
                    controller = definition.getOrCreateController();

                    if (tileContext == null) {
                        tileContext = new ComponentContext(definition.getAttributes());
                        ComponentContext.setContext(tileContext, request);
                    } else {
                        tileContext.addMissing(definition.getAttributes());
                    }
                }
            }

            // Process definition set in Action, if any.
            definition = DefinitionsUtil.getActionDefinition(request);

            if (definition != null) { // We have a definition.
                                      // We use it to complete missing attribute in context.
                                      // We also overload uri and controller if set in definition.

                if (definition.getPath() != null) {
                    uri = definition.getPath();
                }

                if (definition.getOrCreateController() != null) {
                    controller = definition.getOrCreateController();
                }

                if (tileContext == null) {
                    tileContext = new ComponentContext(definition.getAttributes());
                    ComponentContext.setContext(tileContext, request);
                } else {
                    tileContext.addMissing(definition.getAttributes());
                }
            }
        } catch (java.lang.InstantiationException ex) {
            log.error("Can't create associated controller", ex);

            throw new ServletException("Can't create associated controller", ex);
        } catch (DefinitionsFactoryException ex) {
            throw new ServletException(ex);
        }

        // Have we found a definition ?
        if (uri == null) {
            return false;
        }

        // Execute controller associated to definition, if any.
        if (controller != null) {
            try {
                controller.execute(tileContext, request, response, getServletContext());
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }

        // If request comes from a previous Tile, do an include.
        // This allows to insert an action in a Tile.
        if (log.isDebugEnabled()) {
            log.debug("uri=" + uri + " doInclude=" + doInclude);
        }

        if (doInclude) {
            doInclude(uri, request, response);
        } else {
            doForward(uri, request, response); // original behavior
        }

        return true;
    }

    /**
     * <p>Process a forward requested by this mapping (if any). Return
     * <code>true</code> if standard processing should continue, or
     * <code>false</code> if we have already handled this request.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param mapping The ActionMapping we are using
     */
    protected boolean processForward(HttpServletRequest request, HttpServletResponse response,
        ActionMapping mapping) throws IOException, ServletException {
        // Are we going to processing this request?
        String forward = mapping.getForward();

        if (forward == null) {
            return (true);
        }

        internalModuleRelativeForward(forward, request, response);

        return (false);
    }

    /**
     * <p>Process an include requested by this mapping (if any). Return
     * <code>true</code> if standard processing should continue, or
     * <code>false</code> if we have already handled this request.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param mapping The ActionMapping we are using
     */
    protected boolean processInclude(HttpServletRequest request, HttpServletResponse response,
        ActionMapping mapping) throws IOException, ServletException {
        // Are we going to processing this request?
        String include = mapping.getInclude();

        if (include == null) {
            return (true);
        }

        internalModuleRelativeInclude(include, request, response);

        return (false);
    }

    /**
     * <p>Do a module relative forward to specified URI using request dispatcher.
     * URI is relative to the current module. The real URI is compute by prefixing
     * the module name.</p>
     * <p>This method is used internally and is not part of the public API. It is
     * advised to not use it in subclasses. </p>
     *
     * @param uri Module-relative URI to forward to
     * @param request Current page request
     * @param response Current page response
     *
     * @since Struts 1.1
     */
    protected void internalModuleRelativeForward(String uri, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        // Construct a request dispatcher for the specified path
        uri = moduleConfig.getPrefix() + uri;

        // Delegate the processing of this request
        // :FIXME: - exception handling?
        if (log.isDebugEnabled()) {
            log.debug(" Delegating via forward to '" + uri + "'");
        }

        doForward(uri, request, response);
    }

    /**
     * <p>Do a module relative include to specified URI using request dispatcher.
     * URI is relative to the current module. The real URI is compute by prefixing
     * the module name.</p>
     * <p>This method is used internally and is not part of the public API. It is
     * advised to not use it in subclasses.</p>
     *
     * @param uri Module-relative URI to include
     * @param request Current page request
     * @param response Current page response
     *
     * @since Struts 1.1
     */
    protected void internalModuleRelativeInclude(String uri, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        // Construct a request dispatcher for the specified path
        uri = moduleConfig.getPrefix() + uri;

        // Delegate the processing of this request
        // FIXME - exception handling?
        if (log.isDebugEnabled()) {
            log.debug(" Delegating via include to '" + uri + "'");
        }

        doInclude(uri, request, response);
    }

    /**
     * <p>Do a forward to specified URI using a <code>RequestDispatcher</code>.
     * This method is used by all internal method needing to do a forward.</p>
     *
     * @param uri Context-relative URI to forward to
     * @param request Current page request
     * @param response Current page response
     * @since Struts 1.1
     */
    protected void doForward(String uri, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        // Unwrap the multipart request, if there is one.
        if (request instanceof MultipartRequestWrapper) {
            request = ((MultipartRequestWrapper) request).getRequest();
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher(uri);

        if (rd == null) {
            URL resource = WebApplicationContextUtils.getWebApplicationContext(getServletContext())
                                                     .getResource(uri).getURL();
            response.sendRedirect(resource.toString());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                getInternal().getMessage("requestDispatcher", uri));

            return;
        }

        rd.forward(request, response);
    }

    /**
     * <p>Do an include of specified URI using a <code>RequestDispatcher</code>.
     * This method is used by all internal method needing to do an include.</p>
     *
     * @param uri Context-relative URI to include
     * @param request Current page request
     * @param response Current page response
     * @since Struts 1.1
     */
    protected void doInclude(String uri, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        // Unwrap the multipart request, if there is one.
        if (request instanceof MultipartRequestWrapper) {
            request = ((MultipartRequestWrapper) request).getRequest();
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher(uri);

        if (rd == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                getInternal().getMessage("requestDispatcher", uri));

            return;
        }

        rd.include(request, response);
    }
}
