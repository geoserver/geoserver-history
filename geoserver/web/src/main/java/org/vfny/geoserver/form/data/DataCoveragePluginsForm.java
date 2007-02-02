/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.opengis.coverage.grid.Format;
import org.vfny.geoserver.util.CoverageStoreUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;


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
    private List formats;

    /**
     *
     */
    private List formatDescriptions;

    /**
     *
     */
    private List formatIDs;

    /**
     * Default state of New form
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        formats = CoverageStoreUtils.listDataFormats();
        formatDescriptions = new ArrayList();
        formatIDs = new ArrayList();

        Format fTmp;

        for (Iterator i = formats.iterator(); i.hasNext();) {
            fTmp = (Format) i.next();
            formatDescriptions.add(fTmp.getDescription());
            formatIDs.add(fTmp.getName());
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
        return formatDescriptions;
    }

    /**
     *
     */
    public List getFormatIDs() {
        return formatIDs;
    }

    /**
     *
     */
    public List getFormats() {
        return formats;
    }
}
