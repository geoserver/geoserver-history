package org.vfny.geoserver.form.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
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
 * Represents the information required for editing a DataStore.
 * <p>
 * The parameters required by a DataStore are dynamically generated from the
 * DataStoreFactorySPI. Most use of DataStoreFactorySPI has been hidden behind
 * the DataStoreUtil class.
 * </p>
 * 
 * @author Richard Gould, Refractions Research
 */
public class DataDataStoresEditorForm extends ActionForm {
    
	/**
     * Used to identify the DataStore being edited.
     * Maybe we should grab this from session?
     */
	private String dataStoreId;
    
    /** Enabled status of DataStore */
	private boolean enabled;
    
    /* NamespaceID used for DataStore content */
	private String namespaceId;
    
    /* Description of DataStore (abstract?) */
	private String description;
	
	// These are not stored in a single map so we can access them
	// easily from JSP page
    //
    /** String representation of connection parameter keys */
	private List paramKeys;
    
    /** String representation of connection parameter values */
	private List paramValues;
	
    //
    // More hacky attempts to transfer information into the JSP smoothly
    //
    /** Available NamespaceIds */
	private SortedSet namespaces;
	
	/**
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
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request );

		enabledChecked = false;		
				
		ServletContext context = getServlet().getServletContext();
		DataConfig config =(DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

		namespaces = new TreeSet(config.getNameSpaces().keySet());
				
		
		dataStoreId = (String) request.getSession().getAttribute("selectedDataStoreId");
        
		DataStoreConfig dsConfig = config.getDataStore( dataStoreId );
        
        description = dsConfig.getAbstract();
		enabled = dsConfig.isEnabled();
		namespaceId = dsConfig.getNameSpaceId();

		//Retrieve connection params		
		paramKeys   = new ArrayList(dsConfig.getConnectionParams().keySet());
		paramValues = new ArrayList(dsConfig.getConnectionParams().values());
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}

	/**
	 * @return
	 */
	public List getParamKeys() {
		return paramKeys;
	}
	/**
	 * @return
	 */
	public String getParamKey(int index) {
		return (String) paramKeys.get(index);
	}
	/**
	 * @return
	 */
	public String getParamValue(int index) {
		return (String) paramValues.get(index);
	}

	/**
	 * @param list
	 */
	public void setParamValues(int index, String value) {
		paramValues.set(index, value);
	}

    /**
     * getDataStoreId purpose.
     * <p>
     * Description ...
     * </p>
     * @return
     */
    public String getDataStoreId() {
        return dataStoreId;
    }

    /**
     * getDescription purpose.
     * <p>
     * Description ...
     * </p>
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * isEnabled purpose.
     * <p>
     * Description ...
     * </p>
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * getNamespaces purpose.
     * <p>
     * Description ...
     * </p>
     * @return
     */
    public SortedSet getNamespaces() {
        return namespaces;
    }

    /**
     * getParamValues purpose.
     * <p>
     * Description ...
     * </p>
     * @return
     */
    public List getParamValues() {
        return paramValues;
    }

    /**
     * setDescription purpose.
     * <p>
     * Description ...
     * </p>
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * setEnabled purpose.
     * <p>
     * Description ...
     * </p>
     * @param b
     */
    public void setEnabled(boolean b) {
        enabled = b;
    }

    /**
     * setParamKeys purpose.
     * <p>
     * Description ...
     * </p>
     * @param list
     */
    public void setParamKeys(List list) {
        paramKeys = list;
    }

    /**
     * setParamValues purpose.
     * <p>
     * Description ...
     * </p>
     * @param list
     */
    public void setParamValues(List list) {
        paramValues = list;
    }

    /**
     * getNamespaceId purpose.
     * <p>
     * Description ...
     * </p>
     * @return
     */
    public String getNamespaceId() {
        return namespaceId;
    }

    /**
     * setNamespaceId purpose.
     * <p>
     * Description ...
     * </p>
     * @param string
     */
    public void setNamespaceId(String string) {
        namespaceId = string;
    }

}
