/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionForm;

/**
 * DataDataStoresSelectForm purpose.
 * <p>
 * Description of DataDataStoresSelectForm ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * DataDataStoresSelectForm x = new DataDataStoresSelectForm(...);
 * </code></pre>
 * 
 * @author User, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: DataDataStoresSelectForm.java,v 1.1.2.1 2004/01/12 02:10:10 emperorkefka Exp $
 */
public class DataDataStoresSelectForm extends ActionForm {
    
    private String action;   
    private TreeSet dataStores;
    private String selectedDataStore;
    
    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
        
        //POPULATE dataStores WITH AVAILABLE DATA STORES
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        return errors;
    }    
}
