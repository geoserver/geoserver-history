/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.action.data.DataStoreUtils;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataAttributeTypesNewForm extends ActionForm {
	
	String selectedNewAttributeType;
	
	//we must save the request so getNewAttributeTypes can function
	HttpServletRequest request;
	
	public void reset(ActionMapping arg0, HttpServletRequest request) {
		super.reset(arg0, request);
		
		System.out.println("@@@@@@@@@@@@@@@@@@@@atNewForm reset");
		
		this.request = request;		
		selectedNewAttributeType="";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}
	/**
	 * @return Returns the selectedNewFeatureType.
	 */
	public String getSelectedNewAttributeType() {
		return selectedNewAttributeType;
	}

	/**
	 * @param selectedNewFeatureType The selectedNewFeatureType to set.
	 */
	public void setSelectedNewAttributeType(String selectedNewFeatureType) {
		this.selectedNewAttributeType = selectedNewFeatureType;
	}
	
	public SortedSet getNewAttributeTypes () throws IOException {
		
		ServletContext context = getServlet().getServletContext();
		DataConfig config =
			(DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);
		System.out.println("HATE LEVEL 1");
		FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession().getAttribute(DataConfig.SELECTED_FEATURE_TYPE);
		String dataStoreID = ftConfig.getDataStoreId();
		DataStoreConfig dsConfig = config.getDataStore(dataStoreID);
		System.out.println("HATE LEVEL 2");
		DataStoreFactorySpi dsFactory = dsConfig.getFactory();
		System.out.println("HATE LEVEL 3");
		Map params = DataStoreUtils.toConnectionParams(dsFactory, dsConfig.getConnectionParams());
		System.out.println("HATE LEVEL 4");	
		DataStore dataStore = DataStoreUtils.aquireDataStore(params);
System.out.println("BORKBORKNBOBRKHJROJFALDKJFLAKD");
		
		
		//int index = selectedFeatureType.indexOf(dataStoreID+".");
		//String attributeTypeName = selectedFeatureType.substring(index);
		//System.out.println("ATNew::getNewAttributeTypes -- index = " +index+", substr = " +selectedNewAttributeType);
		FeatureType featureType = dataStore.getSchema(ftConfig.getName());
        
		return Collections.unmodifiableSortedSet(new TreeSet(
				Arrays.asList(featureType.getAttributeTypes())
		));
	}

}
