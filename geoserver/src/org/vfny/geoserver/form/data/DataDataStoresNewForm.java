/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * DataDataStoresNewForm purpose.
 * <p>
 * Description of DataDataStoresNewForm ...
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
 * DataDataStoresNewForm x = new DataDataStoresNewForm(...);
 * </code></pre>
 * 
 * @author User, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: DataDataStoresNewForm.java,v 1.1.2.2 2004/01/12 02:16:19 jive Exp $
 */
public class DataDataStoresNewForm extends ActionForm {
    private TreeSet dataStoreTypes;
    private String selectedDataStoreType;
    private String dataStoreID;

    
    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
        
        //POPULATE dataStoreTypes
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        return errors;
    }
}
