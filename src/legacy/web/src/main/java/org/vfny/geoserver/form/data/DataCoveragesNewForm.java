/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;


/**
 * DataCoveragesNewForm purpose.
 *
 * @author rgould, Refractions Research, Inc.
 * @author dmzwiers
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class DataCoveragesNewForm extends ActionForm {
    /**
     *
     */
    private static final long serialVersionUID = 2866636958005021322L;

    /**
     *
     */
    String selectedNewCoverage;

    /**
     *
     */
    HttpServletRequest request;

    public SortedSet getNewCoverages() {
        DataConfig dataConfig = (DataConfig) request.getSession().getServletContext()
                                                    .getAttribute(DataConfig.CONFIG_KEY);

        TreeSet out = new TreeSet(dataConfig.getCoverageIdentifiers(
                    getServlet().getServletContext()));
        out.removeAll(dataConfig.getCoverages().keySet());

        return out;
    }

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
        this.request = request;

        selectedNewCoverage = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    /**
     * Access selectedNewCoverage property.
     *
     * @return Returns the selectedNewCoverage.
     */
    public String getSelectedNewCoverage() {
        return selectedNewCoverage;
    }

    /**
     * Set selectedNewCoverage to selectedNewCoverage.
     *
     * @param selectedNewCoverage The selectedNewCoverage to set.
     */
    public void setSelectedNewCoverage(String selectedNewCoverage) {
        this.selectedNewCoverage = selectedNewCoverage;
    }
}
