/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


/**
 * This class represents the state of the GeoServer appliaction.
 * 
 * <p>
 * ApplicationState used by the state.jsp tile as a single view on the state of
 * the GeoServer application. This class may be extended in the future to
 * provide runtime statistics.
 * </p>
 * 
 * <p>
 * This class is not a bean - content is updated based on methods. As an
 * example consider the following State diagram:
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ApplicationState.java,v 1.4 2004/01/31 00:27:23 jive Exp $
 */
public class ApplicationState implements PlugIn {
    /** The key used to store this value in the Web Container */
    public static final String WEB_CONTAINER_KEY = "GeoServer.ApplicationState";

    /** true if the configuration has been changed (but not applied) */
    private boolean configChanged = false;

    /** true if the geoserve setup has been changed (but not saved) */
    private boolean geoServerChanged = false;

    /** magic, be very careful with this array. defined below in loadStatus() */
    private int[] geoserverStatus = new int[10];
    private Map geoserverNSErrors;
    private Map geoserverDSErrors;
    
    private ServletContext sc;
    /**
     * Clean up the Configuration State during application exit.
     * 
     * <p>
     * Since this class just holds data, no resources need to be released.
     * </p>
     *
     * @see org.apache.struts.action.PlugIn#destroy()
     */
    public void destroy() {
    }

    /**
     * Set up the ApplicationState during Application start up.
     * 
     * <p>
     * ApplicationState simply registers itself with the WEB_CONTAINER_KEY
     * ("GeoServer.ApplicationState") during start up.
     * </p>
     *
     * @param actionServlet ActionServlet representing the Application
     * @param moduleConfig Configuration used to set up this plug in
     *
     * @throws ServletException
     *
     * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet,
     *      org.apache.struts.config.ModuleConfig)
     */
    public void init(ActionServlet actionServlet, ModuleConfig moduleConfig)
        throws ServletException {
        actionServlet.getServletContext().setAttribute(WEB_CONTAINER_KEY, this);

        sc = actionServlet.getServletContext();
        geoserverStatus[0]=-1;
    }

    /**
     * True if the user has changed the Configuration and not yet applied them.
     *
     * @return <code>true</code> if Configuration needs changing.
     */
    public boolean isConfigChanged() {
        return configChanged;
    }

    /**
     * True if the user has changed GeoServer and not yet saved the changes.
     *
     * @return <code>true</code> if GeoServer has been changed (but not saved)
     */
    public boolean isGeoServerChanged() {
        return geoServerChanged;
    }

    /**
     * Notification that Global has been updated from XML config files
     */
    public void notifyLoadXML() {
        geoServerChanged = true;
        configChanged = false;
    }

    /**
     * Notification that Global has been updated from Configuration
     */
    public void notifyToGeoServer() {
        geoServerChanged = true;
        configChanged = false;
    }

    /**
     * Notification that Global has been saved to XML config files.
     */
    public void notifiySaveXML() {
        geoServerChanged = false;
    }

    /**
     * Notification that the User has changed the Configuration
     */
    public void notifyConfigChanged() {
        configChanged = true;
    }
    
