/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.data.DataStoreUtils;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataDataStoresEditorForm extends ActionForm {
	
	private String dataStoreID;
	private boolean enabled;
	private String namespace;
	private String description;
	
	//These are not stored in a single map so we can access them
	//easily from JSP page
	private List connectionParamKeys;
	private List connectionParamValues;
	
	private String selectedDataStore;
	private String selectedDataStoreType;
	
	private TreeSet dataStores;
	private TreeSet dataStoreTypes;
	private TreeSet namespaces;
	
	private String action;
    
    //Used be the ActionForm to inform us that we are creating an entirely new DataStore
    private boolean newDataStore;
	
	/*
	 * Because of the way that STRUTS works, if the user does not check the enabled box,
	 * or unchecks it, setEnabled() is never called, thus we must monitor setEnabled()
	 * to see if it doesn't get called. This must be accessible, as ActionForms need to
	 * know about it -- there is no way we can tell whether we are about to be passed to
	 * an ActionForm or not.
	 * 
	 * Probably a better way to do this, but I can't think of one.
	 * -rgould
	 */
	private boolean enabledChecked = false; 	
	
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);

System.out.println("FormRESET");
		
		enabledChecked = false;		
		action = "";
				
		ServletContext context = getServlet().getServletContext();
		DataConfig config =
			(DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

		dataStores = new TreeSet(config.getDataStores().keySet());
		namespaces = new TreeSet(config.getNameSpaces().keySet());
		dataStoreTypes = new TreeSet( DataStoreUtils.listDataStoresDescriptions());
				
		DataStoreConfig dsConfig;
		
		selectedDataStore = (String) context.getAttribute("selectedDataStore");
		System.out.println("retrieving from context: selectedDataStor:::: " + selectedDataStore);

		dsConfig = config.getDataStore(selectedDataStore);		
		if (dsConfig == null) {
           
            //We could be creating a new DS here.
            if (newDataStore == true){
                System.out.println("[DSFORM]: creating new DS of type " + selectedDataStoreType);
                dsConfig = new DataStoreConfig(selectedDataStoreType);
            } else {
                System.out.println("[DSFORM]: SDS null||empty, so grabbing first one");
    			dsConfig = config.getDataStore( (String) dataStores.first());
            }
		}
		
		dataStoreID = dsConfig.getId(); System.out.println("[DSFORM]: dsID: " + dataStoreID);
		description = dsConfig.getAbstract(); System.out.println("[DSFORM]: desc: " + description);
		enabled = dsConfig.isEnabled(); System.out.println("[DSFORM]: enabld: " + enabled);
		namespace = dsConfig.getNameSpaceId(); System.out.println("[DSFORM]: namespace: " + namespace);

		//Retrieve connection params		
		
		connectionParamKeys   = new ArrayList (dsConfig.getConnectionParams().keySet());
		connectionParamValues = new ArrayList (dsConfig.getConnectionParams().values());
		
		System.out.println("------------------------------DUMP------------------------------");
		System.out.println(":KEYS:"+ connectionParamKeys.toString() + " ========= "+connectionParamKeys.size());
        System.out.println(":VALUES:"+ connectionParamValues.toString() + " ========= "+connectionParamValues.size());
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}
	/**
	 * @return
	 */
	public String getDataStoreID() {
		return dataStoreID;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @return
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param string
	 */
	public void setDataStoreID(String string) {
		dataStoreID = string;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param b
	 */
	public void setEnabled(boolean b) {
		enabledChecked = true;		
		enabled = b;
	}

	/**
	 * @param string
	 */
	public void setNamespace(String string) {
		namespace = string;
	}

	/**
	 * @return
	 */
	public boolean isEnabledChecked() {
		return enabledChecked;
	}

	/**
	 * @param b
	 */
	public void setEnabledChecked(boolean b) {
		enabledChecked = b;
	}

	/**
	 * @return
	 */
	public String getSelectedDataStore() {
		return selectedDataStore;
	}

	/**
	 * @param string
	 */
	public void setSelectedDataStore(String string) {
		ServletContext context = getServlet().getServletContext();
		context.setAttribute("selectedDataStore", string);
		System.out.println("selectedDataStor in context set to " + string);
		selectedDataStore = string;
	}
	
	public Collection getDataStores() {
		return dataStores;
	}
	
	public Collection getDataStoreTypes() {
		return dataStoreTypes;
	}
	
	public Collection getNamespaces() {
		return namespaces;
	}

	/**
	 * @return
	 */
	public String getSelectedDataStoreType() {
		return selectedDataStoreType;
	}

	/**
	 * @param string
	 */
	public void setSelectedDataStoreType(String string) {
		selectedDataStoreType = string;
	}

	/**
	 * @return
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param string
	 */
	public void setAction(String string) {
		action = string;
	}


	/**
	 * @return
	 */
	public List getConnectionParamKeys() {
		return connectionParamKeys;
	}
	/**
	 * @return
	 */
	public String getConnectionParamKey(int index) {
		return (String) connectionParamKeys.get(index);
	}
	/**
	 * @return
	 */
	public String getConnectionParamValue(int index) {
		return (String) connectionParamValues.get(index);
	}

	/**
	 * @param list
	 */
	public void setConnectionParamValues(int index, String value) {
		connectionParamValues.set(index, value);
	}

	/**
	 * setNewDataStore purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param b
	 */
	public void setNewDataStore(boolean b) {
		newDataStore = b;
	}

}
