/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.config.AttributeTypeInfoConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.xml.GMLUtils;
/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataAttributeTypesEditorForm extends ActionForm {
	
	private boolean ref;
	private boolean nillible;
	private String minOccurs;
	private String maxOccurs;
	private String name;
	private String selectedType;
	private String fragment;
    
    HttpServletRequest request;
	
	
	public void reset(ActionMapping arg0, HttpServletRequest request) {
		super.reset(arg0, request);
        this.request = request;
		
        FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession().getAttribute(DataConfig.SELECTED_FEATURE_TYPE);
		AttributeTypeInfoConfig config = (AttributeTypeInfoConfig) request.getSession().getAttribute(DataConfig.SELECTED_ATTRIBUTE_TYPE);
		
		nillible = config.isNillable();
		minOccurs = Integer.toString(config.getMinOccurs());
		maxOccurs = Integer.toString(config.getMaxOccurs());
		name = config.getName();
		selectedType = config.getType();
		fragment = config.getFragment();
		
		if (selectedType.equals(AttributeTypeInfoConfig.TYPE_FRAGMENT)) {
			ref = false;
		} else {
			ref = true;
		}
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}
	/**
	 * @return Returns the fragment.
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * @param fragment The fragment to set.
	 */
	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	/**
	 * @return Returns the maxOccurs.
	 */
	public String getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * @param maxOccurs The maxOccurs to set.
	 */
	public void setMaxOccurs(String maxOccurs) {
		this.maxOccurs = maxOccurs;
	}

	/**
	 * @return Returns the minOccurs.
	 */
	public String getMinOccurs() {
		return minOccurs;
	}

	/**
	 * @param minOccurs The minOccurs to set.
	 */
	public void setMinOccurs(String minOccurs) {
		this.minOccurs = minOccurs;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the nillible.
	 */
	public boolean isNillible() {
		return nillible;
	}

	/**
	 * @param nillible The nillible to set.
	 */
	public void setNillible(boolean nillible) {
		this.nillible = nillible;
	}

	/**
	 * @return Returns the ref.
	 */
	public boolean isRef() {
		return ref;
	}

	/**
	 * @param ref The ref to set.
	 */
	public void setRef(boolean ref) {
		this.ref = ref;
	}

	/**
	 * @return Returns the selectedType.
	 */
	public String getSelectedType() {
		return selectedType;
	}

	/**
	 * @param selectedType The selectedType to set.
	 */
	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}
    
    public SortedSet getAttributeTypes() throws IOException {

        TreeSet set = new TreeSet();
        
        ServletContext context = getServlet().getServletContext();
        DataConfig dataConfig = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);
        FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession().getAttribute(DataConfig.SELECTED_FEATURE_TYPE);
                
        DataStore dataStore = dataConfig.getDataStore(ftConfig.getDataStoreId()).findDataStore();
       
        FeatureType featureType = dataStore.getSchema(ftConfig.getName());
        AttributeType[] types = featureType.getAttributeTypes();
        
        for (int i = 0; i < types.length; i++) {
            List list = GMLUtils.schemaList(types[i].getName(), types[i].getClass());
            for (Iterator iter = list.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
                set.add(element);
			}
        }    
        return set;
    }
}
