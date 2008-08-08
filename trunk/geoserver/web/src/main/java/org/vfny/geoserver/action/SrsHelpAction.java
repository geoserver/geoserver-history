/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.geotools.factory.Hints;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.crs.GeoserverCustomWKTFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Simone Giannecchini, GeoSolutions
 *
 */
public class SrsHelpAction extends Action {
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(SrsHelpAction.class.getName());

    /**
     *  This is a simple action - it reads in the GT2 supported EPSG codes.
     *
     * DONE: once geosever support EPSG thats not in the properties file, this should
     *       be a bit more abstract and get a list of all EPSG defs from the
     *       GDSFactory (if possible).  Use toWKT() as its nicer to read.
     *
     *   Form has two properies - ids  (list of String - the epsg #)
     *                            defs (list of String - the epsg WKT definitions)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        List<String> defs = new ArrayList<String>();
        List<String> ids_string = new ArrayList<String>();

        Set<String> codes = CRS.getSupportedCodes("EPSG");

        CRSAuthorityFactory customFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG",
                new Hints(Hints.CRS_AUTHORITY_FACTORY, GeoserverCustomWKTFactory.class));
        Set<String> customCodes = new HashSet<String>();

        try {
            customCodes = customFactory.getAuthorityCodes(CoordinateReferenceSystem.class);
        } catch (FactoryException e) {
            LOGGER.log(Level.WARNING, "Error occurred while trying to gather custom CRS codes", e);
        }

        // make a set with each code
        Set<String> idSet = new HashSet<String>();
        for (String code : codes) {
            String id = code.substring(code.indexOf(':') + 1); // just the non prefix part
            idSet.add(id);
        }
        List<String> ids = new ArrayList<String>(idSet);
        Collections.sort(ids); //sort to get them in order

        CoordinateReferenceSystem crs;
        for (String id : ids) {
            try { //get its definition
                crs = CRS.decode("EPSG:" + id);
                String def = crs.toString();
                defs.add(def);
                ids_string.add(id.toString());
            } catch (Exception e) {
                if (customCodes.contains(id.toString())) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Issues converting EPSG:" + id + ".", e);
                    } else {
                        LOGGER.log(Level.WARNING,
                            "Issues converting EPSG:" + id + ". " + e.getLocalizedMessage()
                            + " Stack trace included at FINE logging level");
                    }
                } else if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "Issues converting EPSG:" + id, e);
                }
            }
        }

        //send off to the .jsp
        DynaActionForm myForm = (DynaActionForm) form;

        myForm.set("srsDefinitionList", defs.toArray(new String[defs.size()]));
        myForm.set("srsIDList", ids_string.toArray(new String[ids_string.size()]));

        // return back to the admin demo
        //
        return mapping.findForward("success");
    }
}
