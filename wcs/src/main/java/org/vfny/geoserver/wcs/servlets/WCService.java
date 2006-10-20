/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.servlets.ServiceStrategy;
import org.vfny.geoserver.servlets.SpeedStrategy;
import org.vfny.geoserver.wcs.WcsExceptionHandler;

/**
 * Base servlet for all Web Coverage Server requests.
 * 
 * <p>
 * Subclasses should supply the handler, request and response mapping for the
 * service they implement.
 * </p>
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id: WCService.java,v 0.1 Feb 15, 2005 12:18:00 PM $
 */
abstract public class WCService extends AbstractService {
    /**
	 * Constructor for WCS service.
	 * 
	 * @param request The service request being made (GetCaps,GetCoverage,...)
	 * @param wcs The WCS service reference.
	 */
    public WCService(String request, WCS wcs) {
    		super("WCS",request,wcs);
    }
    
    /**
     * @return The wcs service ref.
     */
    public WCS getWCS() {
    		return (WCS) getServiceRef();
    }
    
    /**
     * Sets the wcs service ref.
     * @param wcs
     */
    public void setWCS(WCS wcs) {
    		setServiceRef(wcs);
    }
    
    /**
     * Peforms service according to ServiceStrategy.
     * 
     * <p>
     * This method has very strict requirements, please see the class
     * description for the specifics.
     * </p>
     * 
     * <p>
     * It has a lot of try/catch blocks, but they are fairly necessary to
     * handle things correctly and to avoid as many ugly servlet responses, so
     * that everything is wrapped correctly.
     * </p>
     *
     * @param request The httpServlet of the request.
     * @param response The response to be returned.
     * @param serviceRequest The OGC request to service.
     *
     * @throws ServletException if the strategy can't be instantiated
     */
    protected void doService(HttpServletRequest request,
        HttpServletResponse response, Request serviceRequest)
        throws ServletException {
        LOGGER.info("handling request: " + serviceRequest);

        if (!isServiceEnabled(request)) {
        	try {
        		sendDisabledServiceError(response);
        	} catch(IOException e) {
        		LOGGER.log(Level.WARNING, "Error writing service unavailable response", e);
        	}
            return;
        }

        ServiceStrategy strategy = null;
        Response serviceResponse = null;

        try {
            strategy = (ServiceStrategy) ((ServiceStrategy) getApplicationContext().getBean("speedServiceStrategy")).clone();
            LOGGER.fine("strategy is: " + strategy.getId());
            serviceResponse = getResponseHandler();
        } catch (Throwable t) {
            sendError(request, response, t);

            return;
        }

        Map services = getApplicationContext().getBeansOfType(Service.class);
        Service s = null;
        for (Iterator itr = services.entrySet().iterator(); itr.hasNext();) {
        		Map.Entry entry = (Map.Entry) itr.next();
        		String id = (String) entry.getKey();
        		Service service = (Service) entry.getValue();
        		
        		if (id.equalsIgnoreCase(serviceRequest.getService())) {
        			s = service;
        			break;
        		}
        			
        }

        if (s == null) {
        		String msg = "No service found matching: " +
        			serviceRequest.getService();
        		sendError(request, response,new ServiceException ( msg ));
        		return;
        }
        	
        try {
            // execute request
            LOGGER.finer("executing request");
            serviceResponse.execute(serviceRequest);
            LOGGER.finer("execution succeed");
        } catch (ServiceException serviceException) {
            LOGGER.warning("service exception while executing request: "
                + serviceRequest + "\ncause: " + serviceException.getMessage());
            serviceResponse.abort(s);
            sendError(request, response, serviceException);

            return;
        } catch (Throwable t) {
            //we can safelly send errors here, since we have not touched response yet
            serviceResponse.abort(s);
            sendError(request, response, t);

            return;
        }

        OutputStream strategyOuput = null;

        //obtain the strategy output stream
        try {
            LOGGER.finest("getting strategy output");
            strategyOuput = strategy.getDestination(response);
            LOGGER.finer("strategy output is: "
                + strategyOuput.getClass().getName());

            String mimeType = serviceResponse.getContentType(s.getGeoServer());
            LOGGER.fine("mime type is: " + mimeType);
            response.setContentType(mimeType);

            String encoding = serviceResponse.getContentEncoding();

            if (encoding != null) {
                LOGGER.fine("content encoding is: " + encoding);
                response.setHeader("Content-Encoding", encoding);
            }
        } catch (SocketException socketException) {
            LOGGER.fine(
                "it seems that the user has closed the request stream: "
                + socketException.getMessage());

            // It seems the user has closed the request stream
            // Apparently this is a "cancel" and will quietly go away
            //
            // I will still give strategy and serviceResponse
            // a chance to clean up
            //
            serviceResponse.abort(s);
            strategy.abort();

            return;
        } catch (IOException ex) {
            serviceResponse.abort(s);
            strategy.abort();
            sendError(request, response, ex);

            return;
        }

        try {
            // gather response
            serviceResponse.writeTo(strategyOuput);
            strategyOuput.flush();
            strategy.flush();
        } catch (java.net.SocketException sockEx) { // user cancel
            serviceResponse.abort(s);
            strategy.abort();

            return;
        } catch (IOException ioException) { // strategyOutput error
            LOGGER.log(Level.SEVERE, "Error writing out " + ioException.getMessage(), ioException);
            serviceResponse.abort(s);
            strategy.abort();
            sendError(request, response, ioException);

            return;
        } catch (ServiceException writeToFailure) { // writeTo Failure
            serviceResponse.abort(s);
            strategy.abort();
            sendError(request, response, writeToFailure);

            return;
        } catch (Throwable help) { // This is an unexpected error(!)
        	help.printStackTrace();
            serviceResponse.abort(s);
            strategy.abort();
            sendError(request, response, help);

            return;
        }

        // Finish Response
        // I have moved closing the output stream out here, it was being
        // done by a few of the ServiceStrategy
        //
        // By this time serviceResponse has finished successfully
        // and strategy is also finished
        //
        try {
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (SocketException sockEx) { // user cancel
            LOGGER.warning("Could not send completed response to user:"
                + sockEx);

            return;
        } catch (IOException ioException) {
            // This is bad, the user did not get the completed response
            LOGGER.warning("Could not send completed response to user:"
                + ioException);

            return;
        }

        LOGGER.info("Service handled");
    }
    
    /**
     * a Web Coverage ServiceConfig exception handler
     *
     * @return an instance of WcsExceptionHandler
     */
    protected ExceptionHandler getExceptionHandler() {
        return WcsExceptionHandler.getInstance();
    }
    
    protected boolean isServiceEnabled(HttpServletRequest req){
    	return getWCS().isEnabled();
    }
}