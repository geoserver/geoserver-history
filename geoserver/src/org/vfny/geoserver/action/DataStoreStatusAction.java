/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Checks Data.statusDataStoers for the end user.
 * <p>
 * This is a GeoServerAction - it has nothing to do with the Configuration
 * system.
 * </p>
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: DataStoreStatusAction.java,v 1.1 2004/01/17 00:49:30 jive Exp $
 */
public class DataStoreStatusAction extends GeoServerAction {
    /**
     * Implementation of execute.
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @return
     * @throws java.lang.Exception
     */
    public ActionForward execute(
        ActionMapping arg0,
        ActionForm arg1,
        ServletRequest arg2,
        ServletResponse arg3)
        throws Exception {
        return super.execute(arg0, arg1, arg2, arg3);
    }
}
