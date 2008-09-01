/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geoserver.catalog.Catalog;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.Driver;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.form.data.DataCoveragesNewForm;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.CoverageStoreInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.UserContainer;


/**
 * DataCoveragesNewAction purpose.
 *
 * <p>
 * Description of DataCoveragesNewAction ...
 * </p>
 *
 * <p>
 * Capabilities:
 * </p>
 *
 * <ul>
 * <li> Coverage: description </li>
 * </ul>
 *
 * <p>
 * Example Use:
 * </p>
 *
 * <pre><code>
 *     DataCoveragesNewAction x = new DataCoveragesNewAction(...);
 * </code></pre>
 *
 * @author rgould, Refractions Research, Inc.
 * @author cholmesny
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id: DataCoveragesNewAction.java,v 1.15 2004/09/17 16:34:47
 *          cholmesny Exp $
 */
public class DataCoveragesNewAction extends ConfigAction {
    public final static String NEW_COVERAGE_KEY = "newCoverage";

    public ActionForward execute(ActionMapping mapping, ActionForm incomingForm,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws ConfigurationException {
        final DataCoveragesNewForm form = (DataCoveragesNewForm) incomingForm;
        final String formatID = form.getSelectedNewCoverage();
        final Data catalog = getData();
        CoverageStoreInfo cvStoreInfo = catalog.getFormatInfo(formatID);

        if (cvStoreInfo == null) {
            org.geoserver.catalog.CoverageStoreInfo cvStore = 
                getCatalog().getFactory().createCoverageStore();
            cvStoreInfo = new CoverageStoreInfo( cvStore, getCatalog() );
            cvStoreInfo.load(getDataConfig().getDataFormat(formatID).toDTO());
            //cvStoreInfo = new CoverageStoreInfo(getDataConfig().getDataFormat(formatID).toDTO(),
            //        getCatalog());
        }

        CoverageConfig[] coverageConfigs = newCoverageConfig(cvStoreInfo, formatID, request, getCatalog());

        if (coverageConfigs.length == 1) {
            user.setCoverageConfig(coverageConfigs[0]);

            return mapping.findForward("config.data.coverage.editor");
        } else if (coverageConfigs.length > 1) {
            final DataConfig dataConfig = (DataConfig) getDataConfig();

            for (int ci = 0; ci < coverageConfigs.length; ci++) {
                final CoverageConfig config = coverageConfigs[ci];
                final StringBuffer coverage = new StringBuffer(config.getFormatId());
                dataConfig.addCoverage(coverage.append(":").append(config.getName()).toString(), config);
            }

            // Don't think reset is needed (as me have moved on to new page)
            // form.reset(mapping, request);
            getApplicationState().notifyConfigChanged();

            // Coverage no longer selected
            user.setCoverageConfig(null);

            return mapping.findForward("config.data.coverage");
        }

        return mapping.getInputForward();
    }

    /**
     * Static method so that the CoverageStore editor can do the same thing that the new one
     * does.*/
    public static CoverageConfig[] newCoverageConfig(CoverageStoreInfo cvStoreInfo, String formatID,
        HttpServletRequest request, Catalog catalog ) throws ConfigurationException {
        //GridCoverage gc = null;
        final Driver driver = cvStoreInfo.getDriver();
        CoverageAccess cvAccess = cvStoreInfo.getCoverageAccess();

        if (cvAccess == null) {
            // TODO: FIX THIS!!!
//            cvAccess = (AbstractGridCoverage2DReader) ((AbstractGridFormat) driver).getReader(GeoserverDataDirectory.findDataFile(cvStoreInfo.getUrl()));
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            try {
                params.put("url", GeoserverDataDirectory.findDataFile(cvStoreInfo.getUrl()).toURI().toURL());
            } catch (MalformedURLException e) {
                ConfigurationException exception = new ConfigurationException("Could not retrieve the Coverage source file due to the following error: " + e.getLocalizedMessage());
                exception.setStackTrace(e.getStackTrace());
                throw exception;
            }
            try {
                cvAccess = driver.create(params, null, null);
            } catch (IOException e) {
                ConfigurationException exception = new ConfigurationException("Could not get access the Coverage due to the following error: " + e.getLocalizedMessage());
                exception.setStackTrace(e.getStackTrace());
                throw exception;
            }
        }

        if (cvAccess == null) {
            throw new ConfigurationException(
                "Could not obtain a reader for the CoverageDataSet. Please check the CoverageDataSet configuration!");
        }

//        CoverageConfig coverageConfig = new CoverageConfig(formatID, driver, cvAccess, ConfigRequests.getDataConfig(request));
        int numCoverages = cvAccess.getNumCoverages(null);
        CoverageConfig[] coverageConfigs = new CoverageConfig[numCoverages];

        for (int i=0; i<numCoverages; i++) {
            coverageConfigs[i] = new CoverageConfig(formatID, driver, cvAccess, cvAccess.getNames(null).get(i), request);
        }
        
        request.setAttribute(NEW_COVERAGE_KEY, "true");
        request.getSession().setAttribute(DataConfig.SELECTED_COVERAGE, coverageConfigs[0]);

        return coverageConfigs;
    }
}
