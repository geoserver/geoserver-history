/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStoreFinder;

import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.form.data.DataDataStoresEditorForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataDataStoresEditorAction extends ConfigAction {

	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {

		DataDataStoresEditorForm dataStoresForm = (DataDataStoresEditorForm) form;
		
		String dataStoreID = dataStoresForm.getDataStoreId();
		String namespace = dataStoresForm.getNamespaceId();
		String description = dataStoresForm.getDescription();

        

		// After extracting params into a map
		Map aMap = new HashMap();
		
		// Test to see if they work
		DataStoreFinder.getDataStore( aMap );
						
		boolean enabled = dataStoresForm.isEnabled();
		if (dataStoresForm.isEnabledChecked() == false) {
			enabled = false;
		}
		
		DataConfig dataConfig = (DataConfig) getDataConfig();
		DataStoreConfig config = null;
		
		config = (DataStoreConfig) dataConfig.getDataStore(dataStoreID);

		config.setEnabled(enabled);
		config.setNameSpaceId(namespace);
		config.setAbstract(description);
		
		config.setConnectionParams(aMap);
		dataConfig.addDataStore(config);
        
        request.getSession().removeAttribute("selectedDataStoreId");			

		return mapping.findForward("dataConfigDataStores");
	}
		
}
