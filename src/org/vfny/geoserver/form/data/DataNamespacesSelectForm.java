/*
 * Created on Feb 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.NameSpaceConfig;


/**
 * DataNamespacesSelectForm
 * <p>
 * @author rgould, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: DataNamespacesSelectForm.java,v 1.1 2004/02/18 19:32:51 emperorkefka Exp $
 */
public class DataNamespacesSelectForm extends ActionForm {
    private String selectedNamespace;
    private String action;
    private TreeSet namespaces;
    
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);

        action = "";
        selectedNamespace="";
        
        ServletContext context = getServlet().getServletContext();
        DataConfig config = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);        
        namespaces = new TreeSet(config.getNameSpaces().keySet());
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public TreeSet getNamespaces() {
        return namespaces;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getSelectedNamespace() {
        return selectedNamespace;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setSelectedNamespace(String string) {
        selectedNamespace = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getAction() {
        return action;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setAction(String string) {
        action = string;
    }    
}
