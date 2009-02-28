/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.data.util.CoverageUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.form.data.DataCoveragesNewForm;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.CoverageStoreInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

        CoverageConfig coverageConfig = newCoverageConfig(cvStoreInfo, formatID, request, getCatalog());

        user.setCoverageConfig(coverageConfig);

        return mapping.findForward("config.data.coverage.editor");
    }

    /**
     * Static method so that the CoverageStore editor can do the same thing that the new one
     * does.*/
    public static CoverageConfig newCoverageConfig(CoverageStoreInfo cvStoreInfo, String formatID,
        HttpServletRequest request, Catalog catalog ) throws ConfigurationException {
        //GridCoverage gc = null;
        final Format format = cvStoreInfo.getFormat();
        AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) cvStoreInfo.getReader();

        if (reader == null) {
            reader = (AbstractGridCoverage2DReader) ((AbstractGridFormat) format).getReader(GeoserverDataDirectory
                    .findDataFile(cvStoreInfo.getUrl()));
        }

        if (reader == null) {
            throw new ConfigurationException(
                "Could not obtain a reader for the CoverageDataSet. Please check the CoverageDataSet configuration!");
        }

        CoverageConfig coverageConfig = 
            new CoverageConfig(formatID, format, reader, request,catalog);

        request.setAttribute(NEW_COVERAGE_KEY, "true");
        request.getSession().setAttribute(DataConfig.SELECTED_COVERAGE, coverageConfig);

        return coverageConfig;
    }
}
