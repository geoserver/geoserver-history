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

import javax.servlet.ServletContext;
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
            
        ServletContext context = getServlet().getServletContext();
			
System.out.println("lalalala DataStoresAction.exexcute!");
			
		DataDataStoresEditorForm dataStoresForm = (DataDataStoresEditorForm) form;
		
		String dataStoreID = dataStoresForm.getDataStoreID();
		String namespace = dataStoresForm.getNamespace();
		String description = dataStoresForm.getDescription();
        
        String selectedDataStoreType = dataStoresForm.getSelectedDataStoreType();

		// After extracting params into a map
		Map aMap = new HashMap();
		
		// Test to see if they work
		DataStoreFinder.getDataStore( aMap );
						
		String action = dataStoresForm.getAction();
		
		boolean enabled = dataStoresForm.isEnabled();
		if (dataStoresForm.isEnabledChecked() == false) {
			enabled = false;
		}
		
		
		
		DataConfig dataConfig = (DataConfig) getDataConfig();
		DataStoreConfig config = null;
		System.out.println("[DataStoresAction]: action: "+action+", selectedDS: "+dataStoresForm.getSelectedDataStore());		
		if (action.equals("edit") || action.equals("submit")) {
			config = (DataStoreConfig) dataConfig.getDataStore(dataStoresForm.getSelectedDataStore());
            
            //if the config comes back null, we are creating a new DataStore.
            if (config == null) {
                config = new DataStoreConfig(selectedDataStoreType);
            }
		} /* else if (action.equals("new")) {
            System.out.println("### NEW ### requested, reset, forward, dsType: " + selectedDataStoreType);
            //Return them back to the form page so they can create a new dataStore.
            
            context.removeAttribute("selectedDataStore");
            dataStoresForm.setNewDataStore(true);
            dataStoresForm.setSelectedDataStoreType(selectedDataStoreType);
            dataStoresForm.reset(mapping, request);
            dataStoresForm.setAction("new");
            return mapping.findForward("dataConfigDataStores");
		}
		*/
		//If they push edit, simply forward them back so the information is repopulated.
/*		if (action.equals("edit")) {
			System.out.println("edit selected for dataStore: " + dataStoresForm.getSelectedDataStore());
			dataStoresForm.reset(mapping, request);
			return mapping.findForward("dataConfigDataStores");
		}
*/		
/*		if (action.equals("delete")) {
			dataConfig.removeDataStore(dataStoresForm.getSelectedDataStore());
			System.out.println("Delete requested on " + dataStoresForm.getSelectedDataStore());
		} else {
*/			
			config.setId(dataStoreID);
			config.setEnabled(enabled);
			config.setNameSpaceId(namespace);
			config.setAbstract(description);
		
			//Do configuration parameters here.
		
			dataConfig.addDataStore(dataStoreID, config);
//		}
			
		dataStoresForm.reset(mapping, request);				
		return mapping.findForward("dataConfigDataStores");
	}
		
}