    public int getWfsGood(){
    	if(geoserverStatus[0] != (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverStatus[1];
    }
    
    public int getWfsBad() {
    	if(geoserverStatus[0] != (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverStatus[2];
    }
    
    public int getWfsDisabled() {
    	if(geoserverStatus[0] != (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverStatus[3];
    }
    
    public int getWmsGood(){
    	if(geoserverStatus[0] != (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverStatus[4];
    }
    
    public int getWmsBad() {
    	if(geoserverStatus[0] != (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverStatus[5];
    }
    
    public int getWmsDisabled() {
    	if(geoserverStatus[0] != (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverStatus[6];
    }
    
    public int getDataGood(){
    	if(geoserverStatus[0] != (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverStatus[7];
    }
    
    public int getDataBad() {
    	if(geoserverStatus[0] != (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverStatus[8];
    }
    
    public int getDataDisabled() {
    	if(geoserverStatus[0] != (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverStatus[9];
    }
    
    /**
     * 
     * loadStatus purpose.
     * <p>
     * Magic occurs here, so be careful. This sets the values in the geoserverStatus array.
     * </p>
     * <p>
     * The array is broken into four blocks, [0],[1-3],[4-6],[7-9]. The first block represtents the application state at the time of data creation. Use to test whether the array needs to be reloaded. The next 3 blocks contain the data for wfs,wms and data modules. Each module has three portions: working, broken, and disabled. Each of these is represented as a percentage. 
     * </p>
     *
     */
    private void loadStatus(){
    	geoserverStatus[0] = (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0);
    	GeoServer gs = (GeoServer) sc.getAttribute(GeoServer.WEB_CONTAINER_KEY);
    	if (gs == null)
    		return;
    	
    	// setup geoserverNSErrors
    	geoserverNSErrors = gs.getData().statusNamespaces();
    	
    	// setup geoserverDSErrors
    	geoserverDSErrors = gs.getData().statusDataStores();
    	
    	int g = 0;
    	int b = 0;
    	int d = 0;
    	Iterator i = null;
    	
    	//featuretypes
    	i = geoserverNSErrors.keySet().iterator();
    	while(i.hasNext()){
    		Object key = i.next();
    		Object o = geoserverNSErrors.get(key);
    		if(o.equals(Boolean.TRUE)){
    			g++;
    			i.remove();
    			//geoserverNSErrors.remove(key);
    		}else{
    		if(o.equals(Boolean.FALSE)){
    			d++;
    			i.remove();
    			//geoserverNSErrors.remove(key);
    		}else{
    			b++;
    		}}
    	}
    	if(g+b+d==0){
    		geoserverStatus[7] = 100;
    		geoserverStatus[8] = 0;
    		geoserverStatus[9] = 0;
    	}else{
    		geoserverStatus[7] = (int)(100.0*g/(g+b+d));
    		geoserverStatus[8] = (int)(100.0*b/(g+b+d));
    		geoserverStatus[9] = (int)(100.0*d/(g+b+d));
    	}
    	
    	//datastores
    	i = geoserverDSErrors.keySet().iterator();
    	g = 0;b = 0;d = 0;
    	while(i.hasNext()){
    		Object key = i.next();
    		Object o = geoserverDSErrors.get(key);
    		if(o.equals(Boolean.TRUE)){
    			g++;
    			i.remove();
    			//geoserverDSErrors.remove(key);
    		}else{
    			if(o.equals(Boolean.FALSE)){
    				d++;
    				i.remove();
    				//geoserverDSErrors.remove(key);
    			}else{
    				b++;
    			}}
    	}

    	if(g+b+d==0){
    		geoserverStatus[1] = geoserverStatus[4] = 100;
    		geoserverStatus[2] = geoserverStatus[5] = 0;
    		geoserverStatus[3] = geoserverStatus[6] = 0;
    	}else{
    		geoserverStatus[1] = geoserverStatus[4] = (int)(100.0*g/(g+b+d));
    		geoserverStatus[2] = geoserverStatus[5] = (int)(100.0*b/(g+b+d));
    		geoserverStatus[3] = geoserverStatus[6] = (int)(100.0*d/(g+b+d));
    	}
    }
        
    public Exception getDataStoreError(String key){
    	return (Exception) getDataStoreErrors().get( key );
    }
    
    public Exception getNameSpaceError(String key){
    	return (Exception) getNameSpaceErrors().get( key );
    }
    
    public Exception getWFSError(String key){
    	return (Exception) getNameSpaceErrors().get( key );
    }
    
    public Exception getWMSError(String key){
    	return (Exception) getNameSpaceErrors().get( key );
    }
    
    /**
     * Namespace Exceptions by prefix:typeName.
     * <p>
     * This only includes problems! If this map is null or isEmpty
     * status is "ready".
     * </p>
     * @return
     */
    public Map getNameSpaceErrors(){
    	if(geoserverNSErrors==null || geoserverStatus[0] == (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverNSErrors;
    }
    /** Flattened for your JSP pleasure */
    public List getNameSpaceErrorKeys(){
    	return new LinkedList(getNameSpaceErrors().keySet());
    }
    /** Flattened for your JSP pleasure */
    public List getNameSpaceErrorValues(){
    	return new LinkedList(getNameSpaceErrors().values());
    }
    /**
     * DataStore Exceptions by dataStoreId:typeName
     * <p>
     * This only includes problems! If this map is null or isEmpty
     * status is "ready".
     * </p>
     * @return
     */
    public Map getDataStoreErrors(){
    	if(geoserverDSErrors==null || geoserverStatus[0] == (geoServerChanged ? 1 : 0)+(geoServerChanged ? 2 : 0))
    		loadStatus();
    	return geoserverDSErrors;
    }
    /** Flattened for your JSP pleasure */    
    public List getDataStoreErrorKeys(){
    	return new LinkedList(getDataStoreErrors().keySet());
    }
    /** Flattened for your JSP pleasure */    
    public List getDataStoreErrorValues(){
    	return new LinkedList(getDataStoreErrors().values());
    }
    
    public Map getWFSErrors(){
    	return getNameSpaceErrors();
    }
    
    public List getWFSErrorKeys(){
    	return getNameSpaceErrorKeys();
    }
    
    public Map getWMSErrors(){
    	return getNameSpaceErrors();
    }
    
    public List getWMSErrorKeys(){
    	return getNameSpaceErrorKeys();
    }
}
