/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.form.data.DataDataStoresForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataDataStoresAction extends ConfigAction {

	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
			
System.out.println("lalalala DataStoresAction.exexcute!");
			
		DataDataStoresForm dataStoresForm = (DataDataStoresForm) form;
		
		String dataStoreID = dataStoresForm.getDataStoreID();
		String namespace = dataStoresForm.getNamespace();
		String description = dataStoresForm.getDescription();
		String server = dataStoresForm.getServer();
		String port = dataStoresForm.getPort();
		String username = dataStoresForm.getUsername();
		String password = dataStoresForm.getPassword();
		
		String action = dataStoresForm.getAction();
		
		boolean enabled = dataStoresForm.isEnabled();
		if (dataStoresForm.isEnabledChecked() == false) {
			enabled = false;
		}
		
		DataConfig dataConfig = (DataConfig) getDataConfig();
		DataStoreConfig config = null;
		
		if (action.equals("edit") || action.equals("submit")) {
			config = (DataStoreConfig) dataConfig.getDataStore(dataStoresForm.getSelectedDataStore());
		} else if (action.equals("new")) {
			config = new DataStoreConfig();
		}
		
		//If they push edit, simply forward them back so the information is repopulated.
		if (action.equals("edit")) {
			System.out.println("edit selected for dataStore: " + dataStoresForm.getSelectedDataStore());
			dataStoresForm.reset(mapping, request);
			return mapping.findForward("dataConfigDataStores");
		}
		
		if (action.equals("delete")) {
			dataConfig.removeDataStore(dataStoresForm.getSelectedDataStore());
			System.out.println("Delete requested on " + dataStoresForm.getSelectedDataStore());
		} else {
			
			config.setId(dataStoreID);
			config.setEnabled(enabled);
			config.setNameSpaceId(namespace);
			config.setAbstract(description);
		
			//Do configuration parameters here.
		
			dataConfig.addDataStore(dataStoreID, config);
		}
			
		dataStoresForm.reset(mapping, request);				
		return mapping.findForward("dataConfigDataStores");
	}

}
