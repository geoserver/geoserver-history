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
    private final static Logger LOGGER = Logger.getLogger(SrsHelpAction.class
            .toString());

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
        ArrayList defs = new ArrayList();
        ArrayList ids_string = new ArrayList();

        Set codes = CRS.getSupportedCodes("EPSG");

        CRSAuthorityFactory customFactory = ReferencingFactoryFinder
            .getCRSAuthorityFactory("EPSG",
                new Hints(Hints.CRS_AUTHORITY_FACTORY,
                    GeoserverCustomWKTFactory.class));
        Set customCodes = new HashSet();

        try {
            customCodes = customFactory.getAuthorityCodes(CoordinateReferenceSystem.class);
        } catch (FactoryException e) {
            LOGGER.log(Level.WARNING,
                "Error occurred while trying to gather custom CRS codes", e);
        }

        // make an array of each code (as an int)
        HashSet idSet = new HashSet();
        Iterator codeIt = codes.iterator();

        while (codeIt.hasNext()) {
            String code = (String) codeIt.next();
            String id = code.substring(code.indexOf(':') + 1); //just the number

            idSet.add(Integer.valueOf(id));
        }

        List ids = new ArrayList(idSet);
        Collections.sort(ids); //sort to get them in order

        CoordinateReferenceSystem crs;
        String def;
        codeIt = ids.iterator();

        Integer id;

        while (codeIt.hasNext()) //for each id (in sorted order)
         {
            id = (Integer) codeIt.next();

            try { //get its definition
                crs = CRS.decode("EPSG:" + id);
                def = crs.toString();
                defs.add(def);
                ids_string.add(id.toString());
            } catch (Exception e) {
                if (customCodes.contains(id.toString())) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                            "Issues converting EPSG:" + id + ".", e);
                    } else {
                        LOGGER.log(Level.WARNING,
                            "Issues converting EPSG:" + id + ". "
                            + e.getLocalizedMessage()
                            + " Stack trace included at FINE logging level");
                    }
                } else if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "Issues converting EPSG:" + id, e);
                }
            }
        }

        //send off to the .jsp
        DynaActionForm myForm = (DynaActionForm) form;

        myForm.set("srsDefinitionList", defs.toArray(new String[1]));
        myForm.set("srsIDList", ids_string.toArray(new String[1]));

        // return back to the admin demo
        //
        return mapping.findForward("success");
    }
}
