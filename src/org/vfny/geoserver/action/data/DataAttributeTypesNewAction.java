/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.form.data.DataAttributeTypesNewForm;
/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataAttributeTypesNewAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping,
			ActionForm incomingForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws IOException, ServletException {
		
        DataAttributeTypesNewForm form = (DataAttributeTypesNewForm) incomingForm;
        
		DataConfig dataConfig = (DataConfig) getServlet().getServletContext().getAttribute(DataConfig.CONFIG_KEY);
        FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession().getAttribute(DataConfig.SELECTED_FEATURE_TYPE);
        AttributeTypeInfoConfig config = (AttributeTypeInfoConfig) ftConfig.getAttributeFromSchema(form.getSelectedNewAttributeType());

        String dataStoreID = ftConfig.getDataStoreId();
        String featureTypeName = ftConfig.getName();
        
        DataStoreConfig dsConfig = dataConfig.getDataStore(dataStoreID);
        DataStore dataStore = dsConfig.findDataStore();
        
        FeatureType featureType = dataStore.getSchema(featureTypeName);        
        
		//Retrieve Selected one and populate and save it
        AttributeType attributeType = featureType.getAttributeType(form.getSelectedNewAttributeType());
		AttributeTypeInfoConfig atiConfig = new AttributeTypeInfoConfig(attributeType);
		
		request.getSession().setAttribute(DataConfig.SELECTED_ATTRIBUTE_TYPE, atiConfig);
		
		return mapping.findForward("dataConfigFeatureTypes");
	}
}
