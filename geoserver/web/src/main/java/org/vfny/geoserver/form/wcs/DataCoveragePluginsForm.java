/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.wcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geotools.coverage.io.Driver;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class DataCoveragePluginsForm extends ActionForm {
    private static final Pattern idPattern = Pattern.compile("^\\a$");

    /**
     *
     */
    private Driver[] drivers;

    /**
     *
     */
    private List driverDescriptions;

    /**
     *
     */
    private List driverIDs;

    /**
     * Default state of New form
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        drivers = CoverageStoreUtils.drivers;
        driverDescriptions = CoverageStoreUtils.listDataFormatsDescriptions();
        driverIDs = new ArrayList();


        for (int i = 0; i < drivers.length; i++) {
            driverDescriptions.add(drivers[i].getDescription().toString());
            driverIDs.add(drivers[i].getName());
        }
    }

    /**
     * Check NewForm for correct use
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    /*
     * Allows the JSP page to easily access the list of dataFormat Descriptions
     */
    public List getFormatDescriptions() {
        return driverDescriptions;
    }

    public void setFormatDescriptions(List desc) {
        driverDescriptions = desc;
    }

    /**
     *
     */
    public List getFormatIDs() {
        return driverIDs;
    }

    public void setFormatIDs(List ids) {
        driverIDs = ids;
    }

    /**
     *
     */
    public List getDrivers() {
        return Arrays.asList(drivers);
    }

    public void setDrivers(List f) {
        drivers = (Driver[]) f.toArray(new Driver[]{});
    }
}
