/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.form.data.DataFeatureTypesForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataFeatureTypesAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
			
		DataFeatureTypesForm featureTypesForm = (DataFeatureTypesForm) form;
		
		String name = featureTypesForm.getName();
		String SRS = featureTypesForm.getSRS();
		String title = featureTypesForm.getTitle();
		String latLonBoundingBox = featureTypesForm.getLatlonBoundingBox();
		String keywords = featureTypesForm.getKeywords();
		String _abstract = featureTypesForm.get_abstract();
		
		String action = featureTypesForm.getAction();
		
		DataConfig dataConfig = (DataConfig) getDataConfig();			
		FeatureTypeConfig config = null;		
		
		if (action.equals("edit") || action.equals("submit")) {
			//config = (FeatureTypeConfig) dataConfig.getFeatureTypeConfig(featureTypesForm.getSelectedFeatureType());
		} else if (action.equals("new")) {
			config = new FeatureTypeConfig();
		}
		
		//If they push edit, simply forward them back so the information is repopulated.
		if (action.equals("edit")) {
			featureTypesForm.reset(mapping, request);
			return mapping.findForward("dataConfigFeatureTypes");
		}
		
		if (action.equals("delete")) {
			dataConfig.removeFeatureType(featureTypesForm.getSelectedFeatureType());
		} else {
			
			config.setAbstract(_abstract);
			config.setName(name);
			config.setSRS(Integer.parseInt(SRS));
			config.setTitle(title);
			// Errrrrrrrrrrrr config.setLatLongBBox(new Envelope());
			
			List list = new ArrayList();
			String[] array = keywords != null ? keywords.split("\n") : new String[0];
			
			for (int i = 0; i < array.length;i++) {
				list.add(array[i]);
			}
		
			config.setKeywords(list);
			
			//config.setKeywords()			
			dataConfig.addFeatureType(name, config);
		}
			
		featureTypesForm.reset(mapping, request);
			
		return mapping.findForward("dataConfigFeatureTypes");
	}

	DataStore aquireDataStore( String dataStoreID ) throws IOException{
		DataConfig dataConfig = getDataConfig();
		DataStoreConfig dataStoreConfig = dataConfig.getDataStore( dataStoreID );
		
		Map params = dataStoreConfig.getConnectionParams();
		
		return DataStoreFinder.getDataStore( params );
	}
	FeatureType getSchema( String dataStoreID, String typeName ) throws IOException{
		DataStore dataStore = aquireDataStore( dataStoreID );
		FeatureType type;
				
		return dataStore.getSchema( typeName );
	}
}
