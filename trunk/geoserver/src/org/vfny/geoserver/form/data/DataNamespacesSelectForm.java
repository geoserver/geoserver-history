/*
 * Created on Feb 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.Requests;

/**
 * DataNamespacesSelectForm
 * <p>
 * @author rgould, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: DataNamespacesSelectForm.java,v 1.5 2004/04/03 00:24:28 emperorkefka Exp $
 */
public class DataNamespacesSelectForm extends ActionForm {

	/**
	 * namespace the user selected (value is a prefix)
	 * 
	 * @uml.property name="selectedNamespace" multiplicity="(0 1)"
	 */
	private String selectedNamespace;

	/**
	 * Action the user clicked on
	 * 
	 * @uml.property name="action" multiplicity="(0 1)"
	 */
	private String action;

	/**
	 * 
	 * @uml.property name="namespaces"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private TreeSet namespaces;

    
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);

        action = "";

        UserContainer user = Requests.getUserContainer( request );
        if( user == null){
            return; // User not logged in, probably the JSPCompiler
        }
        selectedNamespace=user.getPrefix();
        
        // populate and sort available namespaces
        //
        DataConfig config = ConfigRequests.getDataConfig(request);        
        namespaces = new TreeSet(config.getNameSpaces().keySet());
        String def = config.getDefaultNameSpace().getPrefix();
        if (namespaces.contains(def)) {
        	namespaces.remove(def);
        	namespaces.add(def+"*");
        }
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

	/**
	 * Access action property.
	 * 
	 * @return Returns the action.
	 * 
	 * @uml.property name="action"
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Set action to action.
	 * 
	 * @param action The action to set.
	 * 
	 * @uml.property name="action"
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Access selectedNamespace property.
	 * 
	 * @return Returns the selectedNamespace.
	 * 
	 * @uml.property name="selectedNamespace"
	 */
	public String getSelectedNamespace() {
		return selectedNamespace;
	}

	/**
	 * Set selectedNamespace to selectedNamespace.
	 * 
	 * @param selectedNamespace The selectedNamespace to set.
	 * 
	 * @uml.property name="selectedNamespace"
	 */
	public void setSelectedNamespace(String selectedNamespace) {
		this.selectedNamespace = selectedNamespace;
	}

	/**
	 * Access namespaces property.
	 * 
	 * @return Returns the namespaces.
	 * 
	 * @uml.property name="namespaces"
	 */
	public TreeSet getNamespaces() {
		return namespaces;
	}

}
