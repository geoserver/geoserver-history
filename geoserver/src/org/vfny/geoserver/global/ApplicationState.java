/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.geotools.validation.dto.PlugInDTO;
import org.geotools.validation.dto.TestDTO;
import org.geotools.validation.dto.TestSuiteDTO;


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
 * @version $Id: ApplicationState.java,v 1.11 2004/02/09 23:29:41 dmzwiers Exp $
 */
public class ApplicationState implements PlugIn {
    /** The key used to store this value in the Web Container */
    public static final String WEB_CONTAINER_KEY = "GeoServer.ApplicationState";

    /** true if the configuration has been changed (but not applied) */
    private boolean wmsEdited;
    private boolean wfsEdited;
    private boolean dataEdited;
    private boolean validationEdited;
    
    /** true if the geoserve setup has been changed (but not saved) */
    private boolean wmsChanged;
    private boolean wfsChanged;
    private boolean dataChanged;
    private boolean validationChanged;

    /** magic, be very careful with this array. defined below in loadStatus() */
    private int[] geoserverStatus = new int[13];
    private Map geoserverNSErrors;
    private Map geoserverDSErrors;
    private Map geoserverVPErrors;
    
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
        return wmsEdited || wfsEdited || dataEdited || validationEdited;
    }
    public void setConfigChanged(boolean edited ){
    	wmsEdited = edited;
    	wfsEdited = edited;
    	dataEdited = edited;
    	validationEdited = edited;
    }
    /**
     * True if the user has changed GeoServer and not yet saved the changes.
     *
     * @return <code>true</code> if GeoServer has been changed (but not saved)
     */
    public boolean isGeoServerChanged() {
        return wmsChanged || wfsChanged || dataChanged || validationChanged;
    }    
    public void setGeoServerChanged(boolean changed){
    	wmsChanged = changed;
    	wfsChanged = changed;
    	dataChanged = changed;
    	validationChanged = changed;
    }
    /**
     * Notification that Global has been updated from XML config files
     */
    public void notifyLoadXML() {
    	// Correct, this represents a load into config from xml
        setConfigChanged( false );
    }

    /**
     * Notification that Global has been updated from Configuration
     */
    public void notifyToGeoServer() {
    	setGeoServerChanged( true );
        setConfigChanged( false );
    }

    /**
     * Notification that Global has been saved to XML config files.
     */
    public void notifiySaveXML() {
        setGeoServerChanged( false );
    }

    /**
     * Notification that the User has changed the Configuration
     */
    public void notifyConfigChanged() {
        setConfigChanged( true );
    }
    /** Q: what is this supposed to do? */
    public int getWfsGood(){
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0)){
    		loadStatus();
    	}
    	return geoserverStatus[1];
    }
    /** q: What foul manner of magic is this? */
    public int getWfsBad() {
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return geoserverStatus[2];
    }
    /** q: This does not make a lot of sense - did you want to consult both ConfigChanged and GeoServer changed? */
    public int getWfsDisabled() {
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return geoserverStatus[3];
    }
    /** Q: scary magic */
    public int getWmsGood(){
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return geoserverStatus[4];
    }
    /** Q: scary magic */
    public int getWmsBad() {
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return geoserverStatus[5];
    }
    /** Q: scary magic */
    public int getWmsDisabled() {
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return geoserverStatus[6];
    }
    
    public int getDataGood(){
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return geoserverStatus[7];
    }
    
    public int getDataBad() {
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return geoserverStatus[8];
    }
    
    public int getDataDisabled() {
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return geoserverStatus[9];
    }
    
    public int getGeoserverGood(){
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return (int)((geoserverStatus[1] + geoserverStatus[4] + geoserverStatus[7])/3.0);
    }
    
    public int getGeoserverBad() {
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return (int)((geoserverStatus[2] + geoserverStatus[5] + geoserverStatus[8])/3.0);
    }
    
    public int getGeoserverDisabled() {
    	if(geoserverStatus[0] != (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return (int)((geoserverStatus[3] + geoserverStatus[6] + geoserverStatus[9])/3.0);
    }
    
    /**
     * 
     * loadStatus purpose.
     * <p>
     * Magic occurs here, so be careful. This sets the values in the geoserverStatus array.
     * </p>
     * <p>
     * The array is broken into four blocks, [0],[1-3],[4-6],[7-9].
     * <ul>
     * <li>[0] -  The first block represtents the application state at the time
     *            of data creation. Use to test whether the array needs to be
     *            reloaded.</li> 
     * <li>[1-3] - wfs (working, broken, disabled) percentages</li>
     * <li>[4-6] - wms (working, broken, disabled) percentages</li>
     * <li>[7-9] - data (working, broken, disabled) percentages</li>
     * </ul>
     * </p>
     */
    private void loadStatus(){
    	// what does isGeoServerChanged() have to do with this
    	// and why don't you use (isGeoServerChanged() ? 3 : 0)
    	//
    	// or are you trying to indicate a bit mask:
    	// bit 1: isGeoServerChanged
    	// bit 2: isConfigChanged
    	//
    	// And all this madness is a cut and paste mistake?
    	geoserverStatus[0] = (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0);
    	Data dt = (Data) sc.getAttribute(Data.WEB_CONTAINER_KEY);
    	GeoValidator gv = (GeoValidator) sc.getAttribute(GeoValidator.WEB_CONTAINER_KEY);
    	if (dt == null || gv==null)
    		return;
    	
    	// setup geoserverNSErrors
    	geoserverNSErrors = dt.statusNamespaces();
    	
    	// setup geoserverDSErrors
    	geoserverDSErrors = dt.statusDataStores();

    	// setup geoserverVPErrors
    	Map tmpVP = gv.getErrors();
    	
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

    	//datastores
    	i = tmpVP.keySet().iterator();
    	g = 0;b = 0;d = 0;
    	while(i.hasNext()){
    		Object key = i.next();
    		Object o = tmpVP.get(key);
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
    		geoserverStatus[10] = 100;
    		geoserverStatus[11] = 0;
    		geoserverStatus[12] = 0;
    	}else{
    		geoserverStatus[10] = (int)(100.0*g/(g+b+d));
    		geoserverStatus[11] = (int)(100.0*b/(g+b+d));
    		geoserverStatus[12] = (int)(100.0*d/(g+b+d));
    	}
    	
    	geoserverVPErrors = new HashMap();
    	i = tmpVP.entrySet().iterator();
    	while(i.hasNext()){
    		Map.Entry e = (Map.Entry)i.next();
    		Object dto = e.getKey();
    		if(dto instanceof PlugInDTO){
    			geoserverVPErrors.put("PlugIn:"+((PlugInDTO)dto).getName(),e.getValue());
    		}else{if(dto instanceof TestDTO){
    			geoserverVPErrors.put("Test:"+((TestDTO)dto).getName(),e.getValue());
    		}else{if(dto instanceof TestSuiteDTO){
    			geoserverVPErrors.put("TestSuite:"+((TestSuiteDTO)dto).getName(),e.getValue());
    		}}}
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
    
    public Exception getValidationError(String key){
    	return (Exception) getValidationErrors().get( key );
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
    	if(geoserverNSErrors==null ||
    	   geoserverStatus[0] == (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
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
    	if(geoserverDSErrors==null || geoserverStatus[0] == (isConfigChanged() ? 1 : 0)+(isGeoServerChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
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
    
    /**
     * Validation Exceptions by obejct type : name where object type is one of TestSuite, Test, PlugIn
     * <p>
     * This only includes problems! If this map is null or isEmpty
     * status is "ready".
     * </p>
     * @return
     */
    public Map getValidationErrors(){
    	if(geoserverNSErrors==null ||
    			geoserverStatus[0] == (isGeoServerChanged() ? 1 : 0)+(isConfigChanged() ? 2 : 0)+(isValidationChanged() ? 4 : 0))
    		loadStatus();
    	return geoserverVPErrors;
    }
    /** Flattened for your JSP pleasure */
    public List getValidationErrorKeys(){
    	return new LinkedList(getValidationErrors().keySet());
    }
    /** Flattened for your JSP pleasure */
    public List getValidationErrorValues(){
    	return new LinkedList(getValidationErrors().values());
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
	/**
	 * @return Returns the dataChanged.
	 */
	public boolean isDataChanged() {
		return dataChanged;
	}
	/**
	 * @param dataChanged The dataChanged to set.
	 */
	public void setDataChanged(boolean dataChanged) {
		this.dataChanged = dataChanged;
	}
	/**
	 * @return Returns the dataEdited.
	 */
	public boolean isDataEdited() {
		return dataEdited;
	}
	/**
	 * @param dataEdited The dataEdited to set.
	 */
	public void setDataEdited(boolean dataEdited) {
		this.dataEdited = dataEdited;
	}
	/**
	 * @return Returns the validationChanged.
	 */
	public boolean isValidationChanged() {
		return validationChanged;
	}
	/**
	 * @param validationChanged The validationChanged to set.
	 */
	public void setValidationChanged(boolean validationChanged) {
		this.validationChanged = validationChanged;
	}
	/**
	 * @return Returns the validationEdited.
	 */
	public boolean isValidationEdited() {
		return validationEdited;
	}
	/**
	 * @param validationEdited The validationEdited to set.
	 */
	public void setValidationEdited(boolean validationEdited) {
		this.validationEdited = validationEdited;
	}
	/**
	 * @return Returns the wfsChanged.
	 */
	public boolean isWfsChanged() {
		return wfsChanged;
	}
	/**
	 * @param wfsChanged The wfsChanged to set.
	 */
	public void setWfsChanged(boolean wfsChanged) {
		this.wfsChanged = wfsChanged;
	}
	/**
	 * @return Returns the wfsEdited.
	 */
	public boolean isWfsEdited() {
		return wfsEdited;
	}
	/**
	 * @param wfsEdited The wfsEdited to set.
	 */
	public void setWfsEdited(boolean wfsEdited) {
		this.wfsEdited = wfsEdited;
	}
	/**
	 * @return Returns the wmsChanged.
	 */
	public boolean isWmsChanged() {
		return wmsChanged;
	}
	/**
	 * @param wmsChanged The wmsChanged to set.
	 */
	public void setWmsChanged(boolean wmsChanged) {
		this.wmsChanged = wmsChanged;
	}
	/**
	 * @return Returns the wmsEdited.
	 */
	public boolean isWmsEdited() {
		return wmsEdited;
	}
	/**
	 * @param wmsEdited The wmsEdited to set.
	 */
	public void setWmsEdited(boolean wmsEdited) {
		this.wmsEdited = wmsEdited;
	}
}
