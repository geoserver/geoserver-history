/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataStoreFactorySpi.Param;
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
	
	DataStore aquireDataStore( Map params ) throws IOException{
		return DataStoreFinder.getDataStore( params );
	}
	
	DataStoreFactorySpi aquireFactory( String dbtype  ){
		String description=null;
		if( dbtype.equals("postgis")) description="PostGIS spatial database";
		if( dbtype.equals("shapefile")) description="ESRI(tm) Shapefiles (*.shp)";
		if( dbtype.equals("oracle")) description="Oracle Spatial Database";		
		if( dbtype.equals("arcsde")) description="ESRI ArcSDE 8.x";								
		
		for( Iterator i= DataStoreFinder.getAvailableDataStores(); i.hasNext(); ){
			DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();
			if( factory.getDescription().equals( description ) ){
				return factory;			
			}								
		}
		return null;
	}
	Param find( DataStoreFactorySpi factory, String key ){
		return find( factory.getParametersInfo(), key );		
	}	
	
	Param find( Param params[], String key ){
		for( int i=0; i<params.length;i++){
			if( key.equalsIgnoreCase( params[i].key ) ){
				return params[i];
			}
		}
		return null;		
	}
}
